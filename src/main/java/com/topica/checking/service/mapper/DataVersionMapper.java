package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.DataVersionDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity DataVersion and its DTO DataVersionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface DataVersionMapper extends EntityMapper<DataVersionDTO, DataVersion> {



    default DataVersion fromId(Long id) {
        if (id == null) {
            return null;
        }
        DataVersion dataVersion = new DataVersion();
        dataVersion.setId(id);
        return dataVersion;
    }
}
