package com.orangehrm.framework.pages;

import com.orangehrm.framework.config.ConfigReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class LoginPage extends BasePage {
    private static final By USERNAME = By.cssSelector("input[name='username']");
    private static final By PASSWORD = By.cssSelector("input[name='password']");
    private static final By LOGIN_BUTTON = By.cssSelector("button[type='submit']");
    private static final By INVALID_CREDENTIALS = By.cssSelector(".oxd-alert-content-text");
    private static final By REQUIRED_MESSAGES = By.xpath("//span[contains(@class,'oxd-input-field-error-message') and normalize-space()='Required']");

    public LoginPage(WebDriver driver) { super(driver); }

    public void open() {
        Allure.step("Navigate to OrangeHRM login page", () -> driver.get(ConfigReader.baseUrl() + "auth/login"));
    }

    public void login(String username, String password) {
        type(USERNAME, username);
        type(PASSWORD, password);
        click(LOGIN_BUTTON);
    }

    public void waitForDashboard() {
        waitForUrl("/dashboard/index");
    }

    public String getInvalidCredentialsMessage() {
        return text(INVALID_CREDENTIALS);
    }

    public int getRequiredMessageCount() {
        wait.until(d -> d.findElements(REQUIRED_MESSAGES).size() >= 2);
        List<WebElement> messages = driver.findElements(REQUIRED_MESSAGES);
        return messages.size();
    }
}
