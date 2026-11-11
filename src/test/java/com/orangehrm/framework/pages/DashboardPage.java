
package com.orangehrm.framework.pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class DashboardPage extends BasePage {

    private static final By DASHBOARD_HEADER =
            By.xpath("//h6[normalize-space()='Dashboard']");

    private static final int EXPECTED_MENU_ITEMS = 9;

    private static final By SIDEBAR_MENU_NAMES =
            By.cssSelector(
                    "nav[aria-label='Sidepanel'] ul.oxd-main-menu a.oxd-main-menu-item .oxd-main-menu-item--name, " +
                            "ul.oxd-main-menu a.oxd-main-menu-item .oxd-main-menu-item--name"
            );

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardHeaderDisplayed() {
        return isDisplayed(DASHBOARD_HEADER);
    }

    private void waitForSidebar() {

        wait.until(d -> {

            List<WebElement> items =
                    d.findElements(SIDEBAR_MENU_NAMES);

            long visibleCount = items.stream()
                                     .filter(WebElement::isDisplayed)
                                     .count();

            return visibleCount >= EXPECTED_MENU_ITEMS;
        });
    }

    @Step("Open sidebar menu item: {menuName}")
    public void openMenu(String menuName) {

        // Wait for the sidebar to be visually rendered.
        waitForSidebar();

        By menu = By.xpath(
                "//ul[contains(@class,'oxd-main-menu')]//a[.//span[normalize-space()='" +
                        menuName + "']]"
        );

        click(menu);
    }

    public Set<String> getSidebarMenuNames() {

        waitForSidebar();

        List<WebElement> items =
                driver.findElements(SIDEBAR_MENU_NAMES);

        return items.stream()
                    .filter(WebElement::isDisplayed)
                    .map(WebElement::getText)
                    .map(String::trim)
                    .filter(value -> !value.isBlank())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
