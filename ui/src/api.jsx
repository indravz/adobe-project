// src/api.js
import axios from "axios";
import { useAuth } from "react-oidc-context";

// You can only access hooks like useAuth inside components or hooks,
// so we use a function to set up the interceptor.
let isInterceptorSet = false;

export const setupAxiosInterceptors = (auth) => {
  if (isInterceptorSet || !auth.user) return;

  const idToken = auth.user.id_token;

  axios.interceptors.request.use((config) => {
    config.headers.Authorization = `Bearer ${idToken}`;
    return config;
  });

  isInterceptorSet = true;
};
