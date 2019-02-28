package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the ServerStatus entity.
 */
public class ServerStatusDTO implements Serializable {

    private Long id;

    private String address;

    private Integer status;

    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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

        ServerStatusDTO serverStatusDTO = (ServerStatusDTO) o;
        if (serverStatusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), serverStatusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ServerStatusDTO{" +
            "id=" + getId() +
            ", address='" + getAddress() + "'" +
            ", status=" + getStatus() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
