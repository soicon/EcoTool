import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IDataVersion, defaultValue } from 'app/shared/model/data-version.model';

export const ACTION_TYPES = {
  FETCH_DATAVERSION_LIST: 'dataVersion/FETCH_DATAVERSION_LIST',
  FETCH_DATAVERSION: 'dataVersion/FETCH_DATAVERSION',
  CREATE_DATAVERSION: 'dataVersion/CREATE_DATAVERSION',
  UPDATE_DATAVERSION: 'dataVersion/UPDATE_DATAVERSION',
  DELETE_DATAVERSION: 'dataVersion/DELETE_DATAVERSION',
  RESET: 'dataVersion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IDataVersion>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type DataVersionState = Readonly<typeof initialState>;

// Reducer

export default (state: DataVersionState = initialState, action): DataVersionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_DATAVERSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_DATAVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_DATAVERSION):
    case REQUEST(ACTION_TYPES.UPDATE_DATAVERSION):
    case REQUEST(ACTION_TYPES.DELETE_DATAVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_DATAVERSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_DATAVERSION):
    case FAILURE(ACTION_TYPES.CREATE_DATAVERSION):
    case FAILURE(ACTION_TYPES.UPDATE_DATAVERSION):
    case FAILURE(ACTION_TYPES.DELETE_DATAVERSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATAVERSION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_DATAVERSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_DATAVERSION):
    case SUCCESS(ACTION_TYPES.UPDATE_DATAVERSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_DATAVERSION):
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

const apiUrl = 'api/data-versions';

// Actions

export const getEntities: ICrudGetAllAction<IDataVersion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_DATAVERSION_LIST,
    payload: axios.get<IDataVersion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IDataVersion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_DATAVERSION,
    payload: axios.get<IDataVersion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IDataVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_DATAVERSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IDataVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_DATAVERSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IDataVersion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_DATAVERSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
