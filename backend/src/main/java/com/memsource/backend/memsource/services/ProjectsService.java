package com.memsource.backend.memsource.services;

import com.memsource.backend.memsource.proxy.MemSourceProxyService;
import io.codejournal.maven.swagger2java.model.MemSourceProjectResponseDto;
import org.springframework.stereotype.Service;

@Service
public class ProjectsService {

    private final FileTokenService fileTokenService;

    private final MemSourceProxyService memSourceProxyService;

    public ProjectsService(FileTokenService fileTokenService, MemSourceProxyService memSourceProxyService) {
        this.fileTokenService = fileTokenService;
        this.memSourceProxyService = memSourceProxyService;
    }

    public MemSourceProjectResponseDto getProjects() {
        String token = fileTokenService.getToken();
        return memSourceProxyService.getProjects(token);
    }
}
