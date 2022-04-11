package com.memsource.backend.memsource.proxy;

import com.google.gson.Gson;
import io.codejournal.maven.swagger2java.model.MemSourceProjectResponseDto;
import io.codejournal.maven.swagger2java.model.MemSourceUserDto;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Logger;


@Service
public class MemSourceProxyService {

    private static final Gson gson = new Gson();

    private static final Logger LOGGER = Logger.getLogger(MemSourceProxyService.class.getName());

    //url from properties with default
    @Value("${external.api.url}")
    private static final String EXTERNAL_API_URL = "https://cloud.memsource.com/web/api2/v1/";

    private final RestTemplate restTemplate;

    public MemSourceProxyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private static HttpEntity<String> getStringHttpEntity(UserConfigurationDto userConfigurationDto) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(buildJsonObject(userConfigurationDto).toString(), headers);
    }

    private static JSONObject buildJsonObject(UserConfigurationDto userConfigurationDto) {
        JSONObject userJsonObject = new JSONObject();

        userJsonObject.put("userName", userConfigurationDto.getUserName());
        userJsonObject.put("password", userConfigurationDto.getPassword());

        return userJsonObject;
    }

    public String getAuthToken(UserConfigurationDto userConfigurationDto) {

        String userConfigurationAsJsonStr =
                restTemplate.postForObject(EXTERNAL_API_URL + "auth/login", getStringHttpEntity(userConfigurationDto), String.class);

        LOGGER.info("userConfigurationAsJsonStr: " + userConfigurationAsJsonStr);

        return gson.fromJson(userConfigurationAsJsonStr, MemSourceUserDto.class).getToken();
    }

    public MemSourceProjectResponseDto getProjects(String authToken) {
        return restTemplate.exchange(EXTERNAL_API_URL + "projects", HttpMethod.GET, buildHttpEntityWithAuth(authToken), MemSourceProjectResponseDto.class).getBody();
    }

    private HttpEntity<String> buildHttpEntityWithAuth(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "ApiToken " + authToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new HttpEntity<>(headers);
    }
}
