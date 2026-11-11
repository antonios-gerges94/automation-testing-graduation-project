package com.orangehrm.framework.pages;

import io.qameta.allure.Allure;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class EmployeeListPage extends BasePage {
    private static final By EMPLOYEE_NAME = By.xpath("//label[normalize-space()='Employee Name']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private static final By SEARCH_BUTTON = By.xpath("//button[normalize-space()='Search']");
    private static final By AUTOCOMPLETE_OPTIONS = By.cssSelector("div.oxd-autocomplete-option");
    private static final By TABLE_BODY = By.cssSelector(".oxd-table-body");
    private static final By TABLE_ROWS = By.cssSelector(".oxd-table-body > div");
    private static final By TABLE_CELLS = By.cssSelector(".oxd-table-cell");
    private static final By NO_RECORDS = By.xpath("//*[normalize-space()='No Records Found']");

    public EmployeeListPage(WebDriver driver) { super(driver); }

    public void searchByName(String employeeName) {
        Allure.step("Search Employee List for: " + employeeName, () -> {
            type(EMPLOYEE_NAME, employeeName);
            selectAutocompleteSuggestion(employeeName);
            click(SEARCH_BUTTON);
            String expectedName = normalizeText(employeeName).toLowerCase();
            wait.until(d -> {
                boolean noRecords = d.findElements(NO_RECORDS).stream().anyMatch(WebElement::isDisplayed);
                if (noRecords) {
                    return true;
                }

                // Selenium WebElement.getText() separates table cells with line breaks.
                // Normalize all whitespace before checking a full name such as "John Smith".
                return d.findElements(TABLE_BODY).stream()
                        .filter(WebElement::isDisplayed)
                        .map(WebElement::getText)
                        .map(EmployeeListPage::normalizeText)
                        .map(String::toLowerCase)
                        .anyMatch(results -> results.contains(expectedName));
            });
        });
    }

    private void selectAutocompleteSuggestion(String expectedName) {
        try {
            List<WebElement> suggestions = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(AUTOCOMPLETE_OPTIONS));
            for (WebElement suggestion : suggestions) {
                String candidate = normalizeText(suggestion.getText());
                String expected = normalizeText(expectedName);
                if (!candidate.isBlank() && (candidate.equalsIgnoreCase(expected)
                        || candidate.toLowerCase().contains(expected.toLowerCase()))) {
                    suggestion.click();
                    return;
                }
            }
            // Do not select an unrelated first suggestion. For an unknown employee,
            // keep the entered text and let the search return "No Records Found".
        } catch (TimeoutException ignored) {
            // A non-existing employee should not produce an autocomplete option.
        }
    }


    /**
     * Returns a real employee from the current demo database. The hosted demo can
     * refresh its seed data, so tests should not assume a permanent employee name.
     */
    public String getFirstEmployeeFullName() {
        click(SEARCH_BUTTON);
        wait.until(d -> !d.findElements(NO_RECORDS).isEmpty()
                || d.findElements(TABLE_ROWS).stream().anyMatch(WebElement::isDisplayed));

        List<WebElement> rows = driver.findElements(TABLE_ROWS);
        if (rows.isEmpty()) {
            throw new AssertionError("Employee List contains no employees; cannot run the positive search test.");
        }

        List<WebElement> cells = rows.get(0).findElements(TABLE_CELLS);
        // OrangeHRM PIM table: checkbox, employee ID, first/middle name, last name, ...
        if (cells.size() < 4) {
            throw new AssertionError("Unexpected Employee List table structure: expected at least 4 cells.");
        }
        String firstName = cells.get(2).getText().trim();
        String lastName = cells.get(3).getText().trim();
        String fullName = (firstName + " " + lastName).trim();
        if (fullName.isBlank()) {
            throw new AssertionError("The first Employee List row does not contain a readable employee name.");
        }
        return fullName;
    }

    public String getResultsText() {
        List<WebElement> tableBodies = driver.findElements(TABLE_BODY);
        return tableBodies.stream()
                          .filter(WebElement::isDisplayed)
                          .findFirst()
                          .map(WebElement::getText)
                          .map(EmployeeListPage::normalizeText)
                          .orElse("");
    }

    private static String normalizeText(String value) {
        return value == null ? "" : value.replaceAll("\\s+", " ").trim();
    }

    public boolean isNoRecordsFoundDisplayed() {
        return isDisplayed(NO_RECORDS);
    }
}
