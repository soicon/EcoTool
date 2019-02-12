package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.AppVersionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity AppVersion and its DTO AppVersionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AppVersionMapper extends EntityMapper<AppVersionDTO, AppVersion> {



    default AppVersion fromId(Long id) {
        if (id == null) {
            return null;
        }
        AppVersion appVersion = new AppVersion();
        appVersion.setId(id);
        return appVersion;
    }
}
