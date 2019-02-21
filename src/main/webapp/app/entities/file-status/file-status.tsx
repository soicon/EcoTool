import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import {
  Button,
  Col,
  Row,
  Table,
  Dropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Form,
  FormGroup,
  Label,
  Input
} from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { FilePond } from 'react-filepond';
import 'filepond/dist/filepond.min.css';
import { ICrudGetAllAction, getSortState, IPaginationBaseState, getPaginationItemsNumber, JhiPagination } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './file-status.reducer';
import { IFileStatus } from 'app/shared/model/file-status.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';
import { getDataEntities } from '../data-version/data-version.reducer';
import { getApiEntities } from '../api-version/api-version.reducer';
import { getInputEntities } from '../input-version/input-version.reducer';
import { processingEntity } from '../source/source.reducer';

export interface IFileStatusProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IFileStatusState = IPaginationBaseState;

export class FileStatus extends React.Component<IFileStatusProps, IFileStatusState> {
  state = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE),
    modal: false,
    dataver: '22.1',
    apiver: '22.1',
    inputver: '22.1',
    fileName: ''
  };
  componentWillMount(): void {
    this.getDataVerEntities();
  }

  componentDidMount() {
    this.getEntities();
  }

  getDataVerEntities() {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getDataEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  }

  sort = prop => () => {
    this.setState(
      {
        order: this.state.order === 'asc' ? 'desc' : 'asc',
        sort: prop
      },
      () => this.sortEntities()
    );
  };

  toggle = filename => {
    this.setState(prevState => ({
      ...prevState,
      modal: !this.state.modal,
      fileName: filename
    }));
  };

  processing = () => {
    const { dataver, fileName } = this.state;
    this.props.processingEntity(fileName, dataver);
    window.location.reload();
  };

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  renderStatus = status => {
    if (status === 0) {
      return (
        <div style={{ backgroundColor: 'gray', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <p style={{ color: 'white' }}>Chưa xử lý</p>
        </div>
      );
    } else if (status == 1) {
      return (
        <div style={{ backgroundColor: 'yellow', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <p style={{ color: 'white' }}>Đang xử lý</p>
        </div>
      );
    } else if (status == 3) {
      return (
        <div style={{ backgroundColor: 'red', display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
          <p style={{ color: 'white' }}> Lỗi </p>
        </div>
      );
    } else {
      return (
        <div style={{ backgroundColor: 'green', display: 'flex', alignContent: 'center', justifyContent: 'center', alignItems: 'center' }}>
          <p style={{ color: 'white' }}>Đã xử lý xong</p>
        </div>
      );
    }
  };

  renderButton = (match, fileStatus, fileName) => {
    if (fileStatus.status == 0) {
      return (
        <Button onClick={() => this.toggle(fileName)} color="info" size="sm">
          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Xử Lý</span>
        </Button>
      );
    } else if (fileStatus.status == 1 || fileStatus.status == 3) {
      return <div />;
    } else {
      return (
        <Button
          onClick={() => {
            setTimeout(() => {
              const response = {
                file: `/api/downloadFile/${fileStatus.result}`
              };
              // server sent the url to the file!
              // now, let's download:
              window.open(response.file);
              // you could also do:
              // window.location.href = response.file;
            }, 100);
          }}
          color="primary"
          size="sm"
        >
          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">Download</span>
        </Button>
      );
    }
  };
  handleChange = event => {
    const value = event.currentTarget.value;
    const att = event.currentTarget.name;
    this.setState(prevState => ({
      ...prevState,
      [att]: value
    }));
  };
  render() {
    const { fileStatusList, match, totalItems, dataList } = this.props;

    const optionDataItems = dataList.map(planet => (
      <option key={planet.version} value={planet.version}>
        {planet.versionInfo}
      </option>
    ));

    return (
      <div>
        <Modal isOpen={this.state.modal} toggle={this.toggle}>
          <ModalHeader toggle={this.toggle}>Modal title</ModalHeader>
          <ModalBody>
            <Form>
              <FormGroup>
                <Label for="exampleSelect">Data Version</Label>
                <Input type="select" name="dataver" id="exampleSelect" onChange={this.handleChange}>
                  {optionDataItems}
                </Input>
              </FormGroup>
            </Form>
          </ModalBody>
          <ModalFooter>
            <Button color="primary" onClick={this.processing}>
              {' '}
              Xử Lý
            </Button>{' '}
            <Button
              color="secondary"
              onClick={() => {
                this.setState(prevState => ({ ...prevState, modal: false }));
              }}
            >
              Cancel
            </Button>
          </ModalFooter>
        </Modal>
        <h2 id="file-status-heading">Upload File</h2>
        <FilePond
          server="/api/upload-file"
          onerror={response => {
            let header = response.headers.split('\n')[0];
            const error = header.split(':')[1];
            console.log(response.headers);
            alert(error);
          }}
        />

        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  Ngày tháng <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('name')}>
                  Tên File <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('url')}>
                  Link Tải <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('result')}>
                  Tên File Kết Quả
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('status')}>
                  Trạng thái <FontAwesomeIcon icon="sort" />
                </th>
                <th className="hand" onClick={this.sort('download_result_url')}>
                  Thông tin phiên bản <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {fileStatusList.map((fileStatus, i) => (
                <tr key={`entity-${i}`}>
                  <td>{fileStatus.createdDate}</td>
                  <td>{fileStatus.name}</td>
                  <td>{fileStatus.url}</td>
                  <td>{fileStatus.result}</td>
                  <td>{this.renderStatus(fileStatus.status)}</td>
                  <td>{fileStatus.versionInfo}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      {this.renderButton(match, fileStatus, fileStatus.name)}
                      <Button tag={Link} to={`${match.url}/${fileStatus.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline"> Xóa</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        </div>
        <Row className="justify-content-center">
          <JhiPagination
            items={getPaginationItemsNumber(totalItems, this.state.itemsPerPage)}
            activePage={this.state.activePage}
            onSelect={this.handlePagination}
            maxButtons={5}
          />
        </Row>
      </div>
    );
  }
}

const mapStateToProps = ({ fileStatus, dataVersion, inputVersion, apiVersion }: IRootState) => ({
  fileStatusList: fileStatus.entities,
  totalItems: fileStatus.totalItems,
  dataList: dataVersion.entities,
  apiList: apiVersion.entities,
  inputList: inputVersion.entities
});

const mapDispatchToProps = {
  getEntities,
  getDataEntities,
  getApiEntities,
  getInputEntities,
  processingEntity
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(FileStatus);
