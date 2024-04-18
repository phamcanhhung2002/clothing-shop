import "./productList.css";
import { DataGrid } from "@mui/x-data-grid";
import { DeleteOutline } from "@mui/icons-material";
import { Link } from "react-router-dom";
import { useEffect, useState, useCallback } from "react";
import { userRequest } from "../../requestMethods";
import { productRows } from "../../dummyData";

export default function ProductList() {
  const [data, setData] = useState(productRows);
  const [paginationModel, setPaginationModel] = useState({
    pageSize: 10,
    page: 0,
  });
  const [isLoading, setIsLoading] = useState(true);
  const [rowCount, setRowCount] = useState(0);

  const [sortModel, setSortModel] = useState(undefined);

  const handleSortModelChange = useCallback((sortModel) => {
    setSortModel({ ...sortModel[0] });
  }, []);

  useEffect(() => {
    const getProducts = async () => {
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

        const { data } = await userRequest.get("/products", options);
        setRowCount(data.totalElements);
        setData(data.content);
      } catch (error) {
        console.log(error);
      }
      setIsLoading(false);
    };
    getProducts();
  }, [paginationModel, sortModel]);

  const handleDelete = (id) => {
    setData(data.filter((item) => item.id !== id));
  };

  const columns = [
    { field: "id", headerName: "ID", width: 90 },
    {
      field: "title",
      headerName: "Product",
      width: 400,
      renderCell: (params) => {
        return (
          <div className="productListItem">
            <img className="productListImg" src={params.row.img} alt="" />
            {params.row.title}
          </div>
        );
      },
    },
    // { field: "inStock", headerName: "Stock", width: 200 },
    {
      field: "price",
      headerName: "Price",
      width: 160,
    },
    {
      field: "action",
      headerName: "Action",
      width: 150,
      renderCell: (params) => {
        return (
          <>
            <Link to={"/product/" + params.row.id}>
              <button className="productListEdit">Edit</button>
            </Link>
            <DeleteOutline
              className="productListDelete"
              onClick={() => handleDelete(params.row.id)}
            />
          </>
        );
      },
    },
  ];

  return (
    <div className="productList">
      <div className="productsTitleContainer">
        <h1 className="productsTitle">Products</h1>
        <Link to="/newProduct">
          <button className="productAddButton">Create</button>
        </Link>
      </div>
      <div style={{ height: 350, width: "100%" }}>
        <DataGrid
          autoHeight
          rows={data}
          disableSelectionOnClick
          columns={columns}
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
    </div>
  );
}
