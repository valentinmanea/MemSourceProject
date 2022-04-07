package com.memsource.backend.memsource.controllers;

import com.memsource.backend.memsource.services.ProjectsService;
import io.codejournal.maven.swagger2java.model.MemSourceProjectResponseDto;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class ProjectsController {
    private final ProjectsService projectsService;

    public ProjectsController(ProjectsService projectsService) {
        this.projectsService = projectsService;
    }

    @GetMapping("/projects")
    public MemSourceProjectResponseDto getProjects() {
        return projectsService.getProjects();
    }
}
