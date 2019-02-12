import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IRunnerLog, defaultValue } from 'app/shared/model/runner-log.model';

export const ACTION_TYPES = {
  FETCH_RUNNERLOG_LIST: 'runnerLog/FETCH_RUNNERLOG_LIST',
  FETCH_RUNNERLOG: 'runnerLog/FETCH_RUNNERLOG',
  CREATE_RUNNERLOG: 'runnerLog/CREATE_RUNNERLOG',
  UPDATE_RUNNERLOG: 'runnerLog/UPDATE_RUNNERLOG',
  DELETE_RUNNERLOG: 'runnerLog/DELETE_RUNNERLOG',
  RESET: 'runnerLog/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IRunnerLog>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type RunnerLogState = Readonly<typeof initialState>;

// Reducer

export default (state: RunnerLogState = initialState, action): RunnerLogState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_RUNNERLOG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_RUNNERLOG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_RUNNERLOG):
    case REQUEST(ACTION_TYPES.UPDATE_RUNNERLOG):
    case REQUEST(ACTION_TYPES.DELETE_RUNNERLOG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_RUNNERLOG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_RUNNERLOG):
    case FAILURE(ACTION_TYPES.CREATE_RUNNERLOG):
    case FAILURE(ACTION_TYPES.UPDATE_RUNNERLOG):
    case FAILURE(ACTION_TYPES.DELETE_RUNNERLOG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_RUNNERLOG_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_RUNNERLOG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_RUNNERLOG):
    case SUCCESS(ACTION_TYPES.UPDATE_RUNNERLOG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_RUNNERLOG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/runner-logs';

// Actions

export const getEntities: ICrudGetAllAction<IRunnerLog> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_RUNNERLOG_LIST,
    payload: axios.get<IRunnerLog>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IRunnerLog> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_RUNNERLOG,
    payload: axios.get<IRunnerLog>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IRunnerLog> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_RUNNERLOG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IRunnerLog> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_RUNNERLOG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IRunnerLog> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_RUNNERLOG,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
