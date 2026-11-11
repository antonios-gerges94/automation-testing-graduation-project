package com.orangehrm.framework.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AdminPage extends BasePage {
    private static final By ADD_BUTTON = By.xpath("//button[normalize-space()='Add']");
    private static final By ADD_USER_HEADER = By.xpath("//h6[normalize-space()='Add User']");

    public AdminPage(WebDriver driver) { super(driver); }

    public void openUsers() {
        Allure.step("Navigate to Admin > User Management > Users", () -> {
            new DashboardPage(driver).openMenu("Admin");
            waitForUrl("/admin/viewSystemUsers");
        });
    }

    public void openAddUserForm() {
        Allure.step("Open Add User form", () -> {
            click(ADD_BUTTON);
            wait.until(org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated(ADD_USER_HEADER));
        });
    }

    public boolean isAddUserHeaderDisplayed() {
        return isDisplayed(ADD_USER_HEADER);
    }

    public boolean isUserFieldDisplayed(String labelText) {
        By field = By.xpath("//label[normalize-space()='" + labelText + "']/ancestor::div[contains(@class,'oxd-input-group')]");
        return isDisplayed(field);
    }
}
