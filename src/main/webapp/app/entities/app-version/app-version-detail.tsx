import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './app-version.reducer';
import { IAppVersion } from 'app/shared/model/app-version.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IAppVersionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class AppVersionDetail extends React.Component<IAppVersionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { appVersionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            AppVersion [<b>{appVersionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="apiVer">Api Ver</span>
            </dt>
            <dd>{appVersionEntity.apiVer}</dd>
            <dt>
              <span id="dataVer">Data Ver</span>
            </dt>
            <dd>{appVersionEntity.dataVer}</dd>
            <dt>
              <span id="inputVer">Input Ver</span>
            </dt>
            <dd>{appVersionEntity.inputVer}</dd>
          </dl>
          <Button tag={Link} to="/entity/app-version" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/app-version/${appVersionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ appVersion }: IRootState) => ({
  appVersionEntity: appVersion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(AppVersionDetail);
