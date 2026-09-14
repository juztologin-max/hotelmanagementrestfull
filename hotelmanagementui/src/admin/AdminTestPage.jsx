import { useState } from "react";
import HttpClient from "../httpClient.js";
export default function AdminTestPage() {
  const [data, setData] = useState("");
  const handleTest = async () => {
    const response = await HttpClient.get("/admin/test");
    setData(response.data);
    return;
  };

  return (
    <div>
      <button onClick={handleTest}>Test</button>
      <div>{data}</div>
    </div>
  );
}
