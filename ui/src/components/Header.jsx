import React from 'react';
import { AppBar, Toolbar, Typography, Button, Box } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { AccountCircle } from '@mui/icons-material'; // Importing AccountCircle icon

const Header = ({ userEmail }) => {
  const navigate = useNavigate();

  const handleProfileClick = () => {
    navigate('/profile');
  };

  return (
    <AppBar position="static" sx={{ backgroundColor: '#1976d2' }}>
      <Toolbar>
        {/* App Name or Logo */}
        <Typography
          variant="h6"
          sx={{ flexGrow: 1, cursor: 'pointer' }}
          onClick={() => navigate('/trading-app')}
        >
          Trading App
        </Typography>

        {/* Conditionally Render Profile Button */}
        {userEmail && (
          <Box>
            <Button
              variant="contained"
              color="secondary"
              onClick={handleProfileClick}
              sx={{
                marginLeft: 2,
                display: 'flex',
                alignItems: 'center',
                backgroundColor: '#1976d2', // Maintain your button color
              }}
            >
              <AccountCircle
                sx={{
                  fontSize: 40, // Adjust the size of the icon
                  color: 'white', // Whitish-grey color for the icon
                  marginRight: 1, // Space between icon and text
                }}
              />
              Profile
            </Button>
          </Box>
        )}
      </Toolbar>
    </AppBar>
  );
};

export default Header;