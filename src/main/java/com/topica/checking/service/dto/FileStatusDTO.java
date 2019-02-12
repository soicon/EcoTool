package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FileStatus entity.
 */
public class FileStatusDTO implements Serializable {

    private Long id;

    private String name;

    private String url;

    private String result;

    private Integer status;

    private String download_result_url;

    private String fileType;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDownload_result_url() {
        return download_result_url;
    }

    public void setDownload_result_url(String download_result_url) {
        this.download_result_url = download_result_url;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FileStatusDTO fileStatusDTO = (FileStatusDTO) o;
        if (fileStatusDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fileStatusDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FileStatusDTO{" +
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
