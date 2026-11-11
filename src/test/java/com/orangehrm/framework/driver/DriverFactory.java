
package com.orangehrm.framework.driver;

import com.orangehrm.framework.config.ConfigReader;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.time.Duration;
import java.util.Locale;

/**
 * Creates browser instances.
 * Selenium Manager resolves compatible drivers automatically.
 */
public final class DriverFactory {

    private DriverFactory() {
    }

    public static WebDriver createDriver() {

        String browser = ConfigReader.get("browser")
                                     .toLowerCase(Locale.ROOT);

        boolean headless = ConfigReader.getBoolean("headless");

        WebDriver driver;

        switch (browser) {

            case "chrome" -> {

                ChromeOptions options = new ChromeOptions();

                options.addArguments(
                        "--window-size=1440,1000",
                        "--disable-notifications",
                        "--remote-allow-origins=*",
                        "--force-device-scale-factor=1"
                );

                if (headless) {
                    options.addArguments(
                            "--headless=new",
                            "--disable-gpu",
                            "--no-sandbox",
                            "--disable-dev-shm-usage"
                    );
                }

                driver = new ChromeDriver(options);
            }

            case "edge" -> {

                EdgeOptions options = new EdgeOptions();

                options.addArguments(
                        "--window-size=1440,1000",
                        "--disable-notifications"
                );

                if (headless) {
                    options.addArguments(
                            "--headless=new",
                            "--disable-gpu",
                            "--no-sandbox",
                            "--disable-dev-shm-usage"
                    );
                }

                driver = new EdgeDriver(options);
            }

            case "firefox" -> {

                FirefoxOptions options = new FirefoxOptions();

                if (headless) {
                    options.addArguments("-headless");
                }

                driver = new FirefoxDriver(options);
            }

            default -> throw new IllegalArgumentException(
                    "Unsupported browser: " + browser
            );
        }

        driver.manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(
                        ConfigReader.getInt("page.load.timeout")
                )
        );

        // Headless browsers must use an explicit viewport size.
        // Do not maximize a headless browser.
        if (headless) {
            driver.manage().window().setSize(
                    new Dimension(1440, 1000)
            );
        } else {
            driver.manage().window().maximize();
        }

        return driver;
    }
}
