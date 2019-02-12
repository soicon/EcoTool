import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ApiVersion from './api-version';
import ApiVersionDetail from './api-version-detail';
import ApiVersionUpdate from './api-version-update';
import ApiVersionDeleteDialog from './api-version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ApiVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ApiVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ApiVersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={ApiVersion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ApiVersionDeleteDialog} />
  </>
);

export default Routes;
