import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Login";
import SignUp from "./SignUp";
import AuthProvider from "./AuthProvider.jsx";
import DashboardRedirector from "./DashboardRedirector.jsx";

import AdminRoutes from "./admin/AdminRoutes.jsx";
import CustomerRoutes from "./customers/CustomerRoutes.jsx";
import AxiosAuthBridge from "./AxiosAuthBridge.jsx";
const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/signup" element={<SignUp />} />
        <Route path="/redirector" element={<DashboardRedirector />} />
        <Route path="/admin/*" element={<AdminRoutes />} />
        <Route path="/customer/*" element={<CustomerRoutes />} />
      </Routes>
    </Router>
  );
};

function App() {
  return (
    <AuthProvider>
      <AxiosAuthBridge>
        <AppRoutes />
      </AxiosAuthBridge>
    </AuthProvider>
  );
}

export default App;
