import { useState } from "react";
import { Link } from "react-router-dom";
import { Container, Row, Col } from "react-bootstrap";
import useAuth from "../AuthContext.jsx";
import HttpClient from "../httpClient.js";
import AdminHeader from "./AdminHeader.jsx";
const AdminDashboard = () => {
  const { logout, loginUser } = useAuth();
  const [data, setData] = useState("");
  const handleTest = async () => {
    const response = await HttpClient.get("/admin/test");
    setData(response.data);
    return;
  };
  return (
    <>
      <AdminHeader />
      <Container>
        <Row>
          <h1>Hello {loginUser.username}</h1>
        </Row>
        <Row>
          <button onClick={logout}>Logout</button>
          <div>{data}</div>
          <button onClick={handleTest}>Test</button>
          <br />
          <Link to="/admin/Test">TestPage</Link>
        </Row>
      </Container>
    </>
  );
};
export default AdminDashboard;
