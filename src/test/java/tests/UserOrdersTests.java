package tests;

import clients.OrderClient;
import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class UserOrdersTests extends BaseTest {
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        orderClient = new OrderClient();

        // создаём пользователя
        String email = "orders_" + UUID.randomUUID() + "@example.com";
        String password = "123456";
        String name = "OrdersUser";
        Response response = userClient.createUser(new User(email, password, name));
        accessToken = response.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
            accessToken = null;
        }
    }

    @Test
    @DisplayName("Получение заказов авторизованным пользователем")
    @Description("Ожидаем 200 OK, success=true и список заказов")
    public void getOrdersWithAuth() {
        Response response = orderClient.getUserOrders(accessToken);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов без авторизации")
    @Description("Ожидаем 401 Unauthorized и сообщение 'You should be authorised'")
    public void getOrdersWithoutAuth() {
        Response response = orderClient.getOrdersWithoutAuth();

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}