package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FileConfig entity.
 */
public class FileConfigDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Long fileStatusId;

    private String fileStatusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFileStatusId() {
        return fileStatusId;
    }

    public void setFileStatusId(Long fileStatusId) {
        this.fileStatusId = fileStatusId;
    }

    public String getFileStatusName() {
        return fileStatusName;
    }

    public void setFileStatusName(String fileStatusName) {
        this.fileStatusName = fileStatusName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileConfigDTO fileConfigDTO = (FileConfigDTO) o;
        if (fileConfigDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileConfigDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FileConfigDTO{" +
            "id=" + getId() +
            ", fileStatus=" + getFileStatusId() +
            ", fileStatus='" + getFileStatusName() + "'" +
            "}";
    }
}
