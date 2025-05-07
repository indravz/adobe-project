import React, { useState } from 'react';
import axios from 'axios';

const Login = ({ setIsLoggedIn, setUserEmail }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false); // State to manage loading
  const [error, setError] = useState(''); // State to display errors

  const handleLogin = async () => {
    setLoading(true); // Show loading indicator during login process
    setError(''); // Clear previous errors

    try {
      const response = await axios.post(
        'http://localhost:8080/api/auth/login', // Replace with your backend login endpoint
        { email, password },
        { withCredentials: true } // Include credentials for session handling
      );

      if (response.status === 200 && response.data) {
        // Login success
        setUserEmail(email); // Update the email in parent state
        setIsLoggedIn(true); // Update the login state
        setLoading(false); // Remove loading state
      } else {
        // Handle unexpected responses
        throw new Error('Unexpected server response. Please try again.');
      }
    } catch (error) {
      console.error('Login error:', error);
      setError(error.response?.data?.message || 'Login failed. Please try again.'); // Display server error or fallback
      setIsLoggedIn(false);
      setLoading(false); // Remove loading state
    }
  };

  return (
    <div style={styles.container}>
      <h2>Login</h2>
      {error && <p style={styles.error}>{error}</p>} {/* Display errors */}
      <input
        type="email"
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
        style={styles.input}
      />
      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        style={styles.input}
      />
      <button onClick={handleLogin} style={styles.button} disabled={loading}>
        {loading ? 'Logging in...' : 'Login'}
      </button>
    </div>
  );
};

const styles = {
  container: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    padding: '20px',
  },
  input: {
    margin: '10px 0',
    padding: '8px',
    width: '100%',
    maxWidth: '300px',
    borderRadius: '4px',
    border: '1px solid #ccc',
  },
  button: {
    padding: '10px 20px',
    borderRadius: '4px',
    border: 'none',
    backgroundColor: '#1976d2',
    color: '#fff',
    cursor: 'pointer',
  },
  error: {
    color: 'red',
    marginBottom: '10px',
  },
};

export default Login;