package tests;

import clients.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import models.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserUpdateTests extends BaseTest {
    private UserClient userClient;
    private String accessToken;
    private String email;
    private final String password = "123456";
    private final String name = "UpdateTest";

    @Before
    public void setUp() {
        userClient = new UserClient();
        email = "update_" + UUID.randomUUID() + "@example.com";

        // создаём пользователя
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
    @DisplayName("Изменение  данных пользователя с авторизацией")
    @Description("Ожидаем 200 OK и success=true при изменении имени авторизованного пользователя")
    public void updateUserWithAuth() {
        User updated = new User(email, password, "NewName");

        Response response = userClient.updateUser(accessToken, updated);

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.name", equalTo("NewName"))
                .body("user.email", notNullValue());
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Ожидаем 401 Unauthorized и сообщение 'You should be authorised'")
    public void updateUserWithoutAuth() {
        User updated = new User(email, password, "NoAuth");

        Response response = userClient.updateUser("", updated);

        response.then()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }
}