package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String ORDERS = "/api/orders";

    @Step("Создать заказ")
    public Response createOrder(String token, Object body) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(body)
                .when()
                .post(BASE_URL + ORDERS);
    }

    @Step("Создать заказ без авторизации")
    public Response createOrderWithoutAuth(Object body) {
        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(BASE_URL + ORDERS);
    }

    @Step("Получить заказы пользователя")
    public Response getUserOrders(String token) {
        return given()
                .header("Authorization", token)
                .when()
                .get(BASE_URL + ORDERS);
    }

    @Step("Получить заказы без авторизации")
    public Response getOrdersWithoutAuth() {
        return given()
                .when()
                .get(BASE_URL + ORDERS);
    }
}