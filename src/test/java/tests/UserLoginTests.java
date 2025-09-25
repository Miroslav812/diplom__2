package tests;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import models.UserCredentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserLoginTests extends BaseTest {
    private UserClient userClient;
    private String accessToken;
    private String email;
    private final String password = "123456";
    private final String name = "LoginTest";

    @Before
    public void setUp() {
        userClient = new UserClient();
        email = "login_" + UUID.randomUUID() + "@example.com";

        // создаём пользователя для тестов
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
    @DisplayName("Успешный логин под существующим пользователем")
    @Description("Ожидаем 200 OK, success=true и возвращаемый accessToken")
    public void loginExistingUser() {
        Response response = userClient.loginUser(new UserCredentials(email, password));

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("Ожидаем 401 Unauthorized и сообщение 'email or password are incorrect'")
    public void loginWithWrongPassword() {
        Response response = userClient.loginUser(new UserCredentials(email, "wrongPass"));

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));

        // accessToken здесь не получаем → tearDown ничего не удаляет
    }
}