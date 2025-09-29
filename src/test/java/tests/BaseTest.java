package tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

/**
 * Базовый класс для всех API-тестов.
 * Содержит настройку RestAssured и общую конфигурацию.
 */
public class BaseTest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.useRelaxedHTTPSValidation(); // Игнорируем SSL-ошибки
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails(); // Логируем только при падениях
    }
}