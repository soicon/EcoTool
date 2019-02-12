import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FileStatus from './file-status';
import FileStatusDetail from './file-status-detail';
import FileStatusUpdate from './file-status-update';
import FileStatusDeleteDialog from './file-status-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FileStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FileStatusUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FileStatusDetail} />
      <ErrorBoundaryRoute path={match.url} component={FileStatus} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FileStatusDeleteDialog} />
  </>
);

export default Routes;
