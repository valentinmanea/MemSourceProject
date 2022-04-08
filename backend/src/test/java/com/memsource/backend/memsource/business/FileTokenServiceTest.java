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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

import static java.time.ZoneOffset.UTC;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

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

        UserConfigurationDto currentConfiguration = new UserConfigurationDto()
                .id(1)
                .userName("ValentinManea")
                .password("Password");

        when(userConfigurationService.getCurrentConfiguration()).thenReturn(currentConfiguration);

        when(memSourceProxyService.getAuthToken(currentConfiguration)).thenReturn("token");

        fileTokenService = new FileTokenService(memSourceProxyService, userConfigurationService);
    }

    @AfterEach
    public void after() throws IOException {
        Files.deleteIfExists(tokenNotPresentAtPath);
    }

    @Test
    public void tokenFileNotPresentOnDisk_tokenRetrievedFromExternalCall() {
        ReflectionTestUtils.setField(fileTokenService, "filePath", tokenNotPresentAtPath);

        assertFalse(Files.exists(tokenNotPresentAtPath));

        fileTokenService.getToken();

        assertTrue(Files.exists(tokenNotPresentAtPath));

        verify(memSourceProxyService, times(1)).getAuthToken(new UserConfigurationDto()
                .id(1)
                .userName("ValentinManea")
                .password("Password"));
    }

    @Test
    public void tokenFileExpired_1dayAgo_tokenRetrievedFromExternalCall() {

        boolean outDatedTokenFileCreated = createOutDatedTokenFile("token", 1, true);

        assertTrue(outDatedTokenFileCreated);

        ReflectionTestUtils.setField(fileTokenService, "filePath", tokenNotPresentAtPath);

        assertTrue(Files.exists(tokenNotPresentAtPath));

        fileTokenService.getToken();

        verify(memSourceProxyService, times(1)).getAuthToken(new UserConfigurationDto()
                .id(1)
                .userName("ValentinManea")
                .password("Password"));

        assertTrue(Files.exists(tokenNotPresentAtPath));
    }

    @Test
    public void tokenFileExpired_2daysAgo_tokenRetrievedFromExternalCall() {

        boolean outDatedTokenFileCreated = createOutDatedTokenFile("token", 1, true);

        assertTrue(outDatedTokenFileCreated);

        ReflectionTestUtils.setField(fileTokenService, "filePath", tokenNotPresentAtPath);

        assertTrue(Files.exists(tokenNotPresentAtPath));

        fileTokenService.getToken();

        verify(memSourceProxyService, times(1)).getAuthToken(new UserConfigurationDto()
                .id(1)
                .userName("ValentinManea")
                .password("Password"));

        assertTrue(Files.exists(tokenNotPresentAtPath));
    }

    @Test
    public void tokenFileNotExpired_tokenAssociatedWithCurrentUser_tokenRetrievedFromDisk() {

        boolean outDatedTokenFileCreated = createOutDatedTokenFile("token", 0, true);

        assertTrue(outDatedTokenFileCreated);

        ReflectionTestUtils.setField(fileTokenService, "filePath", tokenNotPresentAtPath);

        assertTrue(Files.exists(tokenNotPresentAtPath));

        fileTokenService.getToken();

        verify(memSourceProxyService, never()).getAuthToken(new UserConfigurationDto()
                .id(1)
                .userName("ValentinManea")
                .password("Password"));

        assertTrue(Files.exists(tokenNotPresentAtPath));
    }

    @Test
    public void tokenFileNotExpired_tokenNotAssociatedWithCurrentUser_tokenRetrievedFromExternal() {

        boolean outDatedTokenFileCreated = createOutDatedTokenFile("token", 0, false);

        assertTrue(outDatedTokenFileCreated);

        ReflectionTestUtils.setField(fileTokenService, "filePath", tokenNotPresentAtPath);

        assertTrue(Files.exists(tokenNotPresentAtPath));

        fileTokenService.getToken();

        verify(memSourceProxyService, times(1)).getAuthToken(new UserConfigurationDto()
                .id(1)
                .userName("ValentinManea")
                .password("Password"));

        assertTrue(Files.exists(tokenNotPresentAtPath));
    }

    private boolean createOutDatedTokenFile(String token, int expiredDays, boolean useCurrentUser) {
        ReflectionTestUtils.setField(fileTokenService, "filePath", tokenNotPresentAtPath);
        UserConfigurationDto userConfigurationDto;
        if (useCurrentUser) {
            userConfigurationDto = new UserConfigurationDto()
                    .id(1)
                    .userName("ValentinManea")
                    .password("Password");
        } else {
            userConfigurationDto = buildOtherConfiguration();
        }
        ReflectionTestUtils.invokeMethod(fileTokenService, "writeOnDisk", userConfigurationDto, token);

        File file = tokenNotPresentAtPath.toFile();

        return file.setLastModified(LocalDateTime.now(UTC).minusDays(expiredDays).toEpochSecond(UTC));
    }

    private static UserConfigurationDto buildOtherConfiguration() {
        return new UserConfigurationDto()
                .id(2)
                .userName("ValentinManea")
                .password("Password");
    }
}
