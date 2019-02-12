import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './file-status.reducer';
import { IFileStatus } from 'app/shared/model/file-status.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IFileStatusDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export class FileStatusDetail extends React.Component<IFileStatusDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { fileStatusEntity } = this.props;
    return (
      <Row>
        <Col md="8">
          <h2>
            FileStatus [<b>{fileStatusEntity.id}</b>]
          </h2>
          <dl className="jh-entity-details">
            <dt>
              <span id="name">Name</span>
            </dt>
            <dd>{fileStatusEntity.name}</dd>
            <dt>
              <span id="url">Url</span>
            </dt>
            <dd>{fileStatusEntity.url}</dd>
            <dt>
              <span id="result">Result</span>
            </dt>
            <dd>{fileStatusEntity.result}</dd>
            <dt>
              <span id="status">Status</span>
            </dt>
            <dd>{fileStatusEntity.status}</dd>
            <dt>
              <span id="download_result_url">Download Result Url</span>
            </dt>
            <dd>{fileStatusEntity.download_result_url}</dd>
            <dt>
              <span id="fileType">File Type</span>
            </dt>
            <dd>{fileStatusEntity.fileType}</dd>
          </dl>
          <Button tag={Link} to="/entity/file-status" replace color="info">
            <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
          </Button>
          &nbsp;
          <Button tag={Link} to={`/entity/file-status/${fileStatusEntity.id}/edit`} replace color="primary">
            <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
          </Button>
        </Col>
      </Row>
    );
  }
}

const mapStateToProps = ({ fileStatus }: IRootState) => ({
  fileStatusEntity: fileStatus.entity
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FileStatusDetail);
