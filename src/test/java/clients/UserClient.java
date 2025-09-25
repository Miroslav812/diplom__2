package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String REGISTER = "/api/auth/register";
    private static final String LOGIN = "/api/auth/login";
    private static final String USER = "/api/auth/user";

    @Step("Создать пользователя")
    public Response createUser(Object body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(BASE_URL + REGISTER);
    }

    @Step("Авторизация пользователя")
    public Response loginUser(Object body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(BASE_URL + LOGIN);
    }

    @Step("Изменить данные пользователя")
    public Response updateUser(String token, Object body) {
        return given()
                .header("Authorization", token)
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .patch(BASE_URL + USER);
    }

    @Step("Удалить пользователя")
    public void deleteUser(String token) {
        given()
                .header("Authorization", token)
                .when()
                .delete(BASE_URL + USER);
    }
}