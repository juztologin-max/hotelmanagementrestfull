import React from "react";
import { Navigate, Outlet } from "react-router-dom";
import useAuth from "./AuthContext.jsx";
const ProtectedRoute = ({ allowedRoles }) => {
  const { loginUser, isLoading } = useAuth();
  if (isLoading) {
    return null;
  }
  if (!loginUser) return <Navigate to="/" replace />;

  if (allowedRoles && !loginUser.roles.some((role) => allowedRoles.includes(role))) {
    return <Navigate to="/unauthorised" replace />;
  }
  return <Outlet />;
};
export default ProtectedRoute;
