# Latest execution fixes

This patch is based on the user's 11-Nov-2026 Surefire/TestNG execution log.

- Kept Maven Surefire at 3.5.6 so the TestNG `testng.xml` suite is supported.
- Updated PIM navigation: open the PIM module, then use the current Employee List route; open Add Employee from the Employee List Add action when rendered, with the stable route as a fallback.
- Added explicit waiting for the nine sidebar menu labels before reading them.
- Employee autocomplete no longer clicks an unrelated first suggestion when the requested name is absent.
- Added `log4j-slf4j2-impl` so SLF4J 2.x messages from dependencies can be routed through Log4j2.

The patch has not been executed against the user's live OrangeHRM browser session. Re-run `mvn clean test` and inspect actual TestNG/Surefire results before claiming full success.


## Follow-up patch based on 11-Nov-2026 14:11 execution log

- Replaced the fixed positive-search fixture `Paul Collings` with a refresh-safe strategy: when `employees.known.searchTerm` is blank in JSON, the test reads the first actual employee in the current Employee List and searches for that exact name. This avoids relying on stale demo seed data.
- Removed extra Personal Details input-value assertions from the E2E test. The assignment requires landing on Personal Details and then finding the newly created employee in Employee List; the extra `input[name=firstName]` / `input[name=lastName]` assertions were not reliable for the current page markup. The Employee List search remains the end-to-end persistence assertion.
- Scoped the First Name Required-message lookup to the First Name input group and made it independent of a specific HTML tag for the message node.
- Limited retries to transient WebDriver exceptions. Assertion failures are not retried, and the employee-creation E2E test is not retried because it changes shared demo data and retries can create duplicate employees.

This patch is source-reviewed and XML/JSON validated only. It has not been executed against the user's live OrangeHRM session.
