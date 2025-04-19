package com.sergei.tests.ui_test;

import com.sergei.pages.LoginPage;
import com.sergei.pages.WidgetDialogPage;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import static com.sergei.pages.DashboardPage.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Epic("Dashboard")
@Feature("Widget Management")
@Story("Add Task Progress Widget")
@Owner("Sergei")
public class AddTaskProgressWidgetUITest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("UI: Добавление виджета в Dashboard")
    @Description("Создаёт Dashboard, добавляет фильтр и виджет, затем проверяет его наличие")
    public void addTaskProgressWidgetTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito", "--disable-save-password-bubble");
        options.setExperimentalOption("prefs", new java.util.HashMap<String, Object>() {{
            put("credentials_enable_service", false);
            put("profile.password_manager_enabled", false);
        }});

        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            login(driver);

            WidgetDialogPage widgetDialogPage = new WidgetDialogPage(driver);
            createDashboard(driver, wait, widgetDialogPage);
            addWidget(driver, wait, widgetDialogPage);
            createFilter(wait, widgetDialogPage);
            finishAddingWidget(wait, widgetDialogPage);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Ошибка при выполнении теста: " + e.getMessage());
        } finally {
            driver.quit();
        }
    }

    @Step("Логин в Report Portal")
    private void login(WebDriver driver) {
        driver.get("https://demo.reportportal.io/");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs("default", "1q2w3e");
    }

    @Step("Создание нового Dashboard")
    private void createDashboard(WebDriver driver, WebDriverWait wait, WidgetDialogPage widgetDialogPage) {
        driver.get("https://demo.reportportal.io/ui/#default_personal/dashboard");
        WebElement addDashboardBtn = wait.until(ExpectedConditions.elementToBeClickable(ADD_NEW_DASHBOARD_BUTTON));
        widgetDialogPage.scrollAndClick(addDashboardBtn);

        String dashboardName = widgetDialogPage.generateFilterName();
        WebElement dashboardNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(DASHBOARD_NAME_INPUT));
        widgetDialogPage.realInput(dashboardNameInput, dashboardName);

        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(DASHBOARD_ADD_BUTTON));
        widgetDialogPage.scrollAndClick(addButton);
    }

    @Step("Добавление виджета")
    private void addWidget(WebDriver driver, WebDriverWait wait, WidgetDialogPage widgetDialogPage) {
        WebElement addWidgetButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_NEW_WIDGET_BUTTON));
        widgetDialogPage.scrollAndClick(addWidgetButton);

        WebElement widgetRadio = wait.until(ExpectedConditions.elementToBeClickable(LAUNCH_EXECUTION_WIDGET_TOGGLER));
        widgetDialogPage.scrollAndClick(widgetRadio);

        WebElement nextStepButton = wait.until(ExpectedConditions.elementToBeClickable(NEXT_STEP_BUTTON));
        widgetDialogPage.scrollAndClick(nextStepButton);
    }

    @Step("Создание фильтра")
    private void createFilter(WebDriverWait wait, WidgetDialogPage widgetDialogPage) {
        String filterName = widgetDialogPage.generateFilterName();

        WebElement addFilterBtn = wait.until(ExpectedConditions.elementToBeClickable(ADD_FILTER_BUTTON));
        widgetDialogPage.scrollAndClick(addFilterBtn);

        WebElement filterNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(FILTER_NAME_INPUT));
        widgetDialogPage.realInput(filterNameInput, filterName);

        WebElement launchNameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(FILTER_NAME_REPEAT_INPUT));
        widgetDialogPage.realInput(launchNameInput, filterName);

        WebElement submitBtn = wait.until(ExpectedConditions.elementToBeClickable(SUBMIT_FILTER_BUTTON));
        submitBtn.click();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(SUBMIT_FILTER_BUTTON));
    }

    @Step("Завершение добавления виджета и проверка")
    private void finishAddingWidget(WebDriverWait wait, WidgetDialogPage widgetDialogPage) {
        WebElement secondNextStepBtn = wait.until(ExpectedConditions.elementToBeClickable(SECOND_NEXT_STEP_BUTTON));
        widgetDialogPage.scrollAndClick(secondNextStepBtn);

        String expectedWidgetName = widgetDialogPage.getNewWidgetNameFromInput();
        widgetDialogPage.clickAddButton();

        assertTrue(widgetDialogPage.isWidgetWithNameDisplayed(expectedWidgetName),
                "Виджет с именем '" + expectedWidgetName + "' не найден!");
    }
}