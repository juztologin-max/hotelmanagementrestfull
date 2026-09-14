import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "../ProtectedRoute.jsx";
import AdminDashboard from "./AdminDashboard.jsx";
import AdminTestPage from "./AdminTestPage.jsx";
const AdminRoutes = () => {
  return (
    <Routes>
      <Route element={<ProtectedRoute allowedRoles={["ROLE_ADMIN"]} />}>
        <Route path="dashboard" element={<AdminDashboard />} />
        <Route path="Test" element={<AdminTestPage />} />
      </Route>
    </Routes>
  );
};
export default AdminRoutes;
