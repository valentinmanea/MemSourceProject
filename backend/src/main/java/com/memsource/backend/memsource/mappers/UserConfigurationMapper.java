package com.memsource.backend.memsource.mappers;

import com.memsource.backend.memsource.data.UserConfigurationEntity;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.mapstruct.Mapper;

@Mapper
public interface UserConfigurationMapper {
    UserConfigurationDto toUserConfigurationDto(UserConfigurationEntity entity);

    UserConfigurationEntity toUserConfigurationEntity(UserConfigurationDto dto);
}
