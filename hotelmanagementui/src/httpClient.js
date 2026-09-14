import axios from "axios";
const HttpClient = axios.create({ baseURL: "https://localhost:8443/api", withCredentials: true });

export default HttpClient;
