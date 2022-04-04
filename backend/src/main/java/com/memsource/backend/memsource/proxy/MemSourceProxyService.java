package com.memsource.backend.memsource.proxy;

import com.memsource.backend.memsource.exceptions.AuthTokenRetrievalException;
import io.codejournal.maven.swagger2java.model.MemSourceUserDto;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class MemSourceProxyService {

    private final RestTemplate restTemplate;
    private String API_ENDPOINT = "https://cloud.memsource.com/web/api2/v1/";

    public MemSourceProxyService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getAuthToken(UserConfigurationDto userConfigurationDto) {
        ResponseEntity<MemSourceUserDto> memSourceUserDtoResponseEntity = restTemplate.postForEntity(API_ENDPOINT + "auth/login", userConfigurationDto, MemSourceUserDto.class);
        if (memSourceUserDtoResponseEntity.getBody() != null) {
            return memSourceUserDtoResponseEntity.getBody().getToken();
        } else {
            throw new AuthTokenRetrievalException("Auth token cannot be retrieved");
        }
    }

    public void getProjects(String authToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "ApiToken " + authToken );
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Object> response = restTemplate.exchange(API_ENDPOINT + "projects", HttpMethod.GET, request, Object.class);

    }
}
