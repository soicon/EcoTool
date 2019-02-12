import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Source from './source';
import SourceDetail from './source-detail';
import SourceUpdate from './source-update';
import SourceDeleteDialog from './source-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SourceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SourceUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/select/:id`} component={SourceDetail} />
      <ErrorBoundaryRoute path={match.url} component={Source} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={SourceDeleteDialog} />
  </>
);

export default Routes;
