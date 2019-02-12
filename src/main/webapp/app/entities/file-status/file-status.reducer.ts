import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IFileStatus, defaultValue } from 'app/shared/model/file-status.model';

export const ACTION_TYPES = {
  FETCH_FILESTATUS_LIST: 'fileStatus/FETCH_FILESTATUS_LIST',
  FETCH_FILESTATUS: 'fileStatus/FETCH_FILESTATUS',
  CREATE_FILESTATUS: 'fileStatus/CREATE_FILESTATUS',
  UPDATE_FILESTATUS: 'fileStatus/UPDATE_FILESTATUS',
  DELETE_FILESTATUS: 'fileStatus/DELETE_FILESTATUS',
  RESET: 'fileStatus/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IFileStatus>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false
};

export type FileStatusState = Readonly<typeof initialState>;

// Reducer

export default (state: FileStatusState = initialState, action): FileStatusState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_FILESTATUS_LIST):
    case REQUEST(ACTION_TYPES.FETCH_FILESTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_FILESTATUS):
    case REQUEST(ACTION_TYPES.UPDATE_FILESTATUS):
    case REQUEST(ACTION_TYPES.DELETE_FILESTATUS):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_FILESTATUS_LIST):
    case FAILURE(ACTION_TYPES.FETCH_FILESTATUS):
    case FAILURE(ACTION_TYPES.CREATE_FILESTATUS):
    case FAILURE(ACTION_TYPES.UPDATE_FILESTATUS):
    case FAILURE(ACTION_TYPES.DELETE_FILESTATUS):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_FILESTATUS_LIST):
      return {
        ...state,
        loading: false,
        totalItems: action.payload.headers['x-total-count'],
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_FILESTATUS):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_FILESTATUS):
    case SUCCESS(ACTION_TYPES.UPDATE_FILESTATUS):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_FILESTATUS):
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

const apiUrl = 'api/file-statuses';

// Actions

export const getEntities: ICrudGetAllAction<IFileStatus> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_FILESTATUS_LIST,
    payload: axios.get<IFileStatus>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`)
  };
};

export const getEntity: ICrudGetAction<IFileStatus> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_FILESTATUS,
    payload: axios.get<IFileStatus>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IFileStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_FILESTATUS,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IFileStatus> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_FILESTATUS,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IFileStatus> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_FILESTATUS,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
