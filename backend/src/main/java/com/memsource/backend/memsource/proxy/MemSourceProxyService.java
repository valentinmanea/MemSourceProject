package com.memsource.backend.memsource.proxy;

import com.google.gson.Gson;
import io.codejournal.maven.swagger2java.model.MemSourceProjectResponseDto;
import io.codejournal.maven.swagger2java.model.MemSourceUserDto;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;


@Service
public class MemSourceProxyService {

    private final RestTemplate restTemplate;

    private static final Gson gson = new Gson();

    private static final Logger LOGGER = Logger.getLogger(MemSourceProxyService.class.getName());

    public MemSourceProxyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAuthToken(UserConfigurationDto userConfigurationDto) {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request =
                new HttpEntity<>(buildJsonObject(userConfigurationDto).toString(), headers);

        String userConfigurationAsJsonStr =
                restTemplate.postForObject("https://cloud.memsource.com/web/api2/v1/auth/login", request, String.class);

        LOGGER.info("userConfigurationAsJsonStr: " +userConfigurationAsJsonStr);

        return gson.fromJson(userConfigurationAsJsonStr, MemSourceUserDto.class).getToken();
    }

    private JSONObject buildJsonObject(UserConfigurationDto userConfigurationDto) {
        JSONObject userJsonObject = new JSONObject();

        userJsonObject.put("userName", userConfigurationDto.getUserName());
        userJsonObject.put("password", userConfigurationDto.getPassword());

        return userJsonObject;
    }

    public MemSourceProjectResponseDto getProjects(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "ApiToken " + authToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<MemSourceProjectResponseDto> response = restTemplate.exchange("https://cloud.memsource.com/web/api2/v1/projects", HttpMethod.GET, request, MemSourceProjectResponseDto.class);
        return response.getBody();
    }
}
