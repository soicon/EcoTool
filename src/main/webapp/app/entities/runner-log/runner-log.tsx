import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { ICrudGetAllAction, getSortState, IPaginationBaseState, getPaginationItemsNumber, JhiPagination } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './runner-log.reducer';
import { IRunnerLog } from 'app/shared/model/runner-log.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ITEMS_PER_PAGE } from 'app/shared/util/pagination.constants';

export interface IRunnerLogProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export type IRunnerLogState = IPaginationBaseState;

export class RunnerLog extends React.Component<IRunnerLogProps, IRunnerLogState> {
  state: IRunnerLogState = {
    ...getSortState(this.props.location, ITEMS_PER_PAGE)
  };

  componentDidMount() {
    this.getEntities();
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

  sortEntities() {
    this.getEntities();
    this.props.history.push(`${this.props.location.pathname}?page=${this.state.activePage}&sort=${this.state.sort},${this.state.order}`);
  }

  handlePagination = activePage => this.setState({ activePage }, () => this.sortEntities());

  getEntities = () => {
    const { activePage, itemsPerPage, sort, order } = this.state;
    this.props.getEntities(activePage - 1, itemsPerPage, `${sort},${order}`);
  };

  render() {
    const { runnerLogList, match, totalItems } = this.props;
    return (
      <div>
        <h2 id="runner-log-heading">
          Runner Logs
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Runner Log
          </Link>
        </h2>
        <div className="table-responsive">
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={this.sort('id')}>
                  ID <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Apiversion <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Dataversion <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Inputversion <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Source <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Question <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  Answer <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {runnerLogList.map((runnerLog, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${runnerLog.id}`} color="link" size="sm">
                      {runnerLog.id}
                    </Button>
                  </td>
                  <td>
                    {runnerLog.apiversionVersion ? (
                      <Link to={`api-version/${runnerLog.apiversionId}`}>{runnerLog.apiversionVersion}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {runnerLog.dataversionVersion ? (
                      <Link to={`data-version/${runnerLog.dataversionId}`}>{runnerLog.dataversionVersion}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {runnerLog.inputversionVersion ? (
                      <Link to={`input-version/${runnerLog.inputversionId}`}>{runnerLog.inputversionVersion}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{runnerLog.sourcePath ? <Link to={`source/${runnerLog.sourceId}`}>{runnerLog.sourcePath}</Link> : ''}</td>
                  <td>
                    {runnerLog.questionQuestion_text ? (
                      <Link to={`question/${runnerLog.questionId}`}>{runnerLog.questionQuestion_text}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {runnerLog.answerAnswer_text ? <Link to={`answer/${runnerLog.answerId}`}>{runnerLog.answerAnswer_text}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${runnerLog.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${runnerLog.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${runnerLog.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
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

const mapStateToProps = ({ runnerLog }: IRootState) => ({
  runnerLogList: runnerLog.entities,
  totalItems: runnerLog.totalItems
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(RunnerLog);
