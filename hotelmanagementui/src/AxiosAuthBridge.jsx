import { useEffect, useRef } from "react";
import { matchPath } from "react-router-dom";
import useAuth from "./AuthContext.jsx";
import HttpClient from "./httpClient.js";
export default function AxiosAuthBridge({ children }) {
  const { login, logout, token } = useAuth();
  const authRef = useRef({ login, logout, token });
  useEffect(() => {
    authRef.current = { login, logout, token };
  }, [login, logout, token]);

  useEffect(() => {
    const exclusionList = ["/auth/login", "/auth/refresh", "/auth/logout"];
    const requestInterceptor = HttpClient.interceptors.request.use((config) => {
      const currentPath = config.url || "";
      const needsAuthorization = !exclusionList.some((pattern) =>
        matchPath({ path: pattern, end: true }, currentPath),
      );
      console.log(currentPath, needsAuthorization);
      if (needsAuthorization && authRef.current.token) {
        config.headers.Authorization = "Bearer " + authRef.current.token;
      }
      return config;
    });
    const responseInterceptor = HttpClient.interceptors.response.use(
      (response) => response,
      async (error) => {
        const origReq = error.config;
        const origStatus = error.response?.status;
        if (origReq?.url?.includes("/auth/refresh")) {
          console.log(error.message);
          if (error.message !== "canceled") {
            console.log("calling logout because /auth/refresh failed");
            authRef.current.logout();
          }
          return Promise.reject(error);
        }
        if (origStatus === 401 && !origReq._retry) {
          origReq._retry = true;
          try {
            const response = await HttpClient.post("/auth/refresh");
            const newToken = response.data?.access_token;
            authRef.current.login(newToken);
            origReq.headers.Authorization = "Bearer " + newToken;
            return HttpClient(origReq);
          } catch (refreshError) {
            console.log("claling logout because retrying /auth/refresh failed " + refreshError);
            authRef.current.logout();
            return Promise.reject(refreshError);
          }
        }
        return Promise.reject(error);
      },
    );
    return () => {
      HttpClient.interceptors.request.eject(requestInterceptor);
      HttpClient.interceptors.response.eject(responseInterceptor);
    };
  }, []);
  return children;
}
