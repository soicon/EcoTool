import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './question.reducer';
import { IQuestion } from 'app/shared/model/question.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IQuestionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class QuestionDetail extends React.Component<IQuestionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { questionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            Question [<b>{questionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="question_text">Question Text</span>
            </dt>
            <dd>{questionEntity.question_text}</dd>
            <dt>
              <span id="visible">Visible</span>
            </dt>
            <dd>{questionEntity.visible}</dd>
            <dt>Source</dt>
            <dd>{questionEntity.sourceId ? questionEntity.sourceId : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/question" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/question/${questionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ question }: IRootState) => ({
  questionEntity: question.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(QuestionDetail);
