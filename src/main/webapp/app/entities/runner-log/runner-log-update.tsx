import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IApiVersion } from 'app/shared/model/api-version.model';
import { getEntities as getApiVersions } from 'app/entities/api-version/api-version.reducer';
import { IDataVersion } from 'app/shared/model/data-version.model';
import { getEntities as getDataVersions } from 'app/entities/data-version/data-version.reducer';
import { IInputVersion } from 'app/shared/model/input-version.model';
import { getEntities as getInputVersions } from 'app/entities/input-version/input-version.reducer';
import { ISource } from 'app/shared/model/source.model';
import { getEntities as getSources } from 'app/entities/source/source.reducer';
import { IQuestion } from 'app/shared/model/question.model';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { IAnswer } from 'app/shared/model/answer.model';
import { getEntities as getAnswers } from 'app/entities/answer/answer.reducer';
import { getEntity, updateEntity, createEntity, reset } from './runner-log.reducer';
import { IRunnerLog } from 'app/shared/model/runner-log.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IRunnerLogUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IRunnerLogUpdateState {
  isNew: boolean;
  apiversionId: string;
  dataversionId: string;
  inputversionId: string;
  sourceId: string;
  questionId: string;
  answerId: string;
}

export class RunnerLogUpdate extends React.Component<IRunnerLogUpdateProps, IRunnerLogUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      apiversionId: '0',
      dataversionId: '0',
      inputversionId: '0',
      sourceId: '0',
      questionId: '0',
      answerId: '0',
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentWillUpdate(nextProps, nextState) {
    if (nextProps.updateSuccess !== this.props.updateSuccess && nextProps.updateSuccess) {
      this.handleClose();
    }
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }

    this.props.getApiVersions();
    this.props.getDataVersions();
    this.props.getInputVersions();
    this.props.getSources();
    this.props.getQuestions();
    this.props.getAnswers();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { runnerLogEntity } = this.props;
      const entity = {
        ...runnerLogEntity,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/runner-log');
  };

  render() {
    const { runnerLogEntity, apiVersions, dataVersions, inputVersions, sources, questions, answers, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ecoToolApp.runnerLog.home.createOrEditLabel">Create or edit a RunnerLog</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : runnerLogEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="runner-log-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="apiversion.version">Apiversion</Label>
                  <AvInput id="runner-log-apiversion" type="select" className="form-control" name="apiversionId">
                    <option value="" key="0" />
                    {apiVersions
                      ? apiVersions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.version}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="dataversion.version">Dataversion</Label>
                  <AvInput id="runner-log-dataversion" type="select" className="form-control" name="dataversionId">
                    <option value="" key="0" />
                    {dataVersions
                      ? dataVersions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.version}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="inputversion.version">Inputversion</Label>
                  <AvInput id="runner-log-inputversion" type="select" className="form-control" name="inputversionId">
                    <option value="" key="0" />
                    {inputVersions
                      ? inputVersions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.version}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="source.path">Source</Label>
                  <AvInput id="runner-log-source" type="select" className="form-control" name="sourceId">
                    <option value="" key="0" />
                    {sources
                      ? sources.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.path}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="question.question_text">Question</Label>
                  <AvInput id="runner-log-question" type="select" className="form-control" name="questionId">
                    <option value="" key="0" />
                    {questions
                      ? questions.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.question_text}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <AvGroup>
                  <Label for="answer.answer_text">Answer</Label>
                  <AvInput id="runner-log-answer" type="select" className="form-control" name="answerId">
                    <option value="" key="0" />
                    {answers
                      ? answers.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.answer_text}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/runner-log" replace color="info">
                  <FontAwesomeIcon icon="arrow-left" />
                  &nbsp;
                  <span className="d-none d-md-inline">Back</span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                  <FontAwesomeIcon icon="save" />
                  &nbsp; Save
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = (storeState: IRootState) => ({
  apiVersions: storeState.apiVersion.entities,
  dataVersions: storeState.dataVersion.entities,
  inputVersions: storeState.inputVersion.entities,
  sources: storeState.source.entities,
  questions: storeState.question.entities,
  answers: storeState.answer.entities,
  runnerLogEntity: storeState.runnerLog.entity,
  loading: storeState.runnerLog.loading,
  updating: storeState.runnerLog.updating,
  updateSuccess: storeState.runnerLog.updateSuccess
});

const mapDispatchToProps = {
  getApiVersions,
  getDataVersions,
  getInputVersions,
  getSources,
  getQuestions,
  getAnswers,
  getEntity,
  updateEntity,
  createEntity,
  reset
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RunnerLogUpdate);
