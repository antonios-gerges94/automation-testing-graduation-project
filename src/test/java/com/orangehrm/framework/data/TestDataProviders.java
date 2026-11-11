package com.orangehrm.framework.data;

import org.testng.annotations.DataProvider;

/** TestNG DataProviders backed by testdata.json. */
public final class TestDataProviders {
    private TestDataProviders() { }

    @DataProvider(name = "validLogin")
    public static Object[][] validLogin() {
        return new Object[][] {{
                JsonDataReader.get("login", "valid", "username"),
                JsonDataReader.get("login", "valid", "password")
        }};
    }

    @DataProvider(name = "invalidLogin")
    public static Object[][] invalidLogin() {
        return new Object[][] {{
                JsonDataReader.get("login", "invalid", "username"),
                JsonDataReader.get("login", "invalid", "password")
        }};
    }

    @DataProvider(name = "knownEmployee")
    public static Object[][] knownEmployee() {
        return new Object[][] {{JsonDataReader.get("employees", "known", "searchTerm")}};
    }

    @DataProvider(name = "missingEmployee")
    public static Object[][] missingEmployee() {
        return new Object[][] {{JsonDataReader.get("employees", "missing", "searchTerm")}};
    }

    @DataProvider(name = "newEmployee")
    public static Object[][] newEmployee() {
        return new Object[][] {{
                JsonDataReader.get("employees", "new", "firstName"),
                JsonDataReader.get("employees", "new", "lastName")
        }};
    }

    @DataProvider(name = "employeeRequiredValidation")
    public static Object[][] employeeRequiredValidation() {
        return new Object[][] {{
                JsonDataReader.get("employees", "requiredFieldValidation", "firstName"),
                JsonDataReader.get("employees", "requiredFieldValidation", "lastName")
        }};
    }
}
