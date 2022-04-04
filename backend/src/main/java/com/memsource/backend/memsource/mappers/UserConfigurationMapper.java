package com.memsource.backend.memsource.mappers;

import com.memsource.backend.memsource.data.UserConfigurationEntity;
import io.codejournal.maven.swagger2java.model.UserConfigurationDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface UserConfigurationMapper {
    UserConfigurationDto toUserConfigurationDto(UserConfigurationEntity entity);

    List<UserConfigurationDto> toUserConfigurationDtos(List<UserConfigurationEntity> entities);

    UserConfigurationEntity toUserConfigurationEntity(UserConfigurationDto dto);

    List<UserConfigurationEntity> toUserConfigurationEntities(List<UserConfigurationDto> dtos);
}
