package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.DataPrefixDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DataPrefix and its DTO DataPrefixDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DataPrefixMapper extends EntityMapper<DataPrefixDTO, DataPrefix> {



    default DataPrefix fromId(Long id) {
        if (id == null) {
            return null;
        }
        DataPrefix dataPrefix = new DataPrefix();
        dataPrefix.setId(id);
        return dataPrefix;
    }
}
