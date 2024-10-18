package com.fluffy.universe.e2e;

import io.cucumber.core.cli.Main;
import org.junit.jupiter.api.Test;

public class RunCucumberTest {

    @Test
    public void trigger() {
        System.out.println("Text indicator for e2e.action in repository. Hello!");

        // Arguments to specify feature file, glue code, and report generation
        String[] cucumberOptions = new String[]{
                "-g", "com.fluffy.universe.e2esteps",  // Path to step definitions (glue)
                "src/test/resources/features/Registration.feature",  // Path to feature file
                "-p", "pretty",  // Optional, readable output
                "-p", "html:target/cucumber-reports.html"  // Optional, report generation
        };

        try {
            Main.run(cucumberOptions, Thread.currentThread().getContextClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Explicitly exit the JVM after Cucumber finishes
            System.exit(0);
        }
    }
}
