package com.sergei.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class DashboardPage {
    private WebDriver driver;
    private WebDriverWait wait;

    public DashboardPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public static final By ADD_NEW_WIDGET_BUTTON = By.xpath(
            "//span[contains(@class, 'ghostButton__text') and text()='Add new widget']/.."
    );

    public static final By LAUNCH_EXECUTION_WIDGET_TOGGLER = By.cssSelector(
            "span.inputRadio__toggler--ygpdQ.inputRadio__at-top--NPpmC.inputRadio__mode-default--VD2jF.inputRadio__toggler-medium--iSkOd"
    );

    public static final By NEXT_STEP_BUTTON = By.xpath(
            "//span[contains(@class, 'ghostButton__text') and normalize-space(text())='Next step']");

    public static final By RADIO_FILTER = By.cssSelector(
            "label.inputRadio__input-radio--EMMTx.inputRadio__mode-default--VD2jF");

    public static final By SECOND_NEXT_STEP_BUTTON = By.xpath(
            "//button[.//span[text()='Next step'] and contains(@class, 'ghostButton__ghost-button')]");

    public static final By ADD_WIDGET_BUTTON = By.xpath(
            "//button[contains(@class, 'bigButton__big-button') and contains(@class, 'bigButton__color-booger') and text()='Add']");

    public static final By ADD_FILTER_BUTTON = By.xpath(
            "//span[@class='ghostButton__text--SjHtK' and text()='Add filter']"
    );

    public static final By FILTER_NAME_INPUT = By.xpath(
            "//input[@placeholder='Input filter name']"
    );

    public static final By FILTER_NAME_REPEAT_INPUT = By.xpath(
            "//input[@placeholder='Enter name']"
    );

    public static final By WIDGET_NAME_INPUT = By.cssSelector(
            "input.input__input--iYEmM[type='text']"
    );

    public static final By SUBMIT_FILTER_BUTTON = By.xpath(
            "//button[contains(@class, 'addEditFilter__button-inline') and normalize-space(text())='Submit']"
    );

    public static final By ADD_NEW_DASHBOARD_BUTTON = By.xpath(
            "//span[contains(@class, 'ghostButton__text') and text()='Add New Dashboard']/.."
    );

    public static final By DASHBOARD_NAME_INPUT = By.xpath(
            "//input[@placeholder='Enter dashboard name']"
    );

    public static final By DASHBOARD_ADD_BUTTON = By.xpath(
            "//button[contains(@class, 'bigButton__big-button') and text()='Add']"
    );
}
