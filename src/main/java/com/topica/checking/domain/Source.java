package com.topica.checking.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Viewtool.
 */
@Entity
@Table(name = "source")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Source extends AbstractAuditingEntity    implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "path")
    private String path;

    @Column(name = "device_id")
    private String device_id;

    @Column(name = "jhi_type")
    private Integer type;

    @Column(name = "status")
    private Integer status;

    @Column(name = "need_re_answer")
    private Integer need_re_answer;

    @Column(name = "question_id")
    private Integer question_id;

    @OneToMany(mappedBy = "source")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Question> questions = new HashSet<>();
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public Source path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDevice_id() {
        return device_id;
    }

    public Source device_id(String device_id) {
        this.device_id = device_id;
        return this;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public Integer getType() {
        return type;
    }

    public Source type(Integer type) {
        this.type = type;
        return this;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public Source status(Integer status) {
        this.status = status;
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getNeed_re_answer() {
        return need_re_answer;
    }

    public Source need_re_answer(Integer need_re_answer) {
        this.need_re_answer = need_re_answer;
        return this;
    }

    public void setNeed_re_answer(Integer need_re_answer) {
        this.need_re_answer = need_re_answer;
    }

    public Integer getQuestion_id() {
        return question_id;
    }

    public Source question_id(Integer question_id) {
        this.question_id = question_id;
        return this;
    }

    public void setQuestion_id(Integer question_id) {
        this.question_id = question_id;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Source questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Source addQuestion(Question question) {
        this.questions.add(question);
        question.setSource(this);
        return this;
    }

    public Source removeQuestion(Question question) {
        this.questions.remove(question);
        question.setSource(null);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
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
        Source source = (Source) o;
        if (source.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), source.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Viewtool{" +
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
