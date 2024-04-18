import axios from "axios";
import { store } from "./redux/store";
import { setAccessToken } from "./redux/userRedux";
import { logout } from "./redux/apiCalls";

const BASE_URL = "http://localhost:8080/api/";

export const publicRequest = axios.create({
  baseURL: BASE_URL,
});

const userRequest = axios.create({
  baseURL: BASE_URL,
  headers: { "Access-Control-Allow-Origin": "*" },
});

userRequest.interceptors.request.use(
  async (config) => {
    const accessToken = store.getState()?.user.currentUser.accessToken;
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    config.timeout = 15000;
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

userRequest.interceptors.response.use(
  (res) => {
    return res;
  },
  async (err) => {
    const originalConfig = err.config;

    if (originalConfig.url !== "auth/login" && err.response) {
      // Access Token was expired
      if (err.response.status === 401 && !originalConfig._retry) {
        originalConfig._retry = true;

        try {
          const refreshToken = store.getState()?.user.currentUser.refreshToken;
          const rs = await userRequest.post("/auth/refreshtoken", {
            refreshToken,
          });

          const { accessToken } = rs.data;

          store.dispatch(setAccessToken(accessToken));

          return userRequest(originalConfig);
        } catch (_error) {
          await logout();
          return Promise.reject(_error);
        }
      }
    }

    return Promise.reject(err);
  }
);

export { userRequest };
