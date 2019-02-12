package com.topica.checking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AppVersion.
 */
@Entity
@Table(name = "app_version")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class AppVersion extends AbstractAuditingEntity  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "api_ver")
    private String apiVer;

    @Column(name = "data_ver")
    private String dataVer;

    @Column(name = "input_ver")
    private String inputVer;



    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiVer() {
        return apiVer;
    }

    public AppVersion apiVer(String apiVer) {
        this.apiVer = apiVer;
        return this;
    }

    public void setApiVer(String apiVer) {
        this.apiVer = apiVer;
    }

    public String getDataVer() {
        return dataVer;
    }

    public AppVersion dataVer(String dataVer) {
        this.dataVer = dataVer;
        return this;
    }

    public void setDataVer(String dataVer) {
        this.dataVer = dataVer;
    }

    public String getInputVer() {
        return inputVer;
    }

    public AppVersion inputVer(String inputVer) {
        this.inputVer = inputVer;
        return this;
    }

    public void setInputVer(String inputVer) {
        this.inputVer = inputVer;
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
        AppVersion appVersion = (AppVersion) o;
        if (appVersion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appVersion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppVersion{" +
            "id=" + getId() +
            ", apiVer='" + getApiVer() + "'" +
            ", dataVer='" + getDataVer() + "'" +
            ", inputVer='" + getInputVer() + "'" +
            "}";
    }
}
