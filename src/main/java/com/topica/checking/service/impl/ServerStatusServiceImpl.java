package com.topica.checking.service.impl;

import com.topica.checking.service.ServerStatusService;
import com.topica.checking.domain.ServerStatus;
import com.topica.checking.repository.ServerStatusRepository;
import com.topica.checking.service.dto.ServerStatusDTO;
import com.topica.checking.service.mapper.ServerStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing ServerStatus.
 */
@Service
@Transactional
public class ServerStatusServiceImpl implements ServerStatusService {

    private final Logger log = LoggerFactory.getLogger(ServerStatusServiceImpl.class);

    private final ServerStatusRepository serverStatusRepository;

    private final ServerStatusMapper serverStatusMapper;

    public ServerStatusServiceImpl(ServerStatusRepository serverStatusRepository, ServerStatusMapper serverStatusMapper) {
        this.serverStatusRepository = serverStatusRepository;
        this.serverStatusMapper = serverStatusMapper;
    }


    /**
     * Save a serverStatus.
     *
     * @param serverStatusDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public ServerStatusDTO save(ServerStatusDTO serverStatusDTO) {
        log.debug("Request to save ServerStatus : {}", serverStatusDTO);

        ServerStatus serverStatus = serverStatusMapper.toEntity(serverStatusDTO);
        serverStatus = serverStatusRepository.save(serverStatus);
        return serverStatusMapper.toDto(serverStatus);
    }

    /**
     * Get all the serverStatuses.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ServerStatusDTO> findAll() {
        log.debug("Request to get all ServerStatuses");
        return serverStatusRepository.findAll().stream()
            .map(serverStatusMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one serverStatus by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ServerStatusDTO> findOne(Long id) {
        log.debug("Request to get ServerStatus : {}", id);
        return serverStatusRepository.findById(id)
            .map(serverStatusMapper::toDto);
    }

    /**
     * Delete the serverStatus by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ServerStatus : {}", id);
        serverStatusRepository.deleteById(id);
    }
}
