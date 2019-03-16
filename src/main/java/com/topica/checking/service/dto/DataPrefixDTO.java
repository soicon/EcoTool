package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the DataPrefix entity.
 */
public class DataPrefixDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DataPrefixDTO dataPrefixDTO = (DataPrefixDTO) o;
        if (dataPrefixDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataPrefixDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataPrefixDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
