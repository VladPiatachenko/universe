package com.fluffy.universe.e2e;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features/Registration.feature",
        glue = "com.fluffy.universe.e2esteps",
        plugin = {"pretty", "html:target/cucumber-reports.html"},
        monochrome = true // Makes console output readable
)
public class CucumberTest {
}
