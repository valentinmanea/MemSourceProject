package com.memsource.backend.memsource.services;

import com.memsource.backend.memsource.exceptions.TokenNotRetrievedException;
import com.memsource.backend.memsource.proxy.MemSourceProxyService;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;

@Service
public class FileTokenService {

    private static final Logger LOGGER = Logger.getLogger(FileTokenService.class.getName());

    private final MemSourceProxyService memSourceProxyService;

    private final UserConfigurationService userConfigurationService;

    @Value("${authentication.token.filename}")
    private Path filePath;

    public FileTokenService(MemSourceProxyService memSourceProxyService, UserConfigurationService userConfigurationService) {
        this.memSourceProxyService = memSourceProxyService;
        this.userConfigurationService = userConfigurationService;
    }

    public String getToken() {
        try {
            if (Files.exists(filePath) && isTokenNotExpired()) {

                String[] tokenInformation = Files.readAllLines(filePath).get(0).split(" ");

                String token = tokenInformation[0];

                if (currentUserAssociatedWithToken(tokenInformation)) {
                    LOGGER.info("Token retrieved from disk: " + token);
                    return token;
                } else {
                    return retrieveTokenFromExternal();
                }
            } else {
                return retrieveTokenFromExternal();
            }
        } catch (IOException e) {
            throw new TokenNotRetrievedException("Authentication token cannot be retrieved", e);
        }
    }

    private String retrieveTokenFromExternal() throws IOException {
        UserConfigurationDto currentConfiguration = userConfigurationService.getCurrentConfiguration();

        String token = memSourceProxyService.getAuthToken(currentConfiguration);

        writeOnDisk(currentConfiguration, token);

        LOGGER.info("Token retrieved from external: " + token);

        return token;
    }

    private void writeOnDisk(UserConfigurationDto currentConfiguration, String token) throws IOException {
        String tokenAssociatedWithUser = mergeTokenWithUserConf(currentConfiguration, token);

        Files.write(filePath, tokenAssociatedWithUser.getBytes());
    }

    private String mergeTokenWithUserConf(UserConfigurationDto currentConfiguration, String token) {
        return new StringBuilder(token)
                .append(" ")
                .append(currentConfiguration.getId())
                .append(" ")
                .append(currentConfiguration.getUserName())
                .append(" ")
                .append(currentConfiguration.getPassword())
                .toString();
    }

    private boolean currentUserAssociatedWithToken(String[] tokenInformation) {
        String userId = tokenInformation[1];

        String userName = tokenInformation[2];

        String password = tokenInformation[3];

        UserConfigurationDto userConfigurationDto = new UserConfigurationDto()
                .id(Integer.parseInt(userId))
                .userName(userName)
                .password(password);

        UserConfigurationDto currentConfiguration = userConfigurationService.getCurrentConfiguration();

        return currentConfiguration.equals(userConfigurationDto);
    }

    private boolean isTokenNotExpired() throws IOException {
        FileTime creationTime = (FileTime) Files.getAttribute(filePath, "creationTime");

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime convertedFileTime = LocalDateTime.ofInstant(creationTime.toInstant(), ZoneId.systemDefault());

        long days = Duration.between(convertedFileTime.toLocalDate().atStartOfDay(), now.toLocalDate().atStartOfDay()).toDays();// another option

        return days < 1;
    }
}
