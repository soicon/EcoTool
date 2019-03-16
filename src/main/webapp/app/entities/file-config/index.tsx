import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import FileConfig from './file-config';
import FileConfigDetail from './file-config-detail';
import FileConfigUpdate from './file-config-update';
import FileConfigDeleteDialog from './file-config-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FileConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FileConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FileConfigDetail} />
      <ErrorBoundaryRoute path={match.url} component={FileConfig} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FileConfigDeleteDialog} />
  </>
);

export default Routes;
