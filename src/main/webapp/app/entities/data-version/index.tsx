import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DataVersion from './data-version';
import DataVersionDetail from './data-version-detail';
import DataVersionUpdate from './data-version-update';
import DataVersionDeleteDialog from './data-version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DataVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DataVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DataVersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={DataVersion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DataVersionDeleteDialog} />
  </>
);

export default Routes;
