import React from "react";
import { useLocation, Outlet, Navigate } from "react-router-dom";
import { useSelector } from "react-redux";

function RequireAuth() {
  const location = useLocation();
  const currentUser = useSelector((state) => state.user.currentUser);

  return currentUser?.user.roles.includes("ROLE_ADMIN") ? (
    <Outlet />
  ) : (
    <Navigate to="/login" state={{ from: location }} replace />
  );
}

export default RequireAuth;
