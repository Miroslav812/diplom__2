package tests;

import clients.UserClient;
import io.qameta.allure.*;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(JUnit4.class)
@Epic("Stellar Burgers API")
@Feature("Регистрация пользователя")
public class UserRegistrationTests extends BaseTest {

    private UserClient userClient;
    private String accessToken;
    private String email;
    private final String password = "123456";
    private final String name = "TestUser";

    @Before
    public void setUp() {
        userClient = new UserClient();
        email = "user_" + UUID.randomUUID() + "@example.com";
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
            accessToken = null;
        }
    }

    @Test
    @Story("Успешная регистрация")
    @Description("Ожидаем 200 OK, success=true и возврат accessToken")
    public void createUniqueUser() {
        Response response = userClient.createUser(new User(email, password, name));

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @Story("Регистрация с уже существующими данными")
    @Description("Ожидаем 403 Forbidden и сообщение 'User already exists'")
    public void createExistingUser() {
        // первая регистрация
        Response firstResponse = userClient.createUser(new User(email, password, name));
        accessToken = firstResponse.jsonPath().getString("accessToken");

        // повторная регистрация
        Response secondResponse = userClient.createUser(new User(email, password, name));

        secondResponse.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @Story("Регистрация  без обязательных полей")
    @Description("Ожидаем 403 Forbidden и сообщение 'Email, password and name are required fields'")
    public void createUserWithoutEmail() {
        User user = new User(null, password, name);

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
        // accessToken здесь не сохраняем → tearDown не вызовет удаление
    }
}