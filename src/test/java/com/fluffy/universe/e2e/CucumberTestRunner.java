package com.fluffy.universe.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features", // Path to your feature files
        glue = "com.fluffy.universe.steps", // Path to your step definitions
        plugin = {"pretty", "html:target/cucumber-reports.html"}, // Plugins for reporting
        monochrome = true // Makes console output readable
)
public class CucumberTestRunner {
}
