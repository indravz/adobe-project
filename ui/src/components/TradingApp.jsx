import React from 'react';

const TradingApp = ({ userEmail, userName }) => (
  <div>
    <h1>Welcome to Trading App</h1>
    <div>
      <p><strong>Email:</strong> {userEmail}</p>
      <p><strong>Name:</strong> {userName}</p>
    </div>
  </div>
);

export default TradingApp;
