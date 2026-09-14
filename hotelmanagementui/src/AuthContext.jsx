import { useContext, createContext } from "react";

export const AuthContext = createContext(null);

export default function useAuth() {
  return useContext(AuthContext);
}
