import {
  authStart,
  authFailure,
  loginSuccess,
  logoutSuccess,
  registerSuccess,
} from "./userRedux";
import { publicRequest } from "../requestMethods";
import { store } from "./store";

export const login = async (dispatch, user) => {
  dispatch(authStart());
  try {
    const { data } = await publicRequest.post("/auth/login", user);
    dispatch(loginSuccess(data));

    return true;
  } catch (err) {
    dispatch(authFailure(err.response.data));
    return false;
  }
};

export const logout = async () => {
  const dispatch = store.dispatch;
  const refreshToken = store.getState().user.currentUser?.refreshToken;
  dispatch(authStart());
  try {
    await publicRequest.post("/auth/logout", {
      refreshToken,
    });

    dispatch(logoutSuccess());
  } catch (err) {
    dispatch(authFailure(err.response.data));
  }
};

export const register = async (user) => {
  const dispatch = store.dispatch;
  dispatch(authStart());
  try {
    await publicRequest.post("/auth/register", user);
    dispatch(registerSuccess());
    return true;
  } catch (err) {
    dispatch(authFailure(err.response.data));
    return false;
  }
};
