import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './api-version.reducer';
import { IApiVersion } from 'app/shared/model/api-version.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IApiVersionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class ApiVersionDetail extends React.Component<IApiVersionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { apiVersionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            ApiVersion [<b>{apiVersionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="version">Version</span>
            </dt>
            <dd>{apiVersionEntity.version}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{apiVersionEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/api-version" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/api-version/${apiVersionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ apiVersion }: IRootState) => ({
  apiVersionEntity: apiVersion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(ApiVersionDetail);
