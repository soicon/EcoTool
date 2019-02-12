package com.topica.checking.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the RunnerLog entity. This class is used in RunnerLogResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /runner-logs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class RunnerLogCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter apiversionId;

    private LongFilter dataversionId;

    private LongFilter inputversionId;

    private LongFilter sourceId;

    private LongFilter questionId;

    private LongFilter answerId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getApiversionId() {
        return apiversionId;
    }

    public void setApiversionId(LongFilter apiversionId) {
        this.apiversionId = apiversionId;
    }

    public LongFilter getDataversionId() {
        return dataversionId;
    }

    public void setDataversionId(LongFilter dataversionId) {
        this.dataversionId = dataversionId;
    }

    public LongFilter getInputversionId() {
        return inputversionId;
    }

    public void setInputversionId(LongFilter inputversionId) {
        this.inputversionId = inputversionId;
    }

    public LongFilter getSourceId() {
        return sourceId;
    }

    public void setSourceId(LongFilter sourceId) {
        this.sourceId = sourceId;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
    }

    public LongFilter getAnswerId() {
        return answerId;
    }

    public void setAnswerId(LongFilter answerId) {
        this.answerId = answerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final RunnerLogCriteria that = (RunnerLogCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(apiversionId, that.apiversionId) &&
            Objects.equals(dataversionId, that.dataversionId) &&
            Objects.equals(inputversionId, that.inputversionId) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        apiversionId,
        dataversionId,
        inputversionId,
        sourceId,
        questionId,
        answerId
        );
    }

    @Override
    public String toString() {
        return "RunnerLogCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (apiversionId != null ? "apiversionId=" + apiversionId + ", " : "") +
                (dataversionId != null ? "dataversionId=" + dataversionId + ", " : "") +
                (inputversionId != null ? "inputversionId=" + inputversionId + ", " : "") +
                (sourceId != null ? "sourceId=" + sourceId + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
                (answerId != null ? "answerId=" + answerId + ", " : "") +
            "}";
    }

}
