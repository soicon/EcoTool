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
 * Criteria class for the Viewtool entity. This class is used in SourceResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /sources?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SourceCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter path;

    private StringFilter device_id;

    private IntegerFilter type;

    private IntegerFilter status;

    private IntegerFilter need_re_answer;

    private IntegerFilter question_id;

    private LongFilter questionId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getPath() {
        return path;
    }

    public void setPath(StringFilter path) {
        this.path = path;
    }

    public StringFilter getDevice_id() {
        return device_id;
    }

    public void setDevice_id(StringFilter device_id) {
        this.device_id = device_id;
    }

    public IntegerFilter getType() {
        return type;
    }

    public void setType(IntegerFilter type) {
        this.type = type;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public IntegerFilter getNeed_re_answer() {
        return need_re_answer;
    }

    public void setNeed_re_answer(IntegerFilter need_re_answer) {
        this.need_re_answer = need_re_answer;
    }

    public IntegerFilter getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(IntegerFilter question_id) {
        this.question_id = question_id;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
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
        final SourceCriteria that = (SourceCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(path, that.path) &&
            Objects.equals(device_id, that.device_id) &&
            Objects.equals(type, that.type) &&
            Objects.equals(status, that.status) &&
            Objects.equals(need_re_answer, that.need_re_answer) &&
            Objects.equals(question_id, that.question_id) &&
            Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        path,
        device_id,
        type,
        status,
        need_re_answer,
        question_id,
        questionId
        );
    }

    @Override
    public String toString() {
        return "SourceCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (path != null ? "path=" + path + ", " : "") +
                (device_id != null ? "device_id=" + device_id + ", " : "") +
                (type != null ? "type=" + type + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (need_re_answer != null ? "need_re_answer=" + need_re_answer + ", " : "") +
                (question_id != null ? "question_id=" + question_id + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
            "}";
    }

}
