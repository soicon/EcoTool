import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import CorePrefix from './core-prefix';
import CorePrefixDetail from './core-prefix-detail';
import CorePrefixUpdate from './core-prefix-update';
import CorePrefixDeleteDialog from './core-prefix-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={CorePrefixUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={CorePrefixUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={CorePrefixDetail} />
      <ErrorBoundaryRoute path={match.url} component={CorePrefix} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={CorePrefixDeleteDialog} />
  </>
);

export default Routes;
