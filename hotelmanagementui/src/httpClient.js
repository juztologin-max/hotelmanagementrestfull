import axios from "axios";
const HttpClient = axios.create({ baseURL: "/api", withCredentials: true });

export default HttpClient;
