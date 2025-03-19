import axios from "axios";

const axiosClient = axios.create({
  baseURL: `${import.meta.env.VITE_API_URL}`,
  timeout: 30000,
});

axiosClient.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const { response } = error;
    if (response) {
      if (response.status === 401) {
        localStorage.removeItem("token");
        localStorage.removeItem("user");
      }
      return Promise.reject(error);
    } else {
      console.error("Network error (connection refused):", error);
      return Promise.reject(error);
    }
  },
);

export default axiosClient;
