package tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

public class BaseTest {
    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.useRelaxedHTTPSValidation(); // игнорируем SSL ошибки
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }
}