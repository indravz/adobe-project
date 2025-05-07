import React, { useState } from "react";
import config from "../config";
import axios from "axios";
import {
  Button,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Typography,
  Paper,
  CircularProgress,
  Alert,
  Box,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  MenuItem,
} from "@mui/material";

const accountTypes = ["INDIVIDUAL", "JOINT"]; // update as per your backend enum

function Accounts({ userId }) {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [form, setForm] = useState({
    accountNumber: "",
    accountType: "",
  });
  const [creating, setCreating] = useState(false);

  const fetchAccounts = async () => {
    setLoading(true);
    setError(null);

    try {
      const res = await fetch(`${config.API_BASE_URL}/api/accounts`, {
        method: "GET",
        credentials: "include",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (!res.ok) throw new Error(`API responded with ${res.status}`);

      const data = await res.json();

      const processed = data.map((item, index) => ({
        id: index + 1,
        accountNumber: item.accountNumber,
        accountType: item.accountType,
        userId: item.userId,
        createdAt: new Date(item.createdAt).toLocaleString(),
        updatedAt: new Date(item.updatedAt).toLocaleString(),
      }));

      setRows(processed);
    } catch (err) {
      console.error("Failed to fetch accounts:", err);
      setError("Unable to fetch accounts.");
    } finally {
      setLoading(false);
    }
  };

  const handleFormChange = (e) => {
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));
  };

  const handleCreateAccount = async () => {
    if (!form.accountNumber || !form.accountType) return;

    setCreating(true);
    try {
      await axios.post(
        `${config.API_BASE_URL}/api/accounts`,
        {
          accountNumber: form.accountNumber,
          accountType: form.accountType,
          userId: userId,
        },
        { withCredentials: true }
      );

      setDialogOpen(false);
      setForm({ accountNumber: "", accountType: "" });
      fetchAccounts();
    } catch (err) {
      console.error("Account creation failed:", err);
      alert("Failed to create account.");
    } finally {
      setCreating(false);
    }
  };

  return (
    <Box sx={{ mt: 3 }}>
      <Box sx={{ display: "flex", gap: 2, mb: 2 }}>
        <Button variant="contained" onClick={fetchAccounts}>
          List Accounts
        </Button>
        <Button variant="outlined" onClick={() => setDialogOpen(true)}>
          Create Account
        </Button>
      </Box>

      {loading && (
        <Box sx={{ mt: 2 }}>
          <CircularProgress />
        </Box>
      )}

      {error && (
        <Box sx={{ mt: 2 }}>
          <Alert severity="error">{error}</Alert>
        </Box>
      )}

      {rows.length > 0 && (
        <Paper sx={{ mt: 3, overflowX: "auto" }}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell><strong>Account Number</strong></TableCell>
                <TableCell><strong>Account Type</strong></TableCell>
                <TableCell><strong>User ID</strong></TableCell>
                <TableCell><strong>Created At</strong></TableCell>
                <TableCell><strong>Updated At</strong></TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {rows.map((row) => (
                <TableRow key={row.id}>
                  <TableCell>{row.accountNumber}</TableCell>
                  <TableCell>{row.accountType}</TableCell>
                  <TableCell>{row.userId}</TableCell>
                  <TableCell>{row.createdAt}</TableCell>
                  <TableCell>{row.updatedAt}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      )}

      {rows.length === 0 && !loading && !error && (
        <Typography variant="h6" sx={{ mt: 3 }}>
          Welcome to the App
        </Typography>
      )}

      {/* Dialog for Create Account */}
      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>Create New Account</DialogTitle>
        <DialogContent sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 1 }}>
          <TextField
            label="Account Number"
            name="accountNumber"
            value={form.accountNumber}
            onChange={handleFormChange}
            fullWidth
          />
          <TextField
            select
            label="Account Type"
            name="accountType"
            value={form.accountType}
            onChange={handleFormChange}
            fullWidth
          >
            {accountTypes.map((type) => (
              <MenuItem key={type} value={type}>
                {type}
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setDialogOpen(false)} disabled={creating}>
            Cancel
          </Button>
          <Button
            variant="contained"
            onClick={handleCreateAccount}
            disabled={creating}
            startIcon={creating ? <CircularProgress size={18} /> : null}
          >
            {creating ? "Creating..." : "Create Account"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default Accounts;