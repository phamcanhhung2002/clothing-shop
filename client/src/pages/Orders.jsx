import React, { useEffect, useState, useCallback } from "react";
import Announcement from "../components/Announcement";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import styled from "styled-components";
import { mobile } from "../responsive";
import { DataGrid } from "@mui/x-data-grid";
import { userRequest } from "../requestMethods";
import { Link } from "react-router-dom";

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 20px;
  ${mobile({ padding: "10px" })}
`;

const Title = styled.h1`
  font-weight: 300;
  text-align: center;
`;

const Orders = () => {
  const [data, setData] = useState([]);
  const [rowCount, setRowCount] = useState(0);

  const [paginationModel, setPaginationModel] = useState({
    pageSize: 10,
    page: 0,
  });
  const [isLoading, setIsLoading] = useState(true);

  const [sortModel, setSortModel] = useState(undefined);

  const handleSortModelChange = useCallback((sortModel) => {
    setSortModel({ ...sortModel[0] });
  }, []);

  useEffect(() => {
    const getOrders = async () => {
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

        const { data } = await userRequest.get("/users/orders", options);

        setData(data.content);
        setRowCount(data.totalElements);
      } catch (error) {
        console.log(error);
      }
      setIsLoading(false);
    };
    getOrders();
  }, [paginationModel, sortModel]);

  const columns = [
    { field: "id", headerName: "ID", width: 220 },
    {
      field: "amount",
      headerName: "Amount",
      width: 160,
      valueGetter: (value) => {
        return `${value}$`;
      },
    },
    {
      field: "status",
      headerName: "Status",
      width: 160,
    },
    {
      field: "createdAt",
      headerName: "Ordered On",
      width: 200,
      valueGetter: (value) => {
        const date = new Date(value);
        const options = { year: "numeric", month: "long", day: "numeric" };
        const formattedDate = new Intl.DateTimeFormat("en-US", options).format(
          date
        );
        return formattedDate;
      },
    },
    {
      field: "action",
      headerName: "Action",
      width: 150,
      renderCell: (params) => {
        return (
          <>
            <Link to={"/order/" + params.row.id}>
              <span>View order</span>
            </Link>
          </>
        );
      },
    },
  ];

  return (
    <div>
      <Navbar />
      <Announcement />
      <Wrapper>
        <Title>MY ORDER</Title>
        <div style={{ height: 350, width: "100%" }}>
          <DataGrid
            autoHeight
            checkboxSelection
            rows={data}
            columns={columns}
            getRowId={(row) => row.id}
            rowCount={rowCount}
            paginationModel={paginationModel}
            onPaginationModelChange={setPaginationModel}
            paginationMode="server"
            sortingMode="server"
            onSortModelChange={handleSortModelChange}
            loading={isLoading}
          />
        </div>
      </Wrapper>
      <Footer />
    </div>
  );
};

export default Orders;
