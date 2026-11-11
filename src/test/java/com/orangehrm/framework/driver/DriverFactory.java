package com.orangehrm.framework.driver;

import com.orangehrm.framework.config.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

/** Creates browser instances. Selenium Manager resolves compatible drivers automatically. */
public final class DriverFactory {
    private DriverFactory() { }

    public static WebDriver createDriver() {
        String browser = ConfigReader.get("browser").toLowerCase(Locale.ROOT);
        boolean headless = ConfigReader.getBoolean("headless");
        WebDriver driver;

        switch (browser) {
            case "chrome" -> {
                ChromeOptions options = new ChromeOptions();
                options.addArguments("--window-size=1440,1000", "--disable-notifications", "--remote-allow-origins=*");
                if (headless) options.addArguments("--headless=new", "--disable-gpu");
                driver = new ChromeDriver(options);
            }
            case "edge" -> {
                EdgeOptions options = new EdgeOptions();
                options.addArguments("--window-size=1440,1000", "--disable-notifications");
                if (headless) options.addArguments("--headless=new", "--disable-gpu");
                driver = new EdgeDriver(options);
            }
            case "firefox" -> {
                FirefoxOptions options = new FirefoxOptions();
                if (headless) options.addArguments("-headless");
                driver = new FirefoxDriver(options);
            }
            default -> throw new IllegalArgumentException("Unsupported browser '" + browser + "'. Use chrome, edge, or firefox.");
        }

        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(ConfigReader.getInt("page.load.timeout")));
        driver.manage().window().maximize();
        return driver;
    }
}
