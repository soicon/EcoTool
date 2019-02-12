import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './data-version.reducer';
import { IDataVersion } from 'app/shared/model/data-version.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IDataVersionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class DataVersionDetail extends React.Component<IDataVersionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { dataVersionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            DataVersion [<b>{dataVersionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="version">Version</span>
            </dt>
            <dd>{dataVersionEntity.version}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{dataVersionEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/data-version" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/data-version/${dataVersionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ dataVersion }: IRootState) => ({
  dataVersionEntity: dataVersion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(DataVersionDetail);
