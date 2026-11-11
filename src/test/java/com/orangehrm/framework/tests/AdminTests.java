package com.orangehrm.framework.tests;

import com.orangehrm.framework.driver.DriverManager;
import com.orangehrm.framework.pages.AdminPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

@Feature("Admin - User Management")
public class AdminTests extends BaseTest {

    @Test(description = "TC09 - Verify Admin > User Management > Users > Add form")
    @Severity(SeverityLevel.NORMAL)
    @Story("Add User form fields")
    public void addUserFormShouldContainRequiredFields() {
        loginAsAdmin();
        AdminPage adminPage = new AdminPage(DriverManager.getDriver());
        adminPage.openUsers();
        adminPage.openAddUserForm();

        Assert.assertTrue(adminPage.isAddUserHeaderDisplayed(), "The Add User form heading should be displayed.");
        Assert.assertTrue(adminPage.isUserFieldDisplayed("User Role"), "User Role field should be displayed.");
        Assert.assertTrue(adminPage.isUserFieldDisplayed("Employee Name"), "Employee Name field should be displayed.");
        Assert.assertTrue(adminPage.isUserFieldDisplayed("Username"), "Username field should be displayed.");
        Assert.assertTrue(adminPage.isUserFieldDisplayed("Password"), "Password field should be displayed.");
    }
}
