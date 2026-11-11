package com.orangehrm.framework.tests;

import com.orangehrm.framework.data.TestDataProviders;
import com.orangehrm.framework.driver.DriverManager;
import com.orangehrm.framework.pages.DashboardPage;
import com.orangehrm.framework.pages.LoginPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Authentication")
public class LoginTests extends BaseTest {

    @Test(dataProvider = "validLogin", dataProviderClass = TestDataProviders.class,
            description = "TC01 - Login with valid credentials")
    @Severity(SeverityLevel.BLOCKER)
    @Story("Successful login")
    public void validLoginShouldOpenDashboard(String username, String password) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        loginPage.login(username, password);
        loginPage.waitForDashboard();

        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("/dashboard/index"),
                "Successful login should redirect to the dashboard URL.");
        Assert.assertTrue(new DashboardPage(DriverManager.getDriver()).isDashboardHeaderDisplayed(),
                "Dashboard heading should be visible after successful login.");
    }

    @Test(dataProvider = "invalidLogin", dataProviderClass = TestDataProviders.class,
            description = "TC02 - Login with invalid credentials")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Rejected login")
    public void invalidLoginShouldShowError(String username, String password) {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        loginPage.login(username, password);

        Assert.assertEquals(loginPage.getInvalidCredentialsMessage(), "Invalid credentials",
                "The application should reject invalid credentials with the expected message.");
    }

    @Test(description = "TC03 - Login with empty username and password")
    @Severity(SeverityLevel.NORMAL)
    @Story("Required field validation")
    public void emptyLoginFieldsShouldShowTwoRequiredMessages() {
        LoginPage loginPage = new LoginPage(DriverManager.getDriver());
        loginPage.open();
        loginPage.login("", "");

        Assert.assertEquals(loginPage.getRequiredMessageCount(), 2,
                "Both username and password fields should display a Required validation message.");
    }
}
