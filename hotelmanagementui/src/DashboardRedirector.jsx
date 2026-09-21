import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import useAuth from "./AuthContext.jsx";

const DashBoardRedirector = () => {
  const { loginUser } = useAuth();
  const navigate = useNavigate();
  useEffect(() => {
    //console.log(JSON.stringify(loginUser));
    if (loginUser) {
      if (loginUser.roles.includes("ROLE_ADMIN")) {
        navigate("/admin/dashboard", { replace: true });
      } else if (loginUser.roles.includes("ROLE_CUSTOMER")) {
        navigate("/customer/dashboard", { replace: true });
      }
    } else {
      navigate("/", { replace: true });
    }
  }, [loginUser, navigate]);
};

export default DashBoardRedirector;
