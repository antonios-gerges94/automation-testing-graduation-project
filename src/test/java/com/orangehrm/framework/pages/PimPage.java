package com.orangehrm.framework.pages;

import com.orangehrm.framework.config.ConfigReader;
import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/** Page object for PIM navigation in the current OrangeHRM Open Source UI. */
public class PimPage extends BasePage {
    private static final String PIM_MODULE_ROUTE = "/pim/viewPimModule";
    private static final String EMPLOYEE_LIST_ROUTE = "/pim/viewEmployeeList";
    private static final String ADD_EMPLOYEE_ROUTE = "/pim/addEmployee";

    private static final By EMPLOYEE_NAME_LABEL =
            By.xpath("//label[normalize-space()='Employee Name']");
    private static final By EMPLOYEE_TABLE = By.cssSelector(".oxd-table-body");
    private static final By ADD_BUTTON = By.xpath(
            "//button[normalize-space()='Add' or .//*[normalize-space()='Add']]" +
            " | //a[normalize-space()='Add' or .//*[normalize-space()='Add']]");
    private static final By FIRST_NAME_FIELD = By.cssSelector("input[placeholder='First Name']");

    public PimPage(WebDriver driver) {
        super(driver);
    }

    private void openPimModule() {
        new DashboardPage(driver).openMenu("PIM");
        wait.until(d -> d.getCurrentUrl().contains(PIM_MODULE_ROUTE)
                || d.getCurrentUrl().contains(EMPLOYEE_LIST_ROUTE)
                || d.getCurrentUrl().contains(ADD_EMPLOYEE_ROUTE));
    }

    /** Opens the Employee List page after entering the PIM module. */
    public void openEmployeeList() {
        Allure.step("Navigate to PIM > Employee List", () -> {
            openPimModule();

            // The current demo exposes Employee List as a PIM child route. Some UI builds
            // render it as a tab and others do not expose a matching href in the DOM.
            // Use the stable application route after opening PIM rather than waiting on
            // a brittle href-only selector that may not be present.
            driver.navigate().to(ConfigReader.baseUrl() + "pim/viewEmployeeList");
            waitForUrl(EMPLOYEE_LIST_ROUTE);
            wait.until(ExpectedConditions.or(
                    ExpectedConditions.visibilityOfElementLocated(EMPLOYEE_NAME_LABEL),
                    ExpectedConditions.visibilityOfElementLocated(EMPLOYEE_TABLE)));
        });
    }

    /** Opens Add Employee through the Employee List Add button when available. */
    public void openAddEmployee() {
        Allure.step("Navigate to PIM > Add Employee", () -> {
            openEmployeeList();

            List<WebElement> addControls = driver.findElements(ADD_BUTTON);
            boolean hasVisibleAddControl = addControls.stream()
                    .anyMatch(element -> element.isDisplayed() && element.isEnabled());

            if (hasVisibleAddControl) {
                click(ADD_BUTTON);
                // If this build uses the Add button, it should navigate to this route.
                // A direct route fallback is retained for demo builds where the action
                // is not rendered as a regular anchor/button.
                if (!driver.getCurrentUrl().contains(ADD_EMPLOYEE_ROUTE)) {
                    driver.navigate().to(ConfigReader.baseUrl() + "pim/addEmployee");
                }
            } else {
                driver.navigate().to(ConfigReader.baseUrl() + "pim/addEmployee");
            }

            waitForUrl(ADD_EMPLOYEE_ROUTE);
            visible(FIRST_NAME_FIELD);
        });
    }
}
