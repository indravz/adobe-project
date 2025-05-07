import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
  proxy: {
      '/api': {
        target: 'https://6fo7xjq7ee.execute-api.us-east-1.amazonaws.com',
        changeOrigin: true,
        secure: false,
      },
    },
      port: 80,// Replace with your desired port
      allowedHosts: true
    }
})
