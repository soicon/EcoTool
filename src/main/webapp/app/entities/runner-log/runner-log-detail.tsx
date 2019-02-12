import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './runner-log.reducer';
import { IRunnerLog } from 'app/shared/model/runner-log.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IRunnerLogDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class RunnerLogDetail extends React.Component<IRunnerLogDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { runnerLogEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            RunnerLog [<b>{runnerLogEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>Apiversion</dt>
            <dd>{runnerLogEntity.apiversionVersion ? runnerLogEntity.apiversionVersion : ''}</dd>
            <dt>Dataversion</dt>
            <dd>{runnerLogEntity.dataversionVersion ? runnerLogEntity.dataversionVersion : ''}</dd>
            <dt>Inputversion</dt>
            <dd>{runnerLogEntity.inputversionVersion ? runnerLogEntity.inputversionVersion : ''}</dd>
            <dt>Source</dt>
            <dd>{runnerLogEntity.sourcePath ? runnerLogEntity.sourcePath : ''}</dd>
            <dt>Question</dt>
            <dd>{runnerLogEntity.questionQuestion_text ? runnerLogEntity.questionQuestion_text : ''}</dd>
            <dt>Answer</dt>
            <dd>{runnerLogEntity.answerAnswer_text ? runnerLogEntity.answerAnswer_text : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/runner-log" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/runner-log/${runnerLogEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ runnerLog }: IRootState) => ({
  runnerLogEntity: runnerLog.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RunnerLogDetail);
