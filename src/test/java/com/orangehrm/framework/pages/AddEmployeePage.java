package com.orangehrm.framework.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AddEmployeePage extends BasePage {
    private static final By FIRST_NAME = By.cssSelector("input[placeholder='First Name']");
    private static final By LAST_NAME = By.cssSelector("input[placeholder='Last Name']");
    private static final By SAVE_BUTTON = By.xpath("//button[normalize-space()='Save']");
    private static final By PERSONAL_DETAILS_HEADER = By.xpath("//h6[normalize-space()='Personal Details']");
    private static final By FIRST_NAME_REQUIRED = By.xpath(
            "//div[contains(@class,'oxd-input-group')][.//input[@placeholder='First Name']]" +
            "//*[normalize-space()='Required']");

    public AddEmployeePage(WebDriver driver) { super(driver); }

    public void addEmployee(String firstName, String lastName) {
        Allure.step("Enter employee details and save", () -> {
            type(FIRST_NAME, firstName);
            type(LAST_NAME, lastName);
            click(SAVE_BUTTON);
        });
    }

    public void saveWithEmptyFirstName(String lastName) {
        Allure.step("Submit Add Employee with First Name empty", () -> {
            type(LAST_NAME, lastName);
            click(SAVE_BUTTON);
        });
    }

    public boolean isFirstNameFieldDisplayed() { return isDisplayed(FIRST_NAME); }

    public boolean isLastNameFieldDisplayed() { return isDisplayed(LAST_NAME); }

    public boolean isFirstNameRequiredDisplayed() {
        return isDisplayed(FIRST_NAME_REQUIRED);
    }

    public boolean isPersonalDetailsDisplayed() {
        return isDisplayed(PERSONAL_DETAILS_HEADER);
    }

}
