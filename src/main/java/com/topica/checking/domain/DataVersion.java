package com.topica.checking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DataVersion.
 */
@Entity
@Table(name = "data_version")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class DataVersion extends  AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version")
    private String version;

    @Column(name = "description")
    private String description;

    @Column(name = "version_info")
    private String versionInfo;

    @Column(name = "status")
    private Integer status;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public DataVersion version(String version) {
        this.version = version;
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public DataVersion description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public DataVersion versionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
        return this;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public Integer getStatus() {
        return status;
    }

    public DataVersion status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        DataVersion dataVersion = (DataVersion) o;
        if (dataVersion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), dataVersion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "DataVersion{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", description='" + getDescription() + "'" +
            ", versionInfo='" + getVersionInfo() + "'" +
            ", status=" + getStatus() +
            "}";
    }
}
