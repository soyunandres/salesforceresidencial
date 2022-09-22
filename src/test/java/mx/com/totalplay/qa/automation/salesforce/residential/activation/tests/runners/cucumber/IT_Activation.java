package mx.com.totalplay.qa.automation.salesforce.residential.activation.tests.runners.cucumber;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

/**
 * This class executes all scenarios inside feature file
 *
 * @author Alejandro Uribe
 * @version 1.0
 */
@CucumberOptions(
    glue = "mx.com.totalplay.qa.automation.salesforce.residential.activation.tests.steps.activation",
    features = {"classpath:features/activation"},
    plugin = {
        "pretty",
        "json:integration/reports/activationReport.json",
        "html:integration/reports/activationReport.html"
    }
)
public class IT_Activation extends AbstractTestNGCucumberTests {

}
