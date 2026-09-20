import { useState } from "react";
import { Link } from "react-router-dom";
import useAuth from "../AuthContext.jsx";
import HttpClient from "../httpClient.js";
import AdminHeader from "./AdminHeader.jsx";
const AdminDashboard = () => {
  const { logout, loginUser } = useAuth();
  const [data, setData] = useState("");
  const [count, setCount] = useState(0);

  const handleGetTest = async () => {
    const response = await HttpClient.get("/admin/test");
    setData(response.data);
    return;
  };
  const handlePostTest = async () => {
    await HttpClient.post("/admin/test", { id: count });
    setCount(() => count + 1);
    return;
  };
  return (
    <>
      <AdminHeader />
      <div>
        <button onClick={logout}>Logout</button>
        <div>{data}</div>
        <div>{count}</div>
        <button onClick={handleGetTest}>TestGet</button>
        <button onClick={handlePostTest}>TestPost</button>

        <br />
        <Link to="/admin/Test">TestPage</Link>
      </div>
    </>
  );
};
export default AdminDashboard;
