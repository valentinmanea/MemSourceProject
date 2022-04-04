package com.memsource.backend.memsource.services;

import com.memsource.backend.memsource.proxy.MemSourceProxyService;
import com.memsource.backend.memsource.data.UserConfigurationEntity;
import com.memsource.backend.memsource.exceptions.UserConfigurationAlreadyPresentException;
import com.memsource.backend.memsource.exceptions.UserConfigurationNotPresent;
import com.memsource.backend.memsource.mappers.UserConfigurationMapper;
import com.memsource.backend.memsource.repositories.UserConfigurationRepo;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserConfigurationService {

    private final UserConfigurationRepo userConfigurationRepo;

    private final MemSourceProxyService memSourceProxyService;

    private final UserConfigurationMapper userConfigurationMapper = Mappers.getMapper(UserConfigurationMapper.class);

    public UserConfigurationService(UserConfigurationRepo userConfigurationRepo, MemSourceProxyService memSourceProxyService) {
        this.userConfigurationRepo = userConfigurationRepo;
        this.memSourceProxyService = memSourceProxyService;
    }

    public UserConfigurationEntity addUserConfiguration(UserConfigurationDto userConfigurationDto) {
        if (!userConfigurationRepo.findAll().isEmpty()) {
            throw new UserConfigurationAlreadyPresentException("AppConfiguration already exists");
        }
        UserConfigurationEntity userConfigurationEntity = userConfigurationMapper.toUserConfigurationEntity(userConfigurationDto);
        return userConfigurationRepo.save(userConfigurationEntity);
    }

    public UserConfigurationEntity updateUserConfiguration(UserConfigurationDto userConfigurationDto) {
        Optional<UserConfigurationEntity> any = userConfigurationRepo.findAll().stream().findAny();
        if (any.isPresent()) {
            UserConfigurationEntity userConfigurationEntity = any.get();
            userConfigurationEntity.setPassword(userConfigurationDto.getPassword());
            userConfigurationEntity.setUserName(userConfigurationDto.getUserName());
            return userConfigurationRepo.save(userConfigurationEntity);
        } else {
            throw new UserConfigurationNotPresent("AppConfiguration not present!");
        }
    }

    public UserConfigurationDto getCurrentConfiguration() {
        List<UserConfigurationEntity> all = userConfigurationRepo.findAll();
        if (all.isEmpty()) {
            throw new UserConfigurationNotPresent("AppConfiguration not present!");
        } else {
            return userConfigurationMapper.toUserConfigurationDto(all.get(0));
        }
    }

    public void getProjects() {
        UserConfigurationDto currentConfiguration = getCurrentConfiguration();
        String authToken = memSourceProxyService.getAuthToken(currentConfiguration);
        memSourceProxyService.getProjects(authToken);
    }
}
