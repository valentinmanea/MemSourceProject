package com.memsource.backend.memsource.business;

import com.memsource.backend.memsource.proxy.MemSourceProxyService;
import com.memsource.backend.memsource.services.FileTokenService;
import com.memsource.backend.memsource.services.UserConfigurationService;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

public class FileTokenServiceTest {

    @Mock
    private MemSourceProxyService memSourceProxyService;

    @Mock
    private UserConfigurationService userConfigurationService;

    private FileTokenService fileTokenService;

    private Path tokenNotPresentAtPath = Paths.get("test_token.txt");

    @BeforeEach
    public void before() {
        MockitoAnnotations.openMocks(this);

        UserConfigurationDto currentConfiguration = buildCurrentConfiguration();

        when(userConfigurationService.getCurrentConfiguration()).thenReturn(currentConfiguration);

        when(memSourceProxyService.getAuthToken(currentConfiguration)).thenReturn("token");

        fileTokenService = new FileTokenService(memSourceProxyService, userConfigurationService);
    }

    @AfterEach
    public void after() throws IOException {
        Files.deleteIfExists(tokenNotPresentAtPath);
    }

    @Test
    public void tokenNotPresentOnDiskBeforeExternalCall() {
        ReflectionTestUtils.setField(fileTokenService, "filePath", tokenNotPresentAtPath);

        assertFalse(Files.exists(tokenNotPresentAtPath));

        fileTokenService.getToken();

        assertTrue(Files.exists(tokenNotPresentAtPath));
    }

    private static UserConfigurationDto buildCurrentConfiguration() {
        return new UserConfigurationDto()
                .userName("ValentinManea")
                .password("Password");
    }
}
