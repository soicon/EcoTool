package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.CorePrefixDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CorePrefix and its DTO CorePrefixDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CorePrefixMapper extends EntityMapper<CorePrefixDTO, CorePrefix> {



    default CorePrefix fromId(Long id) {
        if (id == null) {
            return null;
        }
        CorePrefix corePrefix = new CorePrefix();
        corePrefix.setId(id);
        return corePrefix;
    }
}
