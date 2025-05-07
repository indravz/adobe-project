import React from "react";
import { Box, Typography, Button } from "@mui/material";

const PreLogin = ({ onSignIn }) => {
  return (
    <Box
      display="flex"
      flexDirection="column"
      alignItems="center"
      justifyContent="center"
      minHeight="80vh"
      textAlign="center"
    >
      <Typography variant="h3" gutterBottom>
        Welcome to the Trading App
      </Typography>
      <Typography variant="h6" color="text.secondary" sx={{ mb: 3 }}>
        Please sign in to manage your trading accounts
      </Typography>
      <Button variant="contained" size="large" onClick={onSignIn}>
        Sign In
      </Button>
    </Box>
  );
};

export default PreLogin;
