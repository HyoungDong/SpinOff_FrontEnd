import { createReducer } from '@reduxjs/toolkit';
import { DEL_TOKEN, SET_GOBACK_PAGE, SET_TOKEN, SIGN_OUT } from './action';

const initialState = {
  isSignIn: false,
  access_token: '',
  refresh_token: '',
  member_id: '',
  goBackPage: '',
};

const authReducer = createReducer(initialState, {
  [SET_TOKEN]: (state, action) => ({
    ...state,
    isSignIn: true,
    access_token: action.payload.token,
    refresh_token: action.payload.refreshToken,
    member_id: action.payload.id,
  }),
  [DEL_TOKEN]: state => ({
    ...state,
    access_token: '',
    refresh_token: '',
  }),
  [SET_GOBACK_PAGE]: (state, action) => ({
    ...state,
    goBackPage: action.payload,
  }),
  [SIGN_OUT]: state => ({
    ...state,
    isSignIn: false,
    access_token: '',
    refresh_token: '',
    member_id: '',
  }),
});

export default authReducer;
