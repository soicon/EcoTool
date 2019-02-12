package com.topica.checking.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A RunnerLog.
 */
@Entity
@Table(name = "runner_log")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RunnerLog extends AbstractAuditingEntity    implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("")
    private ApiVersion apiversion;

    @ManyToOne
    @JsonIgnoreProperties("")
    private DataVersion dataversion;

    @ManyToOne
    @JsonIgnoreProperties("")
    private InputVersion inputversion;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Source source;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties("")
    private Answer answer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApiVersion getApiversion() {
        return apiversion;
    }

    public RunnerLog apiversion(ApiVersion apiVersion) {
        this.apiversion = apiVersion;
        return this;
    }

    public void setApiversion(ApiVersion apiVersion) {
        this.apiversion = apiVersion;
    }

    public DataVersion getDataversion() {
        return dataversion;
    }

    public RunnerLog dataversion(DataVersion dataVersion) {
        this.dataversion = dataVersion;
        return this;
    }

    public void setDataversion(DataVersion dataVersion) {
        this.dataversion = dataVersion;
    }

    public InputVersion getInputversion() {
        return inputversion;
    }

    public RunnerLog inputversion(InputVersion inputVersion) {
        this.inputversion = inputVersion;
        return this;
    }

    public void setInputversion(InputVersion inputVersion) {
        this.inputversion = inputVersion;
    }

    public Source getSource() {
        return source;
    }

    public RunnerLog source(Source source) {
        this.source = source;
        return this;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Question getQuestion() {
        return question;
    }

    public RunnerLog question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer getAnswer() {
        return answer;
    }

    public RunnerLog answer(Answer answer) {
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
        RunnerLog runnerLog = (RunnerLog) o;
        if (runnerLog.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), runnerLog.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RunnerLog{" +
            "id=" + getId() +
            "}";
    }
}
