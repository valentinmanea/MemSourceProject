package com.memsource.backend.memsource.steps;

import com.memsource.backend.memsource.services.ProjectsService;
import com.memsource.backend.memsource.services.UserConfigurationService;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.codejournal.maven.swagger2java.model.MemSourceProjectResponseDto;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.assertj.core.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.logging.Logger;

@SpringBootTest
public class GetProjectsStep {

    @Autowired
    private UserConfigurationService configurationService;

    @Autowired
    private ProjectsService projectsService;

    private MemSourceProjectResponseDto projects;

    private static final Logger LOGGER = Logger.getLogger(GetProjectsStep.class.getName());

    @Given("^Current configuration has the username: (.+) and password: (.+)$")
    public void given(String firstParam, String secondParam) {
        UserConfigurationDto userConfigurationDto = new UserConfigurationDto();
        userConfigurationDto.userName(firstParam);
        userConfigurationDto.password(secondParam);
        configurationService.addUserConfiguration(userConfigurationDto);
    }

    @When("^I'm asking for my projects$")
    public void when() {
        projects = projectsService.getProjects();
    }

    @Then("^I should receive exactly (\\d+) project$")
    public void then(int nbOfProjects) {
        LOGGER.info("Projects: "  + projects);

        Assertions.assertThat(projects.getContent()).hasSize(nbOfProjects);
    }

}
