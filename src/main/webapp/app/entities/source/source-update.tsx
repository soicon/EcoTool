import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './source.reducer';
import { ISource } from 'app/shared/model/source.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISourceUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface ISourceUpdateState {
  isNew: boolean;
}

export class SourceUpdate extends React.Component<ISourceUpdateProps, ISourceUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
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
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { sourceEntity } = this.props;
      const entity = {
        ...sourceEntity,
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
    this.props.history.push('/entity/source');
  };

  render() {
    const { sourceEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ecoToolApp.source.home.createOrEditLabel">Create or edit a Source</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : sourceEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="source-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="pathLabel" for="path">
                    Path
                  </Label>
                  <AvField id="source-path" type="text" name="path" />
                </AvGroup>
                <AvGroup>
                  <Label id="device_idLabel" for="device_id">
                    Device Id
                  </Label>
                  <AvField id="source-device_id" type="text" name="device_id" />
                </AvGroup>
                <AvGroup>
                  <Label id="typeLabel" for="type">
                    Type
                  </Label>
                  <AvField id="source-type" type="string" className="form-control" name="type" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="status">
                    Status
                  </Label>
                  <AvField id="source-status" type="string" className="form-control" name="status" />
                </AvGroup>
                <AvGroup>
                  <Label id="need_re_answerLabel" for="need_re_answer">
                    Need Re Answer
                  </Label>
                  <AvField id="source-need_re_answer" type="string" className="form-control" name="need_re_answer" />
                </AvGroup>
                <AvGroup>
                  <Label id="question_idLabel" for="question_id">
                    Question Id
                  </Label>
                  <AvField id="source-question_id" type="string" className="form-control" name="question_id" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/source" replace color="info">
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
  sourceEntity: storeState.source.entity,
  loading: storeState.source.loading,
  updating: storeState.source.updating,
  updateSuccess: storeState.source.updateSuccess
});

const mapDispatchToProps = {
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
)(SourceUpdate);
