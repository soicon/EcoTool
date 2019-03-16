import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDataPrefix, defaultValue } from 'app/shared/model/data-prefix.model';

export const ACTION_TYPES = {
  FETCH_DATAPREFIX_LIST: 'dataPrefix/FETCH_DATAPREFIX_LIST',
  FETCH_DATAPREFIX: 'dataPrefix/FETCH_DATAPREFIX',
  CREATE_DATAPREFIX: 'dataPrefix/CREATE_DATAPREFIX',
  UPDATE_DATAPREFIX: 'dataPrefix/UPDATE_DATAPREFIX',
  DELETE_DATAPREFIX: 'dataPrefix/DELETE_DATAPREFIX',
  RESET: 'dataPrefix/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDataPrefix>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DataPrefixState = Readonly<typeof initialState>;

// Reducer

export default (state: DataPrefixState = initialState, action): DataPrefixState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DATAPREFIX_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DATAPREFIX):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DATAPREFIX):
    case REQUEST(ACTION_TYPES.UPDATE_DATAPREFIX):
    case REQUEST(ACTION_TYPES.DELETE_DATAPREFIX):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DATAPREFIX_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DATAPREFIX):
    case FAILURE(ACTION_TYPES.CREATE_DATAPREFIX):
    case FAILURE(ACTION_TYPES.UPDATE_DATAPREFIX):
    case FAILURE(ACTION_TYPES.DELETE_DATAPREFIX):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATAPREFIX_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATAPREFIX):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DATAPREFIX):
    case SUCCESS(ACTION_TYPES.UPDATE_DATAPREFIX):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DATAPREFIX):
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

const apiUrl = 'api/data-prefixes';

// Actions

export const getEntities: ICrudGetAllAction<IDataPrefix> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DATAPREFIX_LIST,
    payload: axios.get<IDataPrefix>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDataPrefix> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DATAPREFIX,
    payload: axios.get<IDataPrefix>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDataPrefix> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DATAPREFIX,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDataPrefix> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DATAPREFIX,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDataPrefix> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DATAPREFIX,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
