import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './input-version.reducer';
import { IInputVersion } from 'app/shared/model/input-version.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IInputVersionDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class InputVersionDetail extends React.Component<IInputVersionDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { inputVersionEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            InputVersion [<b>{inputVersionEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="version">Version</span>
            </dt>
            <dd>{inputVersionEntity.version}</dd>
            <dt>
              <span id="description">Description</span>
            </dt>
            <dd>{inputVersionEntity.description}</dd>
          </dl>
          <Button tag={Link} to="/entity/input-version" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/input-version/${inputVersionEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ inputVersion }: IRootState) => ({
  inputVersionEntity: inputVersion.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(InputVersionDetail);
