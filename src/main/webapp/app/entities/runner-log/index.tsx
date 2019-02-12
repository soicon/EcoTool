import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import RunnerLog from './runner-log';
import RunnerLogDetail from './runner-log-detail';
import RunnerLogUpdate from './runner-log-update';
import RunnerLogDeleteDialog from './runner-log-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={RunnerLogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={RunnerLogUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={RunnerLogDetail} />
      <ErrorBoundaryRoute path={match.url} component={RunnerLog} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={RunnerLogDeleteDialog} />
  </>
);

export default Routes;
