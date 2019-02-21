import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IApiVersion, defaultValue } from 'app/shared/model/api-version.model';

export const ACTION_TYPES = {
  FETCH_APIVERSION_LIST: 'apiVersion/FETCH_APIVERSION_LIST',
  FETCH_APIVERSION: 'apiVersion/FETCH_APIVERSION',
  CREATE_APIVERSION: 'apiVersion/CREATE_APIVERSION',
  UPDATE_APIVERSION: 'apiVersion/UPDATE_APIVERSION',
  DELETE_APIVERSION: 'apiVersion/DELETE_APIVERSION',
  RESET: 'apiVersion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IApiVersion>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type ApiVersionState = Readonly<typeof initialState>;

// Reducer

export default (state: ApiVersionState = initialState, action): ApiVersionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APIVERSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APIVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_APIVERSION):
    case REQUEST(ACTION_TYPES.UPDATE_APIVERSION):
    case REQUEST(ACTION_TYPES.DELETE_APIVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_APIVERSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APIVERSION):
    case FAILURE(ACTION_TYPES.CREATE_APIVERSION):
    case FAILURE(ACTION_TYPES.UPDATE_APIVERSION):
    case FAILURE(ACTION_TYPES.DELETE_APIVERSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_APIVERSION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_APIVERSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_APIVERSION):
    case SUCCESS(ACTION_TYPES.UPDATE_APIVERSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_APIVERSION):
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

const apiUrl = 'api/api-versions';

// Actions

export const getEntities: ICrudGetAllAction<IApiVersion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_APIVERSION_LIST,
    payload: axios.get<IApiVersion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getApiEntities: ICrudGetAllAction<IApiVersion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_APIVERSION_LIST,
    payload: axios.get<IApiVersion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IApiVersion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APIVERSION,
    payload: axios.get<IApiVersion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IApiVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APIVERSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IApiVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APIVERSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IApiVersion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APIVERSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
