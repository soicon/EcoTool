package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RunnerLog entity.
 */
public class RunnerLogDTO extends AbstractAuditingDTO implements Serializable {

    private Long id;

    private Long apiversionId;

    private String apiversionVersion;

    private Long dataversionId;

    private String dataversionVersion;

    private Long inputversionId;

    private String inputversionVersion;

    private Long sourceId;

    private String sourcePath;

    private Long questionId;

    private String questionQuestion_text;

    private Long answerId;

    private String answerAnswer_text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getApiversionId() {
        return apiversionId;
    }

    public void setApiversionId(Long apiVersionId) {
        this.apiversionId = apiVersionId;
    }

    public String getApiversionVersion() {
        return apiversionVersion;
    }

    public void setApiversionVersion(String apiVersionVersion) {
        this.apiversionVersion = apiVersionVersion;
    }

    public Long getDataversionId() {
        return dataversionId;
    }

    public void setDataversionId(Long dataVersionId) {
        this.dataversionId = dataVersionId;
    }

    public String getDataversionVersion() {
        return dataversionVersion;
    }

    public void setDataversionVersion(String dataVersionVersion) {
        this.dataversionVersion = dataVersionVersion;
    }

    public Long getInputversionId() {
        return inputversionId;
    }

    public void setInputversionId(Long inputVersionId) {
        this.inputversionId = inputVersionId;
    }

    public String getInputversionVersion() {
        return inputversionVersion;
    }

    public void setInputversionVersion(String inputVersionVersion) {
        this.inputversionVersion = inputVersionVersion;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getQuestionQuestion_text() {
        return questionQuestion_text;
    }

    public void setQuestionQuestion_text(String questionQuestion_text) {
        this.questionQuestion_text = questionQuestion_text;
    }

    public Long getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Long answerId) {
        this.answerId = answerId;
    }

    public String getAnswerAnswer_text() {
        return answerAnswer_text;
    }

    public void setAnswerAnswer_text(String answerAnswer_text) {
        this.answerAnswer_text = answerAnswer_text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RunnerLogDTO runnerLogDTO = (RunnerLogDTO) o;
        if (runnerLogDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), runnerLogDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RunnerLogDTO{" +
            "id=" + getId() +
            ", apiversion=" + getApiversionId() +
            ", apiversion='" + getApiversionVersion() + "'" +
            ", dataversion=" + getDataversionId() +
            ", dataversion='" + getDataversionVersion() + "'" +
            ", inputversion=" + getInputversionId() +
            ", inputversion='" + getInputversionVersion() + "'" +
            ", source=" + getSourceId() +
            ", source='" + getSourcePath() + "'" +
            ", question=" + getQuestionId() +
            ", question='" + getQuestionQuestion_text() + "'" +
            ", answer=" + getAnswerId() +
            ", answer='" + getAnswerAnswer_text() + "'" +
            "}";
    }
}
