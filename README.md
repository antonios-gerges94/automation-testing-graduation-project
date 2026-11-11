# OrangeHRM Automation Testing Graduation Project

A Java 17 + Maven automation framework for the OrangeHRM Open Source Demo using Selenium WebDriver, TestNG, Page Object Model (POM), JSON-driven test data, parallel execution, Log4j2, retry handling, and Allure reporting.

**Application under test:** https://opensource-demo.orangehrmlive.com/web/index.php/auth/login

## Coverage

| Test case | Scenario | Test class |
|---|---|---|
| TC01 | Login with valid credentials and verify Dashboard URL/header | `LoginTests` |
| TC02 | Login with invalid credentials and verify error | `LoginTests` |
| TC03 | Submit empty login fields and verify both Required messages | `LoginTests` |
| TC04 | Search for an existing employee in PIM | `PimTests` |
| TC05 | Search for a non-existing employee | `PimTests` |
| TC06 | Open Add Employee and verify URL and name fields | `PimTests` |
| TC07 | Verify First Name required validation | `PimTests` |
| TC08 | Create a new employee, open Personal Details, and find the employee in Employee List | `PimTests` |
| TC09 | Open Admin > User Management > Users > Add and verify form fields | `AdminTests` |
| TC10 | Verify OrangeHRM footer branding link opens orangehrm.com in a new tab | `UiTests` |
| TC11 | Verify the required sidebar items | `UiTests` |

## Framework design

- **POM:** page locators and browser actions live in page classes; test classes contain scenarios and assertions.
- **Thread safety:** `DriverManager` stores the WebDriver in `ThreadLocal<WebDriver>` and removes it after every test.
- **Parallel execution:** `testng.xml` uses `parallel="tests"` and `thread-count="3"`. Each TestNG `<test>` entry maps to a different test class.
- **Data-driven tests:** `TestDataProviders` reads credentials and employee data from `src/test/resources/testdata/testdata.json` using Jackson.
- **Configuration:** `ConfigReader` loads `src/test/resources/config.properties` using Java `Properties`. JVM `-D` values override the file.
- **Wait strategy:** explicit waits are used; no `Thread.sleep()` or implicit wait is used.
- **Reporting:** Allure TestNG integration captures tests, descriptions, labels, steps, and failure screenshots.
- **Logging:** Log4j2 writes INFO and ERROR messages to the console and `logs/automation.log`.
- **Retry:** `RetryTransformer` applies `RetryAnalyzer` to each test; each failed test is retried up to two times.
- **Driver management:** Selenium Manager resolves the compatible browser driver; no manually downloaded driver executable is required.
- **Surefire compatibility:** Maven Surefire is pinned to 3.5.6 because this project executes the TestNG `testng.xml` suite through `suiteXmlFiles`; Surefire 3.6.x removed that configuration.

## Prerequisites

1. JDK 17 or newer.
2. Apache Maven 3.9.6 or newer.
3. Google Chrome installed (default), or set `browser` to `edge` or `firefox`.
4. Allure Commandline installed and available on `PATH` to generate/open the HTML report.

Check local versions:

```bash
java -version
mvn -version
allure --version
```

## Run the tests

From the project root:

```bash
mvn clean test
```

The suite launches up to three TestNG `<test>` groups in parallel. Each test method creates and closes its own browser session.

### Override configuration without editing files

```bash
mvn clean test -Dbrowser=edge -Dheadless=true -Dexplicit.wait=15
```

Supported browser values: `chrome`, `edge`, `firefox`.

### Test data

Edit `src/test/resources/testdata/testdata.json` to change demo credentials, the existing employee used by TC04, the missing employee search term, or the new employee seed values. The public demo's employee data can change; if TC04 fails because the seeded employee is no longer present, replace `employees.known` with a name currently available in the demo.

The new employee test appends a timestamp to the JSON first-name seed so repeated runs do not collide with an existing employee.

## Allure report

Run tests first, then generate the static HTML report:

```bash
mvn test
allure generate allure-results --clean -o allure-report
allure open allure-report
```

Commit the generated **`allure-report/`** folder when submitting. Do **not** commit `allure-results/`; it contains raw execution data and is excluded in `.gitignore`.

> The repository template does not include a generated report because no live test run has been executed from this project package. Generate the report after running the suite on your machine, then add `allure-report/` to Git.

## Project structure

```text
automation-testing-graduation-project/
├── pom.xml
├── README.md
├── .gitignore
├── logs/
└── src/
    └── test/
        ├── java/com/orangehrm/framework/
        │   ├── config/       # Properties reader
        │   ├── data/         # Jackson reader and TestNG DataProviders
        │   ├── driver/       # DriverFactory and ThreadLocal DriverManager
        │   ├── listeners/    # RetryAnalyzer and annotation transformer
        │   ├── pages/        # Page Object classes
        │   ├── tests/        # Login, PIM, Admin and UI test classes
        │   └── utils/        # Log4j2 helper
        └── resources/
            ├── allure.properties
            ├── config.properties
            ├── log4j2.xml
            ├── testng.xml
            └── testdata/testdata.json
```

## GitHub submission

Create a **public** repository named `automation-testing-graduation-project`, then run these commands from this project folder (replace the URL with your own GitHub repository URL):

```bash
git init
git add .
git commit -m "Add OrangeHRM Selenium TestNG automation framework"
git branch -M main
git remote add origin https://github.com/<YOUR_GITHUB_USERNAME>/automation-testing-graduation-project.git
git push -u origin main
```

After generating the report, add and push it:

```bash
git add allure-report
git commit -m "Add generated Allure HTML report"
git push
```

Before submission, confirm that `allure-results/` is not staged and that the repository visibility is **Public**.


### Dynamic known-employee search

The hosted OrangeHRM demo may refresh its employee seed data. In `src/test/resources/testdata/testdata.json`, leave `employees.known.searchTerm` empty to make TC04 select the first employee currently shown in Employee List, then search for that exact employee. A non-empty value may be supplied to force a particular known employee name.

### Retry policy

The retry analyzer retries transient WebDriver exceptions only. It does not retry assertion failures, and it does not retry TC08 because employee creation changes shared demo data and retries can create duplicate records.
