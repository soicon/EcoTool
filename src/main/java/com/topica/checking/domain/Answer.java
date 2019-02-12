package com.topica.checking.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Answer.
 */
@Entity
@Table(name = "answer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Answer extends AbstractAuditingEntity    implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private Integer status;

    @Column(name = "answer_text")
    private String answer_text;

    @Column(name = "reviewer_id")
    private Integer reviewer_id;

    @Column(name = "user_id")
    private Integer user_id;

    @OneToOne    @JoinColumn(unique = true)
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public Answer status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAnswer_text() {
        return answer_text;
    }

    public Answer answer_text(String answer_text) {
        this.answer_text = answer_text;
        return this;
    }

    public void setAnswer_text(String answer_text) {
        this.answer_text = answer_text;
    }

    public Integer getReviewer_id() {
        return reviewer_id;
    }

    public Answer reviewer_id(Integer reviewer_id) {
        this.reviewer_id = reviewer_id;
        return this;
    }

    public void setReviewer_id(Integer reviewer_id) {
        this.reviewer_id = reviewer_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public Answer user_id(Integer user_id) {
        this.user_id = user_id;
        return this;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Question getQuestion() {
        return question;
    }

    public Answer question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
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
        Answer answer = (Answer) o;
        if (answer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), answer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Answer{" +
            "id=" + getId() +
            ", status=" + getStatus() +
            ", answer_text='" + getAnswer_text() + "'" +
            ", reviewer_id=" + getReviewer_id() +
            ", user_id=" + getUser_id() +
            "}";
    }
}
