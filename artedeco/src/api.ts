import axios from 'axios';

//>>>>>Mudar a url<<<<<<
const api = axios.create({
  baseURL: 'http://localhost:8085',
  headers: {
    'Content-Type': 'application/json',
  },
});


api.interceptors.request.use((config) => {
  const token = localStorage.getItem('artedeco_token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});


api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      
      localStorage.removeItem('artedeco_token');
      localStorage.removeItem('artedeco_sessao');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;