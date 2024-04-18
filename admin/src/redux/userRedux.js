import { createSlice } from "@reduxjs/toolkit";

const userSlice = createSlice({
  name: "user",
  initialState: {
    currentUser: null,
    isFetching: false,
    error: false,
  },
  reducers: {
    authStart: (state) => {
      state.isFetching = true;
      state.error = false;
    },
    loginSuccess: (state, action) => {
      state.isFetching = false;
      state.currentUser = action.payload;
      state.error = false;
    },
    authFailure: (state) => {
      state.isFetching = false;
      state.error = true;
    },
    logoutSuccess: (state) => {
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
} = userSlice.actions;
export default userSlice.reducer;
