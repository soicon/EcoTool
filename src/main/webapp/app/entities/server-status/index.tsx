import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ServerStatus from './server-status';
import ServerStatusDetail from './server-status-detail';
import ServerStatusUpdate from './server-status-update';
import ServerStatusDeleteDialog from './server-status-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ServerStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ServerStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ServerStatusDetail} />
      <ErrorBoundaryRoute path={match.url} component={ServerStatus} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ServerStatusDeleteDialog} />
  </>
);

export default Routes;
