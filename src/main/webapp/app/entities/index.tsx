import React from 'react';
import { Switch } from 'react-router-dom';

// tslint:disable-next-line:no-unused-variable
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Source from './source';
import Question from './question';
import Answer from './answer';
import AppVersion from './app-version';
import ApiVersion from './api-version';
import InputVersion from './input-version';
import DataVersion from './data-version';
import RunnerLog from './runner-log';
import FileStatus from './file-status';
import ServerStatus from './server-status';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}/source`} component={Source} />
      <ErrorBoundaryRoute path={`${match.url}/question`} component={Question} />
      <ErrorBoundaryRoute path={`${match.url}/answer`} component={Answer} />
      <ErrorBoundaryRoute path={`${match.url}/app-version`} component={AppVersion} />
      <ErrorBoundaryRoute path={`${match.url}/api-version`} component={ApiVersion} />
      <ErrorBoundaryRoute path={`${match.url}/input-version`} component={InputVersion} />
      <ErrorBoundaryRoute path={`${match.url}/data-version`} component={DataVersion} />
      <ErrorBoundaryRoute path={`${match.url}/runner-log`} component={RunnerLog} />
      <ErrorBoundaryRoute path={`${match.url}/file-status`} component={FileStatus} />
      <ErrorBoundaryRoute path={`${match.url}/server-status`} component={ServerStatus} />
      {/* jhipster-needle-add-route-path - JHipster will routes here */}
    </Switch>
  </div>
);

export default Routes;
