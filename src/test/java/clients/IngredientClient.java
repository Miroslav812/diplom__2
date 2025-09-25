package clients;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class IngredientClient {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private static final String INGREDIENTS = "/api/ingredients";

    @Step("Получить список ингредиентов")
    public Response getIngredients() {
        return given()
                .when()
                .get(BASE_URL + INGREDIENTS);
    }
}