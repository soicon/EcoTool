import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './server-status.reducer';
import { IServerStatus } from 'app/shared/model/server-status.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IServerStatusDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ServerStatusDetail extends React.Component<IServerStatusDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { serverStatusEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ServerStatus [<b>{serverStatusEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="address">Address</span>
            </dt>
            <dd>{serverStatusEntity.address}</dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{serverStatusEntity.status}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{serverStatusEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/server-status" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/server-status/${serverStatusEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ serverStatus }: IRootState) => ({
  serverStatusEntity: serverStatus.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ServerStatusDetail);
