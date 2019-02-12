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
 * Criteria class for the Question entity. This class is used in QuestionResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /questions?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter question_text;

    private IntegerFilter visible;

    private LongFilter sourceId;

    private LongFilter answerId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(StringFilter question_text) {
        this.question_text = question_text;
    }

    public IntegerFilter getVisible() {
        return visible;
    }

    public void setVisible(IntegerFilter visible) {
        this.visible = visible;
    }

    public LongFilter getSourceId() {
        return sourceId;
    }

    public void setSourceId(LongFilter sourceId) {
        this.sourceId = sourceId;
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
        final QuestionCriteria that = (QuestionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(question_text, that.question_text) &&
            Objects.equals(visible, that.visible) &&
            Objects.equals(sourceId, that.sourceId) &&
            Objects.equals(answerId, that.answerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        question_text,
        visible,
        sourceId,
        answerId
        );
    }

    @Override
    public String toString() {
        return "QuestionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (question_text != null ? "question_text=" + question_text + ", " : "") +
                (visible != null ? "visible=" + visible + ", " : "") +
                (sourceId != null ? "sourceId=" + sourceId + ", " : "") +
                (answerId != null ? "answerId=" + answerId + ", " : "") +
            "}";
    }

}
