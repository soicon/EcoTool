import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import DataPrefix from './data-prefix';
import DataPrefixDetail from './data-prefix-detail';
import DataPrefixUpdate from './data-prefix-update';
import DataPrefixDeleteDialog from './data-prefix-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DataPrefixUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DataPrefixUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DataPrefixDetail} />
      <ErrorBoundaryRoute path={match.url} component={DataPrefix} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DataPrefixDeleteDialog} />
  </>
);

export default Routes;
