import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import source, {
  SourceState
} from 'app/entities/source/source.reducer';
// prettier-ignore
import question, {
  QuestionState
} from 'app/entities/question/question.reducer';
// prettier-ignore
import answer, {
  AnswerState
} from 'app/entities/answer/answer.reducer';
// prettier-ignore
import appVersion, {
  AppVersionState
} from 'app/entities/app-version/app-version.reducer';
// prettier-ignore
import apiVersion, {
  ApiVersionState
} from 'app/entities/api-version/api-version.reducer';
// prettier-ignore
import inputVersion, {
  InputVersionState
} from 'app/entities/input-version/input-version.reducer';
// prettier-ignore
import dataVersion, {
  DataVersionState
} from 'app/entities/data-version/data-version.reducer';
// prettier-ignore
import runnerLog, {
  RunnerLogState
} from 'app/entities/runner-log/runner-log.reducer';
// prettier-ignore
import fileStatus, {
  FileStatusState
} from 'app/entities/file-status/file-status.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly source: SourceState;
  readonly question: QuestionState;
  readonly answer: AnswerState;
  readonly appVersion: AppVersionState;
  readonly apiVersion: ApiVersionState;
  readonly inputVersion: InputVersionState;
  readonly dataVersion: DataVersionState;
  readonly runnerLog: RunnerLogState;
  readonly fileStatus: FileStatusState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  source,
  question,
  answer,
  appVersion,
  apiVersion,
  inputVersion,
  dataVersion,
  runnerLog,
  fileStatus,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
