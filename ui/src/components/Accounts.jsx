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
  IconButton,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";

const accountTypes = ["INDIVIDUAL", "JOINT"];

function Accounts({ userId, auth }) {
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [dialogOpen, setDialogOpen] = useState(false);
  const [editMode, setEditMode] = useState(false);
  const [currentEditId, setCurrentEditId] = useState(null);
  const [form, setForm] = useState({
    accountNumber: "",
    accountType: "",
  });
  const [creating, setCreating] = useState(false);

  const fetchAccounts = async () => {
    if (!userId) {
      setError("User ID is missing.");
      return;
    }

    setLoading(true);
    setError(null);

    try {
      const res = await axios.get(`${config.API_BASE_URL}/accounts/user/${userId}`, {
        headers: {
          Authorization: `Bearer ${auth.user?.id_token}`,
          "Content-Type": "application/json",
        },
        withCredentials: true,
      });

      const data = res.data;

      const processed = data.map((item) => ({
        id: item.id,
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

  const handleCreateOrUpdateAccount = async () => {
    if (!form.accountType) return;

    setCreating(true);
    try {
      if (editMode && currentEditId) {
        await axios.put(
          `${config.API_BASE_URL}/accounts/${currentEditId}`,
          {
            id: currentEditId,
            accountNumber: form.accountNumber,
            accountType: form.accountType,
            userId: userId,
          },
          {
            headers: {
              Authorization: `Bearer ${auth.user?.id_token}`,
              "Content-Type": "application/json",
            },
            withCredentials: true,
          }
        );
      } else {
        await axios.post(
          `${config.API_BASE_URL}/accounts`,
          {
            accountNumber: "", // server generates it or auto-filled
            accountType: form.accountType,
            userId: userId,
          },
          {
            headers: {
              Authorization: `Bearer ${auth.user?.id_token}`,
              "Content-Type": "application/json",
            },
            withCredentials: true,
          }
        );
      }

      setDialogOpen(false);
      setForm({ accountNumber: "", accountType: "" });
      setEditMode(false);
      setCurrentEditId(null);
      fetchAccounts();
    } catch (err) {
      console.error("Account create/update failed:", err);
      alert("Failed to process account.");
    } finally {
      setCreating(false);
    }
  };

  const handleEditClick = (account) => {
    setForm({
      accountNumber: account.accountNumber, // prefill for PUT call
      accountType: account.accountType,
    });
    setEditMode(true);
    setCurrentEditId(account.id);
    setDialogOpen(true);
  };

  const handleDelete = async (id) => {
    if (!window.confirm("Are you sure you want to delete this account?")) return;

    try {
      await axios.delete(`${config.API_BASE_URL}/accounts/${id}`, {
        headers: {
          Authorization: `Bearer ${auth.user?.id_token}`,
          "Content-Type": "application/json",
        },
        withCredentials: true,
      });

      fetchAccounts();
    } catch (err) {
      console.error("Delete failed:", err);
      alert("Failed to delete account.");
    }
  };

  return (
    <Box sx={{ mt: 3 }}>
      <Box sx={{ display: "flex", gap: 2, mb: 2 }}>
        <Button variant="contained" onClick={fetchAccounts} disabled={!userId}>
          List Accounts
        </Button>
        <Button
          variant="outlined"
          onClick={() => {
            setEditMode(false);
            setForm({ accountNumber: "", accountType: "" });
            setDialogOpen(true);
          }}
        >
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
                <TableCell><strong>Actions</strong></TableCell>
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
                  <TableCell>
                    <IconButton onClick={() => handleEditClick(row)}>
                      <EditIcon />
                    </IconButton>
                    <IconButton onClick={() => handleDelete(row.id)}>
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </Paper>
      )}

      <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} fullWidth maxWidth="sm">
        <DialogTitle>{editMode ? "Edit Account" : "Create New Account"}</DialogTitle>
        <DialogContent sx={{ display: "flex", flexDirection: "column", gap: 2, mt: 2 }}>
          <TextField
            label="Account Number"
            name="accountNumber"
            value={form.accountNumber}
            onChange={handleFormChange}
            fullWidth
            disabled // always disabled
          />
          <TextField
            label="Account Type"
            name="accountType"
            select
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
          <Button onClick={handleCreateOrUpdateAccount} variant="contained" disabled={creating}>
            {creating ? "Saving..." : editMode ? "Update" : "Create"}
          </Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}

export default Accounts;
