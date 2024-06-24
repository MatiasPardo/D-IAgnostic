import axios from 'axios';

const BASE_URL = process.env.REACT_APP_API_URL ? `${process.env.REACT_APP_API_URL}/api/` : 'http://localhost:8080/api/'

export const instance = axios.create({
  baseURL: BASE_URL,
  headers: {
    'Credentials': 'true',
    'Authorization': 'Bearer ' + localStorage.getItem('token'),
    'Access-Control-Allow-Origin': '*',
    'Content-Type': 'application/json',
    'Access-Control-Allow-Methods': 'GET, PUT, POST, DELETE, OPTIONS'
  }
})

instance.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers['Authorization'] = `Bearer ${token}`;
      }
      return config;
    },
    (error) => {
      return Promise.reject(error);
    }
);




