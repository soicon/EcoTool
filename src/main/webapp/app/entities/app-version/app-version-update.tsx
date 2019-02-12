import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './app-version.reducer';
import { IAppVersion } from 'app/shared/model/app-version.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IAppVersionUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export interface IAppVersionUpdateState {
  isNew: boolean;
}

export class AppVersionUpdate extends React.Component<IAppVersionUpdateProps, IAppVersionUpdateState> {
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
      const { appVersionEntity } = this.props;
      const entity = {
        ...appVersionEntity,
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
    this.props.history.push('/entity/app-version');
  };

  render() {
    const { appVersionEntity, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="ecoToolApp.appVersion.home.createOrEditLabel">Create or edit a AppVersion</h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : appVersionEntity} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">ID</Label>
                    <AvInput id="app-version-id" type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="apiVerLabel" for="apiVer">
                    Api Ver
                  </Label>
                  <AvField id="app-version-apiVer" type="text" name="apiVer" />
                </AvGroup>
                <AvGroup>
                  <Label id="dataVerLabel" for="dataVer">
                    Data Ver
                  </Label>
                  <AvField id="app-version-dataVer" type="text" name="dataVer" />
                </AvGroup>
                <AvGroup>
                  <Label id="inputVerLabel" for="inputVer">
                    Input Ver
                  </Label>
                  <AvField id="app-version-inputVer" type="text" name="inputVer" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/app-version" replace color="info">
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
  appVersionEntity: storeState.appVersion.entity,
  loading: storeState.appVersion.loading,
  updating: storeState.appVersion.updating,
  updateSuccess: storeState.appVersion.updateSuccess
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
)(AppVersionUpdate);
