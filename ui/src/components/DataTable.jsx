// components/DataTable.tsx
import { DataGrid, GridColDef } from "@mui/x-data-grid";
import { Box, Typography } from "@mui/material";

interface DataTableProps {
  columns: GridColDef[];
  rows: any[];
  title?: string;
}

export default function DataTable({ columns, rows, title }: DataTableProps) {
  return (
    <Box sx={{ height: 400, width: "100%", mt: 2 }}>
      {title && <Typography variant="h6" gutterBottom>{title}</Typography>}
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={5}
        rowsPerPageOptions={[5, 10, 25]}
      />
    </Box>
  );
}
