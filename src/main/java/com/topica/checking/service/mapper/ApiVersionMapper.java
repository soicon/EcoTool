package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.ApiVersionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ApiVersion and its DTO ApiVersionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ApiVersionMapper extends EntityMapper<ApiVersionDTO, ApiVersion> {



    default ApiVersion fromId(Long id) {
        if (id == null) {
            return null;
        }
        ApiVersion apiVersion = new ApiVersion();
        apiVersion.setId(id);
        return apiVersion;
    }
}
