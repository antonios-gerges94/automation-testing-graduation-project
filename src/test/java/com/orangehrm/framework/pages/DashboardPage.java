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
    private static final By DASHBOARD_HEADER = By.xpath("//h6[normalize-space()='Dashboard']");
    // The text is rendered in a nested span in OrangeHRM's current side panel.
    private static final By SIDEBAR_MENU_NAMES = By.cssSelector(
            "nav[aria-label='Sidepanel'] ul.oxd-main-menu a.oxd-main-menu-item .oxd-main-menu-item--name, " +
            "ul.oxd-main-menu a.oxd-main-menu-item .oxd-main-menu-item--name");

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isDashboardHeaderDisplayed() {
        return isDisplayed(DASHBOARD_HEADER);
    }

    @Step("Open sidebar menu item: {menuName}")
    public void openMenu(String menuName) {
        By menu = By.xpath("//ul[contains(@class,'oxd-main-menu')]//a[.//span[normalize-space()='" + menuName + "']]");
        click(menu);
    }

    public Set<String> getSidebarMenuNames() {
        // Login's URL can change before the SPA has finished rendering the side panel.
        // Wait for the expected menu labels to exist before collecting them.
        wait.until(d -> d.findElements(SIDEBAR_MENU_NAMES).size() >= 9);
        List<WebElement> items = driver.findElements(SIDEBAR_MENU_NAMES);
        return items.stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
