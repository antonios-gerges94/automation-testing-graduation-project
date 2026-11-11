package com.orangehrm.framework.tests;

import com.orangehrm.framework.data.TestDataProviders;
import com.orangehrm.framework.driver.DriverManager;
import com.orangehrm.framework.pages.AddEmployeePage;
import com.orangehrm.framework.pages.EmployeeListPage;
import com.orangehrm.framework.pages.PimPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Feature("PIM - Employee Management")
public class PimTests extends BaseTest {

    @Test(dataProvider = "knownEmployee", dataProviderClass = TestDataProviders.class,
            description = "TC04 - Search for an existing employee")
    @Severity(SeverityLevel.NORMAL)
    @Story("Employee list search")
    public void searchShouldReturnKnownEmployee(String configuredSearchTerm) {
        loginAsAdmin();
        new PimPage(DriverManager.getDriver()).openEmployeeList();
        EmployeeListPage employeeList = new EmployeeListPage(DriverManager.getDriver());

        // The public demo data can be refreshed. When JSON has no fixed searchTerm,
        // choose a real employee currently present in Employee List, then search for it.
        String searchTerm = configuredSearchTerm == null || configuredSearchTerm.isBlank()
                ? employeeList.getFirstEmployeeFullName()
                : configuredSearchTerm;
        employeeList.searchByName(searchTerm);

        Assert.assertTrue(employeeList.getResultsText().toLowerCase().contains(searchTerm.toLowerCase()),
                "Employee results should contain the searched employee: " + searchTerm);
    }

    @Test(dataProvider = "missingEmployee", dataProviderClass = TestDataProviders.class,
            description = "TC05 - Search for a non-existing employee")
    @Severity(SeverityLevel.NORMAL)
    @Story("Empty employee search result")
    public void searchShouldShowNoRecordsForUnknownEmployee(String searchTerm) {
        loginAsAdmin();
        new PimPage(DriverManager.getDriver()).openEmployeeList();
        EmployeeListPage employeeList = new EmployeeListPage(DriverManager.getDriver());
        employeeList.searchByName(searchTerm);

        Assert.assertTrue(employeeList.isNoRecordsFoundDisplayed(),
                "The Employee List should display 'No Records Found' for a non-existing employee.");
    }

    @Test(description = "TC06 - Open the Add Employee page")
    @Severity(SeverityLevel.NORMAL)
    @Story("Navigate to Add Employee")
    public void addEmployeePageShouldBeDisplayed() {
        loginAsAdmin();
        new PimPage(DriverManager.getDriver()).openAddEmployee();
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("/pim/addEmployee"),
                "The URL should identify the Add Employee page.");
        Assert.assertTrue(new AddEmployeePage(DriverManager.getDriver()).isFirstNameFieldDisplayed(),
                "The First Name field should be visible.");
        Assert.assertTrue(new AddEmployeePage(DriverManager.getDriver()).isLastNameFieldDisplayed(),
                "The Last Name field should be visible.");
    }

    @Test(dataProvider = "employeeRequiredValidation", dataProviderClass = TestDataProviders.class,
            description = "TC07 - Validate required First Name when adding an employee")
    @Severity(SeverityLevel.NORMAL)
    @Story("Add Employee form validation")
    public void firstNameShouldBeRequired(String firstName, String lastName) {
        Assert.assertTrue(firstName.isBlank(), "TC07 test data must keep First Name empty.");
        loginAsAdmin();
        new PimPage(DriverManager.getDriver()).openAddEmployee();
        AddEmployeePage addEmployeePage = new AddEmployeePage(DriverManager.getDriver());
        addEmployeePage.saveWithEmptyFirstName(lastName);

        Assert.assertTrue(addEmployeePage.isFirstNameRequiredDisplayed(),
                "A Required validation message should appear under First Name.");
    }

    @Test(dataProvider = "newEmployee", dataProviderClass = TestDataProviders.class,
            description = "TC08 - End-to-end: create an employee and verify it in Employee List")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create and search employee end-to-end")
    public void createEmployeeAndFindItInEmployeeList(String firstNameSeed, String lastName) {
        loginAsAdmin();
        String uniqueSuffix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        String firstName = firstNameSeed + uniqueSuffix;
        String fullName = firstName + " " + lastName;

        new PimPage(DriverManager.getDriver()).openAddEmployee();
        AddEmployeePage addEmployeePage = new AddEmployeePage(DriverManager.getDriver());
        addEmployeePage.addEmployee(firstName, lastName);

        Assert.assertTrue(addEmployeePage.isPersonalDetailsDisplayed(),
                "The Personal Details heading should be displayed after saving.");
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("/pim/viewPersonalDetails"),
                "Saving a new employee should open the Personal Details page.");
        new PimPage(DriverManager.getDriver()).openEmployeeList();
        EmployeeListPage employeeList = new EmployeeListPage(DriverManager.getDriver());
        employeeList.searchByName(fullName);
        Assert.assertTrue(employeeList.getResultsText().contains(fullName),
                "The newly created employee should appear in Employee List search results.");
    }
}
