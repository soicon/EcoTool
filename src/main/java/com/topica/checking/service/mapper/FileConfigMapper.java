package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.FileConfigDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FileConfig and its DTO FileConfigDTO.
 */
@Mapper(componentModel = "spring", uses = {FileStatusMapper.class})
public interface FileConfigMapper extends EntityMapper<FileConfigDTO, FileConfig> {

    @Mapping(source = "fileStatus.id", target = "fileStatusId")
    @Mapping(source = "fileStatus.name", target = "fileStatusName")
    FileConfigDTO toDto(FileConfig fileConfig);

    @Mapping(source = "fileStatusId", target = "fileStatus")
    FileConfig toEntity(FileConfigDTO fileConfigDTO);

    default FileConfig fromId(Long id) {
        if (id == null) {
            return null;
        }
        FileConfig fileConfig = new FileConfig();
        fileConfig.setId(id);
        return fileConfig;
    }
}
