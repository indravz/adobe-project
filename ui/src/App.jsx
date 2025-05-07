import React, { useState, useEffect } from "react";
import { Routes, Route, Navigate } from "react-router-dom";
import { useAuth } from "react-oidc-context";
import axios from "axios";
import Accounts from "./components/Accounts";
import PreLogin from "./components/PreLogin";
import config from "./config";

import {
  Box,
  Typography,
  Button,
  Container,
  CircularProgress,
  Alert,
  Paper,
  AppBar,
  Toolbar,
} from "@mui/material";

function App() {
  const auth = useAuth();
  const [userEmail, setUserEmail] = useState("");
  const [userId, setUserId] = useState(null);
  const [backendMessage, setBackendMessage] = useState("");

  useEffect(() => {
    if (auth.user) {
      const email = auth.user.profile?.email || "";
      setUserEmail(email);

      const checkAndCreateUser = async () => {
        try {
          const response = await axios.get(`${config.API_BASE_URL}/api/users/check-email`, {
            params: { email },
          });

          if (response.data.exists) {
            setUserId(response.data.userId);
          } else {
            const newUser = {
              email,
              name: auth.user.profile?.name || "Unknown User",
            };
            const createResp = await axios.post(`${config.API_BASE_URL}/api/users`, newUser);
            setUserId(createResp.data.id);
            setBackendMessage("User created on our backend.");
          }
        } catch (err) {
          console.error("Error in user check or creation:", err);
        }
      };

      checkAndCreateUser();
    }
  }, [auth.user]);

  const signOutRedirect = async () => {
    await auth.removeUser();
    const clientId = "1igkruov9qivq8vibuerkqf3mo";
    const logoutUri = "https://indras.adobe-project.online/logout";
    const cognitoDomain = "https://adobe-project-user-pool.auth.us-east-1.amazoncognito.com";
    window.location.href = `${cognitoDomain}/logout?client_id=${clientId}&logout_uri=${encodeURIComponent(logoutUri)}`;
  };

  if (auth.isLoading) {
    return (
      <Box display="flex" justifyContent="center" alignItems="center" minHeight="100vh">
        <CircularProgress />
      </Box>
    );
  }

  if (auth.error) {
    return (
      <Container>
        <Alert severity="error">Error: {auth.error.message}</Alert>
      </Container>
    );
  }

  return (
    <Box>
      <AppBar position="static">
        <Toolbar>
          <Typography variant="h6" sx={{ flexGrow: 1 }}>
            Trading App
          </Typography>
          {auth.isAuthenticated ? (
            <Button color="inherit" onClick={signOutRedirect}>
              Logout
            </Button>
          ) : (
            <Button color="inherit" onClick={() => auth.signinRedirect()}>
              Login
            </Button>
          )}
        </Toolbar>
      </AppBar>

      <Container sx={{ mt: 4 }}>
        {auth.isAuthenticated ? (
          <>
            <Routes>
              <Route path="/" element={<Navigate to="/app" />} />
              <Route path="/app" element={<Typography variant="h4">Welcome to the App</Typography>} />
              <Route path="/callback" element={<Navigate to="/app" />} />
            </Routes>

            <Paper elevation={2} sx={{ p: 3, mb: 3 }}>
              <Typography variant="subtitle1">
                Signed in as: <strong>{userEmail}</strong>
              </Typography>
              {backendMessage && (
                <Alert severity="success" sx={{ mt: 2 }}>
                  {backendMessage}
                </Alert>
              )}
            </Paper>

            {<Accounts userId= {userId} />}
          </>
        ) : (
          <>
            <Routes>
              <Route path="/logout" element={<Typography variant="h4">You are logged out</Typography>} />
            </Routes>
            <PreLogin onSignIn={() => auth.signinRedirect()} />
          </>
        )}
      </Container>
    </Box>
  );
}

export default App;
