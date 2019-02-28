package com.topica.checking.service.impl;

import com.topica.checking.service.DataVersionService;
import com.topica.checking.domain.DataVersion;
import com.topica.checking.repository.DataVersionRepository;
import com.topica.checking.service.dto.ApiVersionDTO;
import com.topica.checking.service.dto.DataVersionDTO;
import com.topica.checking.service.mapper.DataVersionMapper;
import com.topica.checking.service.specification.ApiversionSpec;
import com.topica.checking.service.specification.DataVersionSpec;
import com.topica.checking.service.specification.SearchCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing DataVersion.
 */
@Service
@Transactional
public class DataVersionServiceImpl implements DataVersionService {

    private final Logger log = LoggerFactory.getLogger(DataVersionServiceImpl.class);

    private final DataVersionRepository dataVersionRepository;

    private final DataVersionMapper dataVersionMapper;

    public DataVersionServiceImpl(DataVersionRepository dataVersionRepository, DataVersionMapper dataVersionMapper) {
        this.dataVersionRepository = dataVersionRepository;
        this.dataVersionMapper = dataVersionMapper;
    }

    /**
     * Save a dataVersion.
     *
     * @param dataVersionDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public DataVersionDTO save(DataVersionDTO dataVersionDTO) {
        log.debug("Request to save DataVersion : {}", dataVersionDTO);

        DataVersion dataVersion = dataVersionMapper.toEntity(dataVersionDTO);
        dataVersion = dataVersionRepository.save(dataVersion);
        return dataVersionMapper.toDto(dataVersion);
    }


    @Scheduled(fixedDelay = 1000)
    public void hostAvailabilityCheck() {
            List<DataVersion> dataVersionList = dataVersionRepository.findAll();
            for (DataVersion dataVersion: dataVersionList) {

                String url = dataVersion.getDescription().split("//")[1].split("/")[0];
                String address = url.split(":")[0];
                int port = Integer.parseInt(url.split(":")[1]);
                try {
                Socket s = new Socket(address, port);
                    if (s.isConnected()) {
                        s.close();
                        dataVersion.setStatus(0);
                        dataVersionRepository.save(dataVersion);
                    }
                }
                catch (UnknownHostException e)
                { // unknown host
                    dataVersion.setStatus(1);
                    dataVersionRepository.save(dataVersion);
                }
                catch (IOException e) { // io exception, service probably not running
                    dataVersion.setStatus(1);
                    dataVersionRepository.save(dataVersion);
                }
                catch (NullPointerException e) {
                    dataVersion.setStatus(1);
                    dataVersionRepository.save(dataVersion);
                }
            }

    }

    /**
     * Get all the dataVersions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<DataVersionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DataVersions");
        return dataVersionRepository.findAll(pageable)
            .map(dataVersionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DataVersionDTO> findByVersion(String ver) {
        log.debug("InputVersionDTO to get  DataVersions");
        DataVersionSpec dataVersionSpec = new DataVersionSpec(new SearchCriteria("version",":",ver));
        return dataVersionRepository.findOne(dataVersionSpec).map(dataVersionMapper::toDto);
    }
    /**
     * Get one dataVersion by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<DataVersionDTO> findOne(Long id) {
        log.debug("Request to get DataVersion : {}", id);
        return dataVersionRepository.findById(id)
            .map(dataVersionMapper::toDto);
    }

    /**
     * Delete the dataVersion by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete DataVersion : {}", id);
        dataVersionRepository.deleteById(id);
    }
}
