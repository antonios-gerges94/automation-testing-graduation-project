package com.orangehrm.framework.pages;

import com.orangehrm.framework.config.ConfigReader;
import com.orangehrm.framework.utils.LoggerUtil;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public abstract class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(ConfigReader.getInt("explicit.wait")));
    }

    protected WebElement visible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement clickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void click(By locator) {
        Allure.step("Click UI element: " + locator, () -> clickable(locator).click());
        LoggerUtil.info("Clicked element: " + locator);
    }

    protected void type(By locator, String value) {
        Allure.step("Enter value in UI field: " + locator, () -> {
            WebElement element = visible(locator);
            element.clear();
            if (value != null && !value.isEmpty()) {
                element.sendKeys(value);
            }
        });
        LoggerUtil.info("Entered text in field: " + locator);
    }

    protected boolean isDisplayed(By locator) {
        try {
            return visible(locator).isDisplayed();
        } catch (RuntimeException exception) {
            return false;
        }
    }

    protected String text(By locator) {
        return visible(locator).getText().trim();
    }

    protected void waitForUrl(String fragment) {
        Allure.step("Wait until URL contains: " + fragment,
                () -> wait.until(ExpectedConditions.urlContains(fragment)));
    }
}
