import { NavLink } from "react-router-dom";
import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import NavDropdown from "react-bootstrap/NavDropdown";
import useAuth from "../AuthContext.jsx";

const AdminHeader = () => {
  const { logout, loginUser } = useAuth();

  return (
    <Navbar expand="lg" className="bg-body-terirary border-bottom">
      <Container>
        <Navbar.Brand as={NavLink} to="/admin/dashboard">
          Home
        </Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="admin-header-nav-id">
          <Nav>
            <Nav.Link as={NavLink} onClick={logout}>
              Logout
            </Nav.Link>{" "}
          </Nav>
        </Navbar.Collapse>
        <NavDropdown title={loginUser.username} id="profile-dropdown-id">
          <NavDropdown.Item as={NavLink} onClick={logout}>
            Logout
          </NavDropdown.Item>
        </NavDropdown>
      </Container>
    </Navbar>
  );
};

export default AdminHeader;
