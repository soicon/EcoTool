package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Answer entity.
 */
public class AnswerDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Integer status;

    private String answer_text;

    private Integer reviewer_id;

    private Integer user_id;

    private Long questionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public Integer getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(Integer reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AnswerDTO answerDTO = (AnswerDTO) o;
        if (answerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), answerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AnswerDTO{" +
            "id=" + getId() +
            ", status=" + getStatus() +
            ", answer_text='" + getAnswer_text() + "'" +
            ", reviewer_id=" + getReviewer_id() +
            ", user_id=" + getUser_id() +
            ", question=" + getQuestionId() +
            "}";
    }
}
