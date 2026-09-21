import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "@/ProtectedRoute";
import Dashboard from "@/customers/CustomerDashboard";
import NewBooking from "@/customers/NewBooking";
const AdminRoutes = () => {
  return (
    <Routes>
      <Route element={<ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]} />}>
        <Route path="dashboard" element={<Dashboard />} />
        <Route path="new-booking" element={<NewBooking />} />
      </Route>
    </Routes>
  );
};
export default AdminRoutes;
