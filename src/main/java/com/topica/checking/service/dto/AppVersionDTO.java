package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AppVersion entity.
 */
public class AppVersionDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String apiVer;

    private String dataVer;

    private String inputVer;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiVer() {
        return apiVer;
    }

    public void setApiVer(String apiVer) {
        this.apiVer = apiVer;
    }

    public String getDataVer() {
        return dataVer;
    }

    public void setDataVer(String dataVer) {
        this.dataVer = dataVer;
    }

    public String getInputVer() {
        return inputVer;
    }

    public void setInputVer(String inputVer) {
        this.inputVer = inputVer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AppVersionDTO appVersionDTO = (AppVersionDTO) o;
        if (appVersionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appVersionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppVersionDTO{" +
            "id=" + getId() +
            ", apiVer='" + getApiVer() + "'" +
            ", dataVer='" + getDataVer() + "'" +
            ", inputVer='" + getInputVer() + "'" +
            "}";
    }
}
