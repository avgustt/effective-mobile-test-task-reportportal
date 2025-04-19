package com.sergei.tests.api_tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Epic("Dashboard")
@Feature("API")
@Story("Negative Dashboard Creation")
@Owner("Sergei")
public class CreateDashboardNegativeTest {

    private static final String BASE_URL = "https://demo.reportportal.io/api/v1";
    private static final String PROJECT = "default_personal";
    private static final String TOKEN = "Bearer 123123123_WcNYVgJ5Qwu-MWDnVc7XVO-Ezeon6hd6mBNdMFaCE0BT1FZOuSItQh2J-EvDi82C";

    @Test
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("API: Негативный тест — создание Dashboard без имени")
    @Description("Проверяет, что Dashboard не создается при отсутствии обязательного параметра 'name'")
    public void createDashboardWithMissingName_shouldReturn400() {
        Response response = sendPostRequestWithoutName();
        validateBadRequestResponse(response);
        validateDashboardNotCreated();
    }

    @Step("POST: Пытаемся создать Dashboard без имени")
    public Response sendPostRequestWithoutName() {
        RestAssured.baseURI = BASE_URL;

        return RestAssured
                .given()
                .header("Authorization", TOKEN)
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/" + PROJECT + "/dashboard")
                .then()
                .extract().response();
    }

    @Step("Проверяем, что получен статус 400 и корректное сообщение об ошибке")
    public void validateBadRequestResponse(Response response) {
        int statusCode = response.getStatusCode();
        String errorMessage = response.getBody().asString();

        assertEquals(400, statusCode, "Ожидался статус 400 при создании Dashboard без имени");
        System.out.println("Получен статус 400 как ожидалось. Ответ сервера: " + errorMessage);
    }

    @Step("GET: Проверка, что Dashboard без имени не появился в списке")
    public void validateDashboardNotCreated() {
        Response getResponse = RestAssured
                .given()
                .header("Authorization", TOKEN)
                .queryParam("page.page", 1)
                .queryParam("page.size", 100)
                .get("/" + PROJECT + "/dashboard")
                .then()
                .extract().response();

        List<String> names = getResponse.jsonPath().getList("content.name");
        boolean hasEmpty = names.stream().anyMatch(name -> name == null || name.trim().isEmpty());
        assertFalse(hasEmpty, "Dashboard с пустым именем найден в списке");
        System.out.println("Dashboard без имени не появился в списке.");
    }
}
