package com.orangehrm.framework.listeners;

import com.orangehrm.framework.utils.LoggerUtil;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.openqa.selenium.WebDriverException;

import java.util.concurrent.atomic.AtomicInteger;

/** Retries each failed test at most two times (three total attempts). */
public class RetryAnalyzer implements IRetryAnalyzer {
    private static final int MAX_RETRIES = 2;
    private final AtomicInteger retryCount = new AtomicInteger(0);

    @Override
    public boolean retry(ITestResult result) {
        // Assertion failures are deterministic and should not be retried.
        // The E2E test creates persistent demo data, so retrying it could create duplicates.
        if ("createEmployeeAndFindItInEmployeeList".equals(result.getMethod().getMethodName())) {
            return false;
        }
        if (!(result.getThrowable() instanceof WebDriverException)) {
            return false;
        }

        int attempt = retryCount.getAndIncrement();
        if (attempt < MAX_RETRIES) {
            LoggerUtil.info("Retrying transient WebDriver failure in '" + result.getName()
                    + "' - retry " + (attempt + 1) + " of " + MAX_RETRIES);
            return true;
        }
        return false;
    }
}
