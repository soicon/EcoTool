package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the InputVersion entity.
 */
public class InputVersionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String version;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        InputVersionDTO inputVersionDTO = (InputVersionDTO) o;
        if (inputVersionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), inputVersionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "InputVersionDTO{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
