import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './file-status.reducer';
import { IFileStatus } from 'app/shared/model/file-status.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IFileStatusUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IFileStatusUpdateState {
  isNew: boolean;
}

export class FileStatusUpdate extends React.Component<IFileStatusUpdateProps, IFileStatusUpdateState> {
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
      const { fileStatusEntity } = this.props;
      const entity = {
        ...fileStatusEntity,
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
    this.props.history.push('/entity/file-status');
  };

  render() {
    const { fileStatusEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ecoToolApp.fileStatus.home.createOrEditLabel">Create or edit a FileStatus</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : fileStatusEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="file-status-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    Name
                  </Label>
                  <AvField id="file-status-name" type="text" name="name" />
                </AvGroup>
                <AvGroup>
                  <Label id="urlLabel" for="url">
                    Url
                  </Label>
                  <AvField id="file-status-url" type="text" name="url" />
                </AvGroup>
                <AvGroup>
                  <Label id="resultLabel" for="result">
                    Result
                  </Label>
                  <AvField id="file-status-result" type="text" name="result" />
                </AvGroup>
                <AvGroup>
                  <Label id="statusLabel" for="status">
                    Status
                  </Label>
                  <AvField id="file-status-status" type="string" className="form-control" name="status" />
                </AvGroup>
                <AvGroup>
                  <Label id="download_result_urlLabel" for="download_result_url">
                    Download Result Url
                  </Label>
                  <AvField id="file-status-download_result_url" type="text" name="download_result_url" />
                </AvGroup>
                <AvGroup>
                  <Label id="fileTypeLabel" for="fileType">
                    File Type
                  </Label>
                  <AvField id="file-status-fileType" type="text" name="fileType" />
                </AvGroup>
                <AvGroup>
                  <Label id="versionInfoLabel" for="versionInfo">
                    Version Info
                  </Label>
                  <AvField id="file-status-versionInfo" type="text" name="versionInfo" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/file-status" replace color="info">
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
  fileStatusEntity: storeState.fileStatus.entity,
  loading: storeState.fileStatus.loading,
  updating: storeState.fileStatus.updating,
  updateSuccess: storeState.fileStatus.updateSuccess
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
)(FileStatusUpdate);
