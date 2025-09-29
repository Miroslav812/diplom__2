package tests;

import clients.IngredientClient;
import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.Order;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

@RunWith(JUnit4.class)
@Epic("Stellar Burgers API")
@Feature("Создание заказов")
public class OrderCreationTests extends BaseTest {

    private UserClient userClient;
    private OrderClient orderClient;
    private IngredientClient ingredientClient;
    private String accessToken;
    private List<String> ingredients;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();
        ingredientClient = new IngredientClient();

        // создаём пользователя
        String email = "order_" + UUID.randomUUID() + "@example.com";
        String password = "123456";
        String name = "OrderUser";

        Response response = userClient.createUser(new User(email, password, name));
        accessToken = response.jsonPath().getString("accessToken");

        // получаем список ингредиентов
        Response ingredientsResponse = ingredientClient.getIngredients();
        ingredients = ingredientsResponse.jsonPath().getList("data._id");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
            accessToken = null;
        }
    }

    @Test
    @Story("Создание заказа авторизованным пользователем")
    @Description("Ожидаем 200 OK, success=true и возврат номера заказа")
    public void createOrderWithAuth() {
        Order order = new Order(ingredients.subList(0, 2));

        Response response = orderClient.createOrder(accessToken, order);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @Story("Создание заказа без авторизации")
    @Description("Ожидаем 200 OK, success=true, заказ создаётся без привязки к пользователю")
    public void createOrderWithoutAuth() {
        Order order = new Order(ingredients.subList(0, 2));

        Response response = orderClient.createOrderWithoutAuth(order);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", notNullValue());
    }

    @Test
    @Story("Создание заказа без ингредиентов")
    @Description("Ожидаем 400 Bad Request и сообщение 'Ingredient ids must be provided'")
    public void createOrderWithoutIngredients() {
        Order order = new Order(Arrays.asList());

        Response response = orderClient.createOrder(accessToken, order);

        response.then()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Story("Создание  заказа с некорректными данными")
    @Description("Ожидаем 500 Internal Server Error при некорректном id ингредиента")
    public void createOrderWithInvalidIngredient() {
        Order order = new Order(Arrays.asList("invalid_ingredient"));

        Response response = orderClient.createOrder(accessToken, order);

        response.then()
                .statusCode(500);
    }
}