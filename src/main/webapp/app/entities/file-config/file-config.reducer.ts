import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFileConfig, defaultValue } from 'app/shared/model/file-config.model';

export const ACTION_TYPES = {
  FETCH_FILECONFIG_LIST: 'fileConfig/FETCH_FILECONFIG_LIST',
  FETCH_FILECONFIG: 'fileConfig/FETCH_FILECONFIG',
  CREATE_FILECONFIG: 'fileConfig/CREATE_FILECONFIG',
  UPDATE_FILECONFIG: 'fileConfig/UPDATE_FILECONFIG',
  DELETE_FILECONFIG: 'fileConfig/DELETE_FILECONFIG',
  RESET: 'fileConfig/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFileConfig>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FileConfigState = Readonly<typeof initialState>;

// Reducer

export default (state: FileConfigState = initialState, action): FileConfigState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FILECONFIG_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FILECONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FILECONFIG):
    case REQUEST(ACTION_TYPES.UPDATE_FILECONFIG):
    case REQUEST(ACTION_TYPES.DELETE_FILECONFIG):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FILECONFIG_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FILECONFIG):
    case FAILURE(ACTION_TYPES.CREATE_FILECONFIG):
    case FAILURE(ACTION_TYPES.UPDATE_FILECONFIG):
    case FAILURE(ACTION_TYPES.DELETE_FILECONFIG):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FILECONFIG_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FILECONFIG):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FILECONFIG):
    case SUCCESS(ACTION_TYPES.UPDATE_FILECONFIG):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FILECONFIG):
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

const apiUrl = 'api/file-configs';

// Actions

export const getEntities: ICrudGetAllAction<IFileConfig> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FILECONFIG_LIST,
    payload: axios.get<IFileConfig>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFileConfig> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FILECONFIG,
    payload: axios.get<IFileConfig>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFileConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FILECONFIG,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFileConfig> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FILECONFIG,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFileConfig> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FILECONFIG,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
