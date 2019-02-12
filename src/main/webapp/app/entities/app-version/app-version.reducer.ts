import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IAppVersion, defaultValue } from 'app/shared/model/app-version.model';

export const ACTION_TYPES = {
  FETCH_APPVERSION_LIST: 'appVersion/FETCH_APPVERSION_LIST',
  FETCH_APPVERSION: 'appVersion/FETCH_APPVERSION',
  CREATE_APPVERSION: 'appVersion/CREATE_APPVERSION',
  UPDATE_APPVERSION: 'appVersion/UPDATE_APPVERSION',
  DELETE_APPVERSION: 'appVersion/DELETE_APPVERSION',
  RESET: 'appVersion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IAppVersion>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type AppVersionState = Readonly<typeof initialState>;

// Reducer

export default (state: AppVersionState = initialState, action): AppVersionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APPVERSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_APPVERSION):
    case REQUEST(ACTION_TYPES.UPDATE_APPVERSION):
    case REQUEST(ACTION_TYPES.DELETE_APPVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_APPVERSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPVERSION):
    case FAILURE(ACTION_TYPES.CREATE_APPVERSION):
    case FAILURE(ACTION_TYPES.UPDATE_APPVERSION):
    case FAILURE(ACTION_TYPES.DELETE_APPVERSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPVERSION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPVERSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPVERSION):
    case SUCCESS(ACTION_TYPES.UPDATE_APPVERSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPVERSION):
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

const apiUrl = 'api/app-versions';

// Actions

export const getEntities: ICrudGetAllAction<IAppVersion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_APPVERSION_LIST,
    payload: axios.get<IAppVersion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IAppVersion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APPVERSION,
    payload: axios.get<IAppVersion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IAppVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APPVERSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IAppVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APPVERSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IAppVersion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APPVERSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
