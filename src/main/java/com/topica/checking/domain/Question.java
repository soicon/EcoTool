package com.topica.checking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question extends AbstractAuditingEntity    implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text")
    private String question_text;

    @Column(name = "visible")
    private Integer visible;

    @ManyToOne
    @JsonIgnoreProperties("questions")
    private Source source;

    @OneToOne(mappedBy = "question")
    @JsonIgnore
    private Answer answer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public Question question_text(String question_text) {
        this.question_text = question_text;
        return this;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public Integer getVisible() {
        return visible;
    }

    public Question visible(Integer visible) {
        this.visible = visible;
        return this;
    }

    public void setVisible(Integer visible) {
        this.visible = visible;
    }

    public Source getSource() {
        return source;
    }

    public Question source(Source source) {
        this.source = source;
        return this;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Answer getAnswer() {
        return answer;
    }

    public Question answer(Answer answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(Answer answer) {
        this.answer = answer;
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
        Question question = (Question) o;
        if (question.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", question_text='" + getQuestion_text() + "'" +
            ", visible=" + getVisible() +
            "}";
    }
}
