package com.topica.checking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A FileStatus.
 */
@Entity
@Table(name = "file_status")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class FileStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "result")
    private String result;

    @Column(name = "status")
    private Integer status;

    @Column(name = "download_result_url")
    private String download_result_url;

    @Column(name = "file_type")
    private String fileType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public FileStatus name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public FileStatus url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public FileStatus result(String result) {
        this.result = result;
        return this;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public FileStatus status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDownload_result_url() {
        return download_result_url;
    }

    public FileStatus download_result_url(String download_result_url) {
        this.download_result_url = download_result_url;
        return this;
    }

    public void setDownload_result_url(String download_result_url) {
        this.download_result_url = download_result_url;
    }

    public String getFileType() {
        return fileType;
    }

    public FileStatus fileType(String fileType) {
        this.fileType = fileType;
        return this;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
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
        FileStatus fileStatus = (FileStatus) o;
        if (fileStatus.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileStatus.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FileStatus{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", url='" + getUrl() + "'" +
            ", result='" + getResult() + "'" +
            ", status=" + getStatus() +
            ", download_result_url='" + getDownload_result_url() + "'" +
            ", fileType='" + getFileType() + "'" +
            "}";
    }
}
