package com.memsource.backend.memsource.services;

import com.memsource.backend.memsource.proxy.MemSourceProxyService;
import io.codejournal.maven.swagger2java.model.MemSourceProjectResponseDto;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ProjectsService {
    private static final Logger LOGGER = Logger.getLogger(ProjectsService.class.getName());

    private final FileTokenService fileTokenService;

    private final MemSourceProxyService memSourceProxyService;

    public ProjectsService(FileTokenService fileTokenService, MemSourceProxyService memSourceProxyService) {
        this.fileTokenService = fileTokenService;
        this.memSourceProxyService = memSourceProxyService;
    }

    public MemSourceProjectResponseDto getProjects() {
//        try {
        String token = fileTokenService.getToken();
        return memSourceProxyService.getProjects(token);

//        } catch (Exception e) {
//            LOGGER.info("exception: " + e.getClass());
//        }
//        return null;
    }
}
