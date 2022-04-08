package com.memsource.backend.memsource.steps;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class GetProjectsStep {
    @Given("^I'm authenticated with username: (.+) and password: (.+)$")
    public void given(String firstParam, String secondParam) {
        System.out.println("firstParam: " + firstParam+ " secondParam: " + secondParam);
    }

    @When("^I'm asking for my projects$")
    public void when() {

    }

    @Then("^I should receive exactly (\\d+) project$")
    public void then(int nbOfProjects) {
        System.out.println(nbOfProjects);
    }

}
