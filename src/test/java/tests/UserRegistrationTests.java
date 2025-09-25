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
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешной регистрации нового юзера")
    public void createUniqueUser() {
        Response response = userClient.createUser(new User(email, password, name));

        response.then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue());

        accessToken = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание уже зарегистрированного пользователя")
    @Description("Ожидаем 403 при повторной регистрации с теми же данными")
    public void createExistingUser() {
        // регистрируем первого
        Response firstResponse = userClient.createUser(new User(email, password, name));
        accessToken = firstResponse.jsonPath().getString("accessToken");

        // повторная регистрация
        Response secondResponse = userClient.createUser(new User(email, password, name));

        secondResponse.then()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Ожидаем 403 и ошибку обязательных полей")
    public void createUserWithoutEmail() {
        User user = new User(null, password, name);

        Response response = userClient.createUser(user);

        response.then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
        // accessToken не сохраняем — tearDown ничего не удаляет
    }
}