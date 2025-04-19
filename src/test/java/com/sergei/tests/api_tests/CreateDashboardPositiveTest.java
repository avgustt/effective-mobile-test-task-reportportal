package com.sergei.tests.api_tests;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.*;

@Epic("Dashboard")
@Feature("API")
@Story("Create Dashboard")
@Owner("Sergei")
public class CreateDashboardPositiveTest {

    private static final String BASE_URL = "https://demo.reportportal.io/api/v1";
    private static final String PROJECT = "default_personal";
    private static final String TOKEN = "Bearer 123123123_WcNYVgJ5Qwu-MWDnVc7XVO-Ezeon6hd6mBNdMFaCE0BT1FZOuSItQh2J-EvDi82C";

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("API: Создание Dashboard с обязательным полем 'name'")
    @Description("Создаёт Dashboard через API, проверяет статус 201 и наличие в списке")
    public void createDashboardAndVerifyInList() {
        String dashboardName = "API_UniqueDashboard_" + System.currentTimeMillis();

        String dashboardId = step("POST: Создание Dashboard с именем: " + dashboardName, () -> {
            RestAssured.baseURI = BASE_URL;

            Response response = RestAssured
                    .given()
                    .header("Authorization", TOKEN)
                    .contentType(ContentType.JSON)
                    .body("{\"name\": \"" + dashboardName + "\"}")
                    .when()
                    .post("/" + PROJECT + "/dashboard")
                    .then()
                    .extract().response();

            int statusCode = response.getStatusCode();
            assertEquals(201, statusCode, "Dashboard не был создан");

            String id = response.jsonPath().getString("id");
            System.out.println("Dashboard создан. Имя: \"" + dashboardName + "\", ID: " + id);
            return id;
        });

        step("GET: Проверка наличия созданного Dashboard в списке", () -> {
            Response getResponse = RestAssured
                    .given()
                    .header("Authorization", TOKEN)
                    .queryParam("page.page", 1)
                    .queryParam("page.size", 100)
                    .get("/" + PROJECT + "/dashboard")
                    .then()
                    .extract().response();

            List<String> names = getResponse.jsonPath().getList("content.name");
            boolean found = names.contains(dashboardName);
            assertTrue(found, "Dashboard с именем \"" + dashboardName + "\" не найден в списке");

            System.out.println("Dashboard \"" + dashboardName + "\" найден в списке.");
        });
    }
}