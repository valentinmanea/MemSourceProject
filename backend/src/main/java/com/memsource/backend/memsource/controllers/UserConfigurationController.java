package com.memsource.backend.memsource.controllers;

import com.memsource.backend.memsource.data.UserConfigurationEntity;
import com.memsource.backend.memsource.services.UserConfigurationService;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@CrossOrigin
public class UserConfigurationController {

    private static final Logger LOGGER = Logger.getLogger(UserConfigurationController.class.getName());

    private final UserConfigurationService userConfigurationService;

    public UserConfigurationController(UserConfigurationService userConfigurationService) {
        this.userConfigurationService = userConfigurationService;
    }

    @PostMapping
    public UserConfigurationEntity addUserConf(UserConfigurationDto userConfigurationDto) {
        LOGGER.info("userConfiguration to add: " + userConfigurationDto);
        return userConfigurationService.addUserConfiguration(userConfigurationDto);
    }

    @PutMapping
    public UserConfigurationEntity updateUserConf(UserConfigurationDto userConfigurationDto) {
        LOGGER.info("userConfiguration to update: " + userConfigurationDto);
        return userConfigurationService.updateUserConfiguration(userConfigurationDto);
    }

    @GetMapping("/current")
    public UserConfigurationDto getCurrentConfiguration() {
        return userConfigurationService.getCurrentConfiguration();
    }

    @GetMapping("/projects")
    public void gertProjects(){
        userConfigurationService.getProjects();
    }
}
