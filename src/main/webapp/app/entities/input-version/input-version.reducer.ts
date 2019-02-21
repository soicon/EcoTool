import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IInputVersion, defaultValue } from 'app/shared/model/input-version.model';

export const ACTION_TYPES = {
  FETCH_INPUTVERSION_LIST: 'inputVersion/FETCH_INPUTVERSION_LIST',
  FETCH_INPUTVERSION: 'inputVersion/FETCH_INPUTVERSION',
  CREATE_INPUTVERSION: 'inputVersion/CREATE_INPUTVERSION',
  UPDATE_INPUTVERSION: 'inputVersion/UPDATE_INPUTVERSION',
  DELETE_INPUTVERSION: 'inputVersion/DELETE_INPUTVERSION',
  RESET: 'inputVersion/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IInputVersion>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type InputVersionState = Readonly<typeof initialState>;

// Reducer

export default (state: InputVersionState = initialState, action): InputVersionState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_INPUTVERSION_LIST):
    case REQUEST(ACTION_TYPES.FETCH_INPUTVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_INPUTVERSION):
    case REQUEST(ACTION_TYPES.UPDATE_INPUTVERSION):
    case REQUEST(ACTION_TYPES.DELETE_INPUTVERSION):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_INPUTVERSION_LIST):
    case FAILURE(ACTION_TYPES.FETCH_INPUTVERSION):
    case FAILURE(ACTION_TYPES.CREATE_INPUTVERSION):
    case FAILURE(ACTION_TYPES.UPDATE_INPUTVERSION):
    case FAILURE(ACTION_TYPES.DELETE_INPUTVERSION):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_INPUTVERSION_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_INPUTVERSION):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_INPUTVERSION):
    case SUCCESS(ACTION_TYPES.UPDATE_INPUTVERSION):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_INPUTVERSION):
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

const apiUrl = 'api/input-versions';

// Actions

export const getEntities: ICrudGetAllAction<IInputVersion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INPUTVERSION_LIST,
    payload: axios.get<IInputVersion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getInputEntities: ICrudGetAllAction<IInputVersion> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_INPUTVERSION_LIST,
    payload: axios.get<IInputVersion>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IInputVersion> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_INPUTVERSION,
    payload: axios.get<IInputVersion>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IInputVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_INPUTVERSION,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IInputVersion> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_INPUTVERSION,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IInputVersion> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_INPUTVERSION,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
