import { useState, useEffect } from "react";
import { useNavigate, Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card";
import { Field, FieldDescription, FieldGroup, FieldLabel } from "@/components/ui/field";
import { Input } from "@/components/ui/input";

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
    <div className="flex min-h-svh w-full items-center justify-center p-6 md:p-10">
      <div className="w-full max-w-sm flex flex-col gap-6">
        <Card>
          <CardHeader>
            <CardTitle>Login to your account</CardTitle>
            <CardDescription>Enter your email below to login to your account</CardDescription>
          </CardHeader>
          <CardContent>
            <form onSubmit={handleLogin}>
              <FieldGroup>
                <Field>
                  <FieldLabel htmlFor="username">Username</FieldLabel>
                  <Input
                    id="username"
                    type="text"
                    required
                    value={username}
                    onChange={(t) => setUsername(t.target.value)}
                  />
                </Field>
                <Field>
                  <div className="flex items-center">
                    <FieldLabel htmlFor="password">Password</FieldLabel>
                    <Link
                      className="ml-auto inline-block text-sm underline-offset-4 hover:underline"
                      to="/forgot-password"
                    >
                      Forgot Password?
                    </Link>
                  </div>
                  <Input
                    id="password"
                    type="password"
                    required
                    onChange={(t) => setPassword(t.target.value)}
                  />
                </Field>
                <Field>
                  <Button type="submit">Login</Button>
                  <FieldDescription className="text-center">
                    {"Don't have an account?"} <Link to="/signup">Sign Up</Link>
                  </FieldDescription>
                </Field>
              </FieldGroup>
            </form>
          </CardContent>
        </Card>
      </div>
    </div>
  );
}
