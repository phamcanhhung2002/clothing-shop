import {
  authFailure,
  authStart,
  loginSuccess,
  logoutSuccess,
} from "./userRedux";
import { publicRequest } from "../requestMethods";
import { store } from "./store";

export const login = async (dispatch, user) => {
  dispatch(authStart());
  try {
    const res = await publicRequest.post("/auth/login", user);
    dispatch(loginSuccess(res.data));
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
