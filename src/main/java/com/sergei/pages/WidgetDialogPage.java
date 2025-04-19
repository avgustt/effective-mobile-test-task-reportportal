package com.sergei.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static com.sergei.pages.DashboardPage.*;

public class WidgetDialogPage {
    private WebDriver driver;
    private WebDriverWait wait;
    private Actions actions;
    private JavascriptExecutor js;

    public WidgetDialogPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.actions = new Actions(driver);
        this.js = (JavascriptExecutor) driver;
    }

    public void clickAddButton() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(ADD_WIDGET_BUTTON));
        scrollAndClick(addButton);
    }

    public String getNewWidgetNameFromInput() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(WIDGET_NAME_INPUT));
        return input.getAttribute("value");
    }

    public static By getWidgetByName(String name) {
        return By.xpath("//div[contains(@class, 'widgetHeader__widget-name-block') and text()='" + name + "']");
    }

    public boolean isWidgetWithNameDisplayed(String widgetName) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(getWidgetByName(widgetName))).isDisplayed();
    }

    public void scrollAndClick(WebElement element) {
        int attempts = 0;
        while (attempts < 3) {
            try {
                js.executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
                Thread.sleep(300);
                js.executeScript("arguments[0].click();", element);
                return;
            } catch (Exception e) {
                System.out.println("Попытка " + (attempts + 1) + " не удалась. Повтор...");
                attempts++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }
        throw new RuntimeException("Не удалось кликнуть по элементу после 3 попыток");
    }

    public String generateApiKey() {
        return "AutoKey_" + System.currentTimeMillis();
    }

    public String generateFilterName() {
        return "Auto_" + System.currentTimeMillis();
    }

    public void realInput(WebElement element, String text) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
        js.executeScript("arguments[0].scrollIntoView(true);", element);

        try {
            Thread.sleep(500);
        } catch (InterruptedException ignored) {}

        element.click();
        element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        element.sendKeys(Keys.BACK_SPACE);

        try {
            Thread.sleep(300);
        } catch (InterruptedException ignored) {}

        for (char ch : text.toCharArray()) {
            element.sendKeys(String.valueOf(ch));
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}
        }

        new WebDriverWait(driver, Duration.ofSeconds(3))
                .until(ExpectedConditions.attributeToBe(element, "value", text));

        String actual = element.getAttribute("value");
        if (!text.equals(actual)) {
            throw new RuntimeException("Текст в поле не установился корректно: ожидалось '" + text + "', но получили '" + actual + "'");
        }
    }
}