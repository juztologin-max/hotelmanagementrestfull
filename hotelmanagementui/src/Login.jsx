import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { Container, Card, Form, Button, FloatingLabel, Alert } from "react-bootstrap";
import HttpClient from "./httpClient.js";
import useAuth from "./AuthContext.jsx";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();
  const { login, loginUser, isLoading } = useAuth();

  useEffect(() => {
    if (loginUser) {
      return navigate("/redirector", { replace: true });
    }
  }, [loginUser, navigate]);

  const handleLogin = async (e) => {
    e.preventDefault();
    setError("");
    try {
      const response = await HttpClient.post("/auth/login", {
        username: username,
        password: password,
      });
      login(response.data.access_token);
      navigate("/redirector");
    } catch (error) {
      setError("Invalid credentials" + error.message);
      console.log(error);
    }
  };
  if (isLoading || loginUser) {
    return null;
  }
  return (
    <Container className="d-flex justify-content-center align-items-center min-vh-100">
      <Card className="w-auto shadow-sm p-4">
        <Card.Body>
          <h2 className="text-center mb-4 font-weight-bold">Login</h2>
          <Form onSubmit={handleLogin}>
            <FloatingLabel controlId="username-floating-id" label="Username" className="mb-3">
              <Form.Control
                type="text"
                placeholder="Type your username here"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                required
              />
            </FloatingLabel>
            <FloatingLabel controlId="password-floating-id" label="password" className="mb-3">
              <Form.Control
                type="password"
                value={password}
                placeholder="Type your password here"
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </FloatingLabel>
            {error && (
              <Alert variant="danger" dismissible onClose={() => setError(null)}>
                {error}
              </Alert>
            )}
            <Button variant="primary" type="submit" className="w-100">
              Sign In
            </Button>
          </Form>
        </Card.Body>
      </Card>
    </Container>
  );
}
