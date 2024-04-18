import styled from "styled-components";
import Navbar from "../components/Navbar";
import Announcement from "../components/Announcement";
import Products from "../components/Products";
import Newsletter from "../components/Newsletter";
import Footer from "../components/Footer";
import { mobile } from "../responsive";
import { useLocation } from "react-router";
import { useState } from "react";
import ReactPaginate from "react-paginate";
import { useEffect } from "react";
import { publicRequest } from "../requestMethods";
import ScrollToTop from "../components/ScrollToTop";

const Container = styled.div``;

const Title = styled.h1`
  margin: 20px;
`;

const FilterContainer = styled.div`
  display: flex;
  justify-content: space-between;
`;

const Filter = styled.div`
  margin: 20px;
  ${mobile({ width: "0px 20px", display: "flex", flexDirection: "column" })}
`;

const FilterText = styled.span`
  font-size: 20px;
  font-weight: 600;
  margin-right: 20px;
  ${mobile({ marginRight: "0px" })}
`;

const Select = styled.select`
  padding: 10px;
  margin-right: 20px;
  ${mobile({ margin: "10px 0px" })}
`;
const Option = styled.option``;

const PaginateContainer = styled.div`
  display: flex;
  justify-content: center;
`;

const ProductList = () => {
  const location = useLocation();
  const { cat, title } = location.state;
  const [filters, setFilters] = useState({});
  const [sort, setSort] = useState("created_at,desc");
  const [pageCount, setPageCount] = useState(0);
  const [products, setProducts] = useState([]);
  const [page, setPage] = useState(0);
  const size = 5;

  const handleFilters = (e) => {
    let value = e.target.value;
    value = value !== "Color" && value !== "Size" ? value : undefined;
    setFilters({
      ...filters,
      [e.target.name]: value,
    });
  };

  const handlePageClick = (event) => {
    setPage(event.selected);
  };

  useEffect(() => {
    const getProducts = async () => {
      try {
        const { data } = await publicRequest.get("/products", {
          params: {
            cat,
            page,
            size,
            sort,
            ...filters,
          },
        });
        console.log(data.totalPages);
        setProducts(data.content);
        setPageCount(data.totalPages);
      } catch (error) {
        console.log(error);
      }
    };

    console.log(filters);
    console.log(sort);
    getProducts();
  }, [filters, page, sort, cat]);

  return (
    <Container>
      <Navbar />
      <Announcement />
      <Title>{title}</Title>
      <FilterContainer>
        <Filter>
          <FilterText>Filter Products:</FilterText>
          <Select name="color" onChange={handleFilters}>
            <Option>Color</Option>
            <Option>white</Option>
            <Option>black</Option>
            <Option>red</Option>
            <Option>blue</Option>
            <Option>yellow</Option>
            <Option>green</Option>
          </Select>
          <Select name="clothing-size" onChange={handleFilters}>
            <Option>Size</Option>
            <Option>XS</Option>
            <Option>S</Option>
            <Option>M</Option>
            <Option>L</Option>
            <Option>XL</Option>
          </Select>
        </Filter>
        <Filter>
          <FilterText>Sort Products:</FilterText>
          <Select onChange={(e) => setSort(e.target.value)}>
            <Option value="created_at,desc">Newest</Option>
            <Option value="price,asc">Price (asc)</Option>
            <Option value="price,desc">Price (desc)</Option>
          </Select>
        </Filter>
      </FilterContainer>
      <Products products={products} />
      <PaginateContainer>
        <ReactPaginate
          breakLabel="..."
          nextLabel=">"
          previousLabel="<"
          pageCount={pageCount}
          onPageChange={handlePageClick}
          pageRangeDisplayed={5}
          renderOnZeroPageCount={null}
          pageClassName="page-item"
          pageLinkClassName="page-link"
          previousClassName="page-item"
          previousLinkClassName="page-link"
          nextClassName="page-item"
          nextLinkClassName="page-link"
          breakClassName="page-item"
          breakLinkClassName="page-link"
          containerClassName="pagination"
          activeClassName="active"
        />
      </PaginateContainer>
      <Newsletter />
      <Footer />
      <ScrollToTop />
    </Container>
  );
};

export default ProductList;
