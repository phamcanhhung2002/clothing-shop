import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: {
    currentUser: null,
    isFetching: false,
    error: null,
  },
  reducers: {
    authStart: (state) => {
      state.isFetching = true;
      state.error = null;
    },
    loginSuccess: (state, action) => {
      state.isFetching = false;
      state.error = null;
      state.currentUser = action.payload;
    },
    authFailure: (state, action) => {
      state.isFetching = false;
      state.error = action.payload.message;
    },
    logoutSuccess: (state) => {
      state.isFetching = false;
      state.error = null;
      state.currentUser = null;
    },
    registerSuccess: (state) => {
      state.isFetching = false;
      state.error = null;
      state.currentUser = null;
    },
    setAccessToken: (state, action) => {
      state.currentUser.accessToken = action.payload;
    },
  },
});

export const {
  authStart,
  loginSuccess,
  authFailure,
  logoutSuccess,
  setAccessToken,
  registerSuccess,
} = userSlice.actions;
export default userSlice.reducer;
