package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.SourceDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Viewtool and its DTO SourceDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SourceMapper extends EntityMapper<SourceDTO, Source> {


    @Mapping(target = "questions", ignore = true)
    Source toEntity(SourceDTO sourceDTO);

    default Source fromId(Long id) {
        if (id == null) {
            return null;
        }
        Source source = new Source();
        source.setId(id);
        return source;
    }
}
