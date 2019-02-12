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
 * Criteria class for the Answer entity. This class is used in AnswerResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /answers?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AnswerCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter status;

    private StringFilter answer_text;

    private IntegerFilter reviewer_id;

    private IntegerFilter user_id;

    private LongFilter questionId;

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getStatus() {
        return status;
    }

    public void setStatus(IntegerFilter status) {
        this.status = status;
    }

    public StringFilter getAnswer_text() {
        return answer_text;
    }

    public void setAnswer_text(StringFilter answer_text) {
        this.answer_text = answer_text;
    }

    public IntegerFilter getReviewer_id() {
        return reviewer_id;
    }

    public void setReviewer_id(IntegerFilter reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public IntegerFilter getUser_id() {
        return user_id;
    }

    public void setUser_id(IntegerFilter user_id) {
        this.user_id = user_id;
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
        final AnswerCriteria that = (AnswerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(answer_text, that.answer_text) &&
            Objects.equals(reviewer_id, that.reviewer_id) &&
            Objects.equals(user_id, that.user_id) &&
            Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        status,
        answer_text,
        reviewer_id,
        user_id,
        questionId
        );
    }

    @Override
    public String toString() {
        return "AnswerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (answer_text != null ? "answer_text=" + answer_text + ", " : "") +
                (reviewer_id != null ? "reviewer_id=" + reviewer_id + ", " : "") +
                (user_id != null ? "user_id=" + user_id + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
            "}";
    }

}
