package com.memsource.backend.memsource.functional;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.memsource.backend.memsource.steps"},
        tags = "@Ready"
)
public class RunCucumberTest {
}