import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './file-config.reducer';
import { IFileConfig } from 'app/shared/model/file-config.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFileConfigDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FileConfigDetail extends React.Component<IFileConfigDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fileConfigEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            FileConfig [<b>{fileConfigEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>File Status</dt>
            <dd>{fileConfigEntity.fileStatusName ? fileConfigEntity.fileStatusName : ''}</dd>
          </dl>
          <Button tag={Link} to="/entity/file-config" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/file-config/${fileConfigEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ fileConfig }: IRootState) => ({
  fileConfigEntity: fileConfig.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FileConfigDetail);
