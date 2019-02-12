import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AppVersion from './app-version';
import AppVersionDetail from './app-version-detail';
import AppVersionUpdate from './app-version-update';
import AppVersionDeleteDialog from './app-version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AppVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AppVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AppVersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={AppVersion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={AppVersionDeleteDialog} />
  </>
);

export default Routes;
