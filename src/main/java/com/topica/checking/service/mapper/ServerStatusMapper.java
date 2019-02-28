package com.topica.checking.service.mapper;

import com.topica.checking.domain.*;
import com.topica.checking.service.dto.ServerStatusDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ServerStatus and its DTO ServerStatusDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ServerStatusMapper extends EntityMapper<ServerStatusDTO, ServerStatus> {



    default ServerStatus fromId(Long id) {
        if (id == null) {
            return null;
        }
        ServerStatus serverStatus = new ServerStatus();
        serverStatus.setId(id);
        return serverStatus;
    }
}
