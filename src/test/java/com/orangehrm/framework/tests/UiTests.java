package com.orangehrm.framework.tests;

import com.orangehrm.framework.driver.DriverManager;
import com.orangehrm.framework.pages.DashboardPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Set;

@Feature("UI and Branding")
public class UiTests extends BaseTest {

    @Test(description = "TC10 - Verify OrangeHRM footer branding link")
    @Severity(SeverityLevel.MINOR)
    @Story("Footer external branding link")
    public void footerLinkShouldOpenOrangeHrmWebsiteInNewTab() {
        loginAsAdmin();
        ((JavascriptExecutor) DriverManager.getDriver()).executeScript("window.scrollTo(0, document.body.scrollHeight);");
        By footerLinkLocator = By.xpath("//a[contains(normalize-space(.), 'OrangeHRM, Inc')]");
        WebElement footerLink = new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getDriver(),
                java.time.Duration.ofSeconds(10)).until(
                org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable(footerLinkLocator));
        Assert.assertTrue(footerLink.getText().contains("OrangeHRM, Inc"),
                "Footer branding should contain 'OrangeHRM, Inc'.");

        String originalWindow = DriverManager.getDriver().getWindowHandle();
        Set<String> existingWindows = DriverManager.getDriver().getWindowHandles();
        footerLink.click();
        new org.openqa.selenium.support.ui.WebDriverWait(DriverManager.getDriver(), java.time.Duration.ofSeconds(10))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe(existingWindows.size() + 1));

        for (String window : DriverManager.getDriver().getWindowHandles()) {
            if (!window.equals(originalWindow)) {
                DriverManager.getDriver().switchTo().window(window);
                break;
            }
        }
        Assert.assertTrue(DriverManager.getDriver().getCurrentUrl().contains("orangehrm.com"),
                "The footer branding link should open a page on orangehrm.com in a new tab.");
    }

    @Test(description = "TC11 - Verify the sidebar menu items")
    @Severity(SeverityLevel.NORMAL)
    @Story("Sidebar navigation menu")
    public void sidebarShouldContainAllRequiredMenuItems() {
        loginAsAdmin();
        Set<String> actualMenuItems = new DashboardPage(DriverManager.getDriver()).getSidebarMenuNames();
        String[] expectedMenuItems = {
                "Admin", "PIM", "Leave", "Time", "Recruitment", "My Info",
                "Performance", "Dashboard", "Directory"
        };

        for (String expectedItem : expectedMenuItems) {
            Assert.assertTrue(actualMenuItems.contains(expectedItem),
                    "Sidebar should contain the '" + expectedItem + "' menu item. Actual items: " + actualMenuItems);
        }
    }
}
