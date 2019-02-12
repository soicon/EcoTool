package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Viewtool entity.
 */
public class SourceDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private String path;

    private String device_id;

    private Integer type;

    private Integer status;

    private Integer need_re_answer;

    private Integer question_id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNeed_re_answer() {
        return need_re_answer;
    }

    public void setNeed_re_answer(Integer need_re_answer) {
        this.need_re_answer = need_re_answer;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SourceDTO sourceDTO = (SourceDTO) o;
        if (sourceDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sourceDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SourceDTO{" +
            "id=" + getId() +
            ", path='" + getPath() + "'" +
            ", device_id='" + getDevice_id() + "'" +
            ", type=" + getType() +
            ", status=" + getStatus() +
            ", need_re_answer=" + getNeed_re_answer() +
            ", question_id=" + getQuestion_id() +
            "}";
    }
}
