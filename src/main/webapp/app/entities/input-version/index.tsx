import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import InputVersion from './input-version';
import InputVersionDetail from './input-version-detail';
import InputVersionUpdate from './input-version-update';
import InputVersionDeleteDialog from './input-version-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InputVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InputVersionUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InputVersionDetail} />
      <ErrorBoundaryRoute path={match.url} component={InputVersion} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={InputVersionDeleteDialog} />
  </>
);

export default Routes;
