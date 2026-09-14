import { useState } from "react";
import { Link } from "react-router-dom";
import useAuth from "../AuthContext.jsx";
import HttpClient from "../httpClient.js";
const AdminDashboard = () => {
  const { logout, loginUser } = useAuth();
  const [data, setData] = useState("");
  const handleTest = async () => {
    const response = await HttpClient.get("/admin/test");
    setData(response.data);
    return;
  };
  return (
    <div>
      <h1>Hello {loginUser.username}</h1>
      <button onClick={logout}>Logout</button>
      <div>{data}</div>
      <button onClick={handleTest}>Test</button>
      <br />
      <Link to="/admin/Test">TestPage</Link>
    </div>
  );
};
export default AdminDashboard;
