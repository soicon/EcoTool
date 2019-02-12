package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.InputVersionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity InputVersion and its DTO InputVersionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface InputVersionMapper extends EntityMapper<InputVersionDTO, InputVersion> {



    default InputVersion fromId(Long id) {
        if (id == null) {
            return null;
        }
        InputVersion inputVersion = new InputVersion();
        inputVersion.setId(id);
        return inputVersion;
    }
}
