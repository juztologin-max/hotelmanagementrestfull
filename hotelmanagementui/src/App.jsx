import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Login from "./Login";
import AuthProvider from "./AuthProvider.jsx";
import DashboardRedirector from "./DashboardRedirector.jsx";

import AdminRoutes from "./admin/AdminRoutes.jsx";
import AxiosAuthBridge from "./AxiosAuthBridge.jsx";
const AppContent = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/redirector" element={<DashboardRedirector />} />
        <Route path="/admin/*" element={<AdminRoutes />} />
      </Routes>
    </Router>
  );
};

function App() {
  return (
    <AuthProvider>
      <AxiosAuthBridge>
        <AppContent />
      </AxiosAuthBridge>
    </AuthProvider>
  );
}

export default App;
