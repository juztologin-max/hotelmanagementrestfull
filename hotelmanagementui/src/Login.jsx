import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
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
    <div>
      <form onSubmit={handleLogin}>
        <input
          type="text"
          placeholder="username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          required
        />
        <br />
        <input
          type="password"
          placeholder="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <div>{error && <div>{error}</div>}</div>
        <button type="submit">Sign In</button>
      </form>
    </div>
  );
}
