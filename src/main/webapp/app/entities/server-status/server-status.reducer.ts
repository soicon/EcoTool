import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IServerStatus, defaultValue } from 'app/shared/model/server-status.model';

export const ACTION_TYPES = {
  FETCH_SERVERSTATUS_LIST: 'serverStatus/FETCH_SERVERSTATUS_LIST',
  FETCH_SERVERSTATUS: 'serverStatus/FETCH_SERVERSTATUS',
  CREATE_SERVERSTATUS: 'serverStatus/CREATE_SERVERSTATUS',
  UPDATE_SERVERSTATUS: 'serverStatus/UPDATE_SERVERSTATUS',
  DELETE_SERVERSTATUS: 'serverStatus/DELETE_SERVERSTATUS',
  RESET: 'serverStatus/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IServerStatus>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type ServerStatusState = Readonly<typeof initialState>;

// Reducer

export default (state: ServerStatusState = initialState, action): ServerStatusState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SERVERSTATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SERVERSTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_SERVERSTATUS):
    case REQUEST(ACTION_TYPES.UPDATE_SERVERSTATUS):
    case REQUEST(ACTION_TYPES.DELETE_SERVERSTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_SERVERSTATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SERVERSTATUS):
    case FAILURE(ACTION_TYPES.CREATE_SERVERSTATUS):
    case FAILURE(ACTION_TYPES.UPDATE_SERVERSTATUS):
    case FAILURE(ACTION_TYPES.DELETE_SERVERSTATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVERSTATUS_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_SERVERSTATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_SERVERSTATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_SERVERSTATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_SERVERSTATUS):
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

const apiUrl = 'api/server-statuses';

// Actions

export const getEntities: ICrudGetAllAction<IServerStatus> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SERVERSTATUS_LIST,
  payload: axios.get<IServerStatus>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IServerStatus> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SERVERSTATUS,
    payload: axios.get<IServerStatus>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IServerStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SERVERSTATUS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IServerStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SERVERSTATUS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IServerStatus> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SERVERSTATUS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
