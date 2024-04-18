import "./userList.css";
import { DataGrid } from "@mui/x-data-grid";
import { DeleteOutline } from "@mui/icons-material";
import { Link } from "react-router-dom";
import { useEffect, useState, useCallback } from "react";
import { userRequest } from "../../requestMethods";

export default function UserList() {
  const [data, setData] = useState([]);
  const [paginationModel, setPaginationModel] = useState({
    pageSize: 10,
    page: 0,
  });
  const [isLoading, setIsLoading] = useState(true);

  const [sortModel, setSortModel] = useState(undefined);

  const [rowCount, setRowCount] = useState(0);

  const handleSortModelChange = useCallback((sortModel) => {
    setSortModel({ ...sortModel[0] });
  }, []);

  const handleDelete = (id) => {
    setData(data.filter((item) => item.id !== id));
  };

  useEffect(() => {
    const getUsers = async () => {
      try {
        const options = {
          params: {
            page: paginationModel.page,
            size: paginationModel.pageSize,
          },
        };

        if (sortModel && sortModel.field) {
          options.params.sort = `${sortModel.field},${sortModel.sort}`;
        }

        const { data } = await userRequest.get("/admins/users", options);
        setRowCount(data.totalElements);
        setData(data.content);
      } catch (error) {
        console.log(error);
      }

      setIsLoading(false);
    };
    getUsers();
  }, [paginationModel, sortModel]);

  const columns = [
    { field: "id", headerName: "ID", width: 90 },
    {
      field: "username",
      headerName: "User",
      width: 200,
      renderCell: (params) => {
        return (
          <div className="userListUser">
            <img className="userListImg" src={params.row.img} alt="" />
            {params.row.username}
          </div>
        );
      },
    },
    { field: "email", headerName: "Email", width: 200 },
    {
      field: "action",
      headerName: "Action",
      width: 150,
      renderCell: (params) => {
        return (
          <>
            <Link to={"/user/" + params.row.id}>
              <button className="userListEdit">Edit</button>
            </Link>
            <DeleteOutline
              className="userListDelete"
              onClick={() => handleDelete(params.row.id)}
            />
          </>
        );
      },
    },
  ];

  return (
    <div className="userList">
      <div className="usersTitleContainer">
        <h1 className="usersTitle">Users</h1>
        <Link to="/newUser">
          <button className="userAddButton">Create</button>
        </Link>
      </div>
      <DataGrid
        autoHeight
        rows={data}
        disableSelectionOnClick
        columns={columns}
        getRowId={(row) => row.id}
        rowCount={rowCount}
        checkboxSelection
        paginationModel={paginationModel}
        onPaginationModelChange={setPaginationModel}
        paginationMode="server"
        sortingMode="server"
        onSortModelChange={handleSortModelChange}
        loading={isLoading}
      />
    </div>
  );
}
