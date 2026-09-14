import { useState, useEffect, useMemo, useCallback } from "react";
import { jwtDecode } from "jwt-decode";
import axios from "axios";
import { AuthContext } from "./AuthContext.jsx";
import HttpClient from "./httpClient.js";
const extractUserFromToken = (token) => {
  try {
    const decoded = jwtDecode(token);
    return {
      username: decoded.sub,
      roles: decoded.roles,
    };
  } catch (e) {
    console.log(e);
    return null;
  }
};

export default function AuthProvider({ children }) {
  const [auth, setAuth] = useState({ token: null, loginUser: null });
  const [isLoading, setLoading] = useState(true);

  const login = useCallback((cToken) => {
    setAuth({ token: cToken, loginUser: extractUserFromToken(cToken) });
  }, []);

  const logout = useCallback(async () => {
    try {
      await HttpClient.post("/auth/logout");
    } catch (e) {
      console.log("Failed to clear cookie" + e);
    } finally {
      setAuth({ token: null, loginUser: null });
    }
  }, []);

  const contextStore = useMemo(
    () => ({
      token: auth.token,
      loginUser: auth.loginUser,
      login: login,
      logout: logout,
      isLoading: isLoading,
    }),
    [auth.token, auth.loginUser, login, logout, isLoading],
  );

  useEffect(() => {
    const controller = new AbortController();
    let isMounted = true;

    const accessTokenUsingRefreshCookie = async () => {
      try {
        const response = await HttpClient.post(
          "/auth/refresh",
          {},
          {
            signal: controller.signal,
            withCredentials: true,
          },
        );
        const newToken = response.data.access_token;

        if (isMounted && newToken) {
          setAuth({ token: newToken, loginUser: extractUserFromToken(newToken) });
        }
      } catch (err) {
        if (axios.isCancel(err)) {
          return;
        }
        if (isMounted) logout();
      } finally {
        if (isMounted) setLoading(false);
      }
    };

    accessTokenUsingRefreshCookie();

    return () => {
      isMounted = false;
      controller.abort();
    };
  }, [logout]);

  return <AuthContext.Provider value={contextStore}>{children}</AuthContext.Provider>;
}
