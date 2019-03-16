import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ICorePrefix, defaultValue } from 'app/shared/model/core-prefix.model';

export const ACTION_TYPES = {
  FETCH_COREPREFIX_LIST: 'corePrefix/FETCH_COREPREFIX_LIST',
  FETCH_COREPREFIX: 'corePrefix/FETCH_COREPREFIX',
  CREATE_COREPREFIX: 'corePrefix/CREATE_COREPREFIX',
  UPDATE_COREPREFIX: 'corePrefix/UPDATE_COREPREFIX',
  DELETE_COREPREFIX: 'corePrefix/DELETE_COREPREFIX',
  RESET: 'corePrefix/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ICorePrefix>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type CorePrefixState = Readonly<typeof initialState>;

// Reducer

export default (state: CorePrefixState = initialState, action): CorePrefixState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_COREPREFIX_LIST):
    case REQUEST(ACTION_TYPES.FETCH_COREPREFIX):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_COREPREFIX):
    case REQUEST(ACTION_TYPES.UPDATE_COREPREFIX):
    case REQUEST(ACTION_TYPES.DELETE_COREPREFIX):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_COREPREFIX_LIST):
    case FAILURE(ACTION_TYPES.FETCH_COREPREFIX):
    case FAILURE(ACTION_TYPES.CREATE_COREPREFIX):
    case FAILURE(ACTION_TYPES.UPDATE_COREPREFIX):
    case FAILURE(ACTION_TYPES.DELETE_COREPREFIX):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_COREPREFIX_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_COREPREFIX):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_COREPREFIX):
    case SUCCESS(ACTION_TYPES.UPDATE_COREPREFIX):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_COREPREFIX):
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

const apiUrl = 'api/core-prefixes';

// Actions

export const getCoreEntities: ICrudGetAllAction<ICorePrefix> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_COREPREFIX_LIST,
    payload: axios.get<ICorePrefix>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<ICorePrefix> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_COREPREFIX,
    payload: axios.get<ICorePrefix>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<ICorePrefix> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_COREPREFIX,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getCoreEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ICorePrefix> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_COREPREFIX,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getCoreEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<ICorePrefix> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_COREPREFIX,
    payload: axios.delete(requestUrl)
  });
  dispatch(getCoreEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
