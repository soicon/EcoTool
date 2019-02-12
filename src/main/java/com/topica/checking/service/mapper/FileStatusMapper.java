package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.FileStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity FileStatus and its DTO FileStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FileStatusMapper extends EntityMapper<FileStatusDTO, FileStatus> {



    default FileStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        FileStatus fileStatus = new FileStatus();
        fileStatus.setId(id);
        return fileStatus;
    }
}
