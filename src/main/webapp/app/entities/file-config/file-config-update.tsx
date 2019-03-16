import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IFileStatus } from 'app/shared/model/file-status.model';
import { getEntities as getFileStatuses } from 'app/entities/file-status/file-status.reducer';
import { getEntity, updateEntity, createEntity, reset } from './file-config.reducer';
import { IFileConfig } from 'app/shared/model/file-config.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFileConfigUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFileConfigUpdateState {
  isNew: boolean;
  fileStatusId: string;
}

export class FileConfigUpdate extends React.Component<IFileConfigUpdateProps, IFileConfigUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      fileStatusId: '0',
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

    this.props.getFileStatuses();
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { fileConfigEntity } = this.props;
      const entity = {
        ...fileConfigEntity,
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
    this.props.history.push('/entity/file-config');
  };

  render() {
    const { fileConfigEntity, fileStatuses, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ecoToolApp.fileConfig.home.createOrEditLabel">Create or edit a FileConfig</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : fileConfigEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="file-config-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label for="fileStatus.name">File Status</Label>
                  <AvInput id="file-config-fileStatus" type="select" className="form-control" name="fileStatusId">
                    <option value="" key="0" />
                    {fileStatuses
                      ? fileStatuses.map(otherEntity => (
                          <option value={otherEntity.id} key={otherEntity.id}>
                            {otherEntity.name}
                          </option>
                        ))
                      : null}
                  </AvInput>
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/file-config" replace color="info">
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
  fileStatuses: storeState.fileStatus.entities,
  fileConfigEntity: storeState.fileConfig.entity,
  loading: storeState.fileConfig.loading,
  updating: storeState.fileConfig.updating,
  updateSuccess: storeState.fileConfig.updateSuccess
});

const mapDispatchToProps = {
  getFileStatuses,
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
)(FileConfigUpdate);
