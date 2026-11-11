package com.orangehrm.framework.tests;

import com.orangehrm.framework.data.JsonDataReader;
import com.orangehrm.framework.driver.DriverFactory;
import com.orangehrm.framework.driver.DriverManager;
import com.orangehrm.framework.pages.LoginPage;
import com.orangehrm.framework.utils.LoggerUtil;
import io.qameta.allure.Allure;
import io.qameta.allure.AttachmentOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;

public abstract class BaseTest {
    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.createDriver();
        DriverManager.setDriver(driver);
        LoggerUtil.info("Started browser session on thread " + Thread.currentThread().getName());
    }

    protected void loginAsAdmin() {
        String username = JsonDataReader.get("login", "valid", "username");
        String password = JsonDataReader.get("login", "valid", "password");
        Allure.step("Authenticate with the configured demo administrator account", () -> {
            LoginPage loginPage = new LoginPage(DriverManager.getDriver());
            loginPage.open();
            loginPage.login(username, password);
            loginPage.waitForDashboard();
        });
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && result.getStatus() == ITestResult.FAILURE) {
                LoggerUtil.error("Test failed: " + result.getName(), result.getThrowable());
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.attachment("Failure screenshot - " + result.getName(), "image/png",
                        new ByteArrayInputStream(screenshot), AttachmentOptions.withFileExtension(".png"));
            }
        } catch (Exception attachmentError) {
            LoggerUtil.error("Could not attach failure screenshot", attachmentError);
        } finally {
            DriverManager.quitDriver();
            driver = null;
        }
    }
}
