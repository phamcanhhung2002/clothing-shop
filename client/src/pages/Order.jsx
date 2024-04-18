import styled from "styled-components";
import Announcement from "../components/Announcement";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import { mobile } from "../responsive";
import { useEffect, useState } from "react";
import { userRequest } from "../requestMethods";
import { useLocation } from "react-router-dom";
import { Link } from "react-router-dom";

const Container = styled.div``;

const Wrapper = styled.div`
  padding: 20px;
  ${mobile({ padding: "10px" })}
`;

const Title = styled.h1`
  font-weight: 300;
  text-align: center;
`;

const Top = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
`;

const TopButton = styled.button`
  padding: 10px;
  font-weight: 600;
  cursor: pointer;
  border: ${(props) => props.type === "filled" && "none"};
  background-color: ${(props) =>
    props.type === "filled" ? "black" : "transparent"};
  color: ${(props) => props.type === "filled" && "white"};
`;

const Bottom = styled.div`
  display: flex;
  justify-content: space-between;
  ${mobile({ flexDirection: "column" })}
`;

const Info = styled.div`
  flex: 3;
`;

const Product = styled.div`
  display: flex;
  justify-content: space-between;
  padding-bottom: 2rem;
  ${mobile({ flexDirection: "column" })}
`;

const ProductDetail = styled.div`
  flex: 2;
  display: flex;
`;

const Image = styled.img`
  width: 200px;
`;

const Details = styled.div`
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-around;
`;

const ProductName = styled.span``;

const ProductId = styled.span``;

const ProductColorContainer = styled.div`
  display: flex;
  flex-direction: row;
`;

const ProductColor = styled.div`
  margin-left: 5px;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background-color: ${(props) => props.color};
  ${({ color }) =>
    color === "white"
      ? "border-width: 1px; border-color: gray; border-style: solid;"
      : ""}
`;

const ProductSize = styled.span``;

const PriceDetail = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

const ProductAmountContainer = styled.div`
  display: flex;
  align-items: center;
  margin-bottom: 20px;
`;

const ProductAmount = styled.div`
  font-size: 24px;
  margin: 5px;
  ${mobile({ margin: "5px 15px" })}
`;

const ProductPrice = styled.div`
  font-size: 30px;
  font-weight: 200;
  ${mobile({ marginBottom: "20px" })}
`;

const Hr = styled.hr`
  background-color: #eee;
  border: none;
  height: 1px;
`;

const Summary = styled.div`
  flex: 1;
  border: 0.5px solid lightgray;
  border-radius: 10px;
  padding: 20px;
  height: 60vh;
`;

const SummaryTitle = styled.h1`
  font-weight: 200;
`;

const SummaryItem = styled.div`
  margin: 30px 0px;
  display: flex;
  justify-content: space-between;
  font-weight: ${(props) => props.type === "total" && "500"};
  font-size: ${(props) => props.type === "total" && "24px"};
`;

const SummaryItemText = styled.span``;

const SummaryItemPrice = styled.span``;

const Order = () => {
  const location = useLocation();
  const id = location.pathname.split("/")[2];
  const [order, setOrder] = useState({});

  useEffect(() => {
    const getOrderDetail = async () => {
      try {
        const { data } = await userRequest.get("/users/orders/" + id);
        setOrder(data);
      } catch (error) {
        console.log(error);
      }
    };
    getOrderDetail();
  }, [id]);
  return (
    <Container>
      <Navbar />
      <Announcement />
      <Wrapper>
        <Title>YOUR ORDER</Title>
        <Top>
          <Link to="/">
            <TopButton>CONTINUE SHOPPING</TopButton>
          </Link>
        </Top>
        <Bottom>
          <Info>
            {order?.products &&
              order.products.map((orderProduct) => {
                const product = orderProduct.product;
                return (
                  <Product key={product.id}>
                    <ProductDetail>
                      <Image src={product.img} />
                      <Details>
                        <ProductName>
                          <b>Product:</b> {product.title}
                        </ProductName>
                        <ProductId>
                          <b>ID:</b> {product.id}
                        </ProductId>
                        <ProductColorContainer>
                          <b>Color:</b>{" "}
                          <ProductColor color={orderProduct.color} />
                        </ProductColorContainer>
                        <ProductSize>
                          <b>Size:</b> {orderProduct.size}
                        </ProductSize>
                        <ProductId>
                          <b>Unit Price:</b> $ {product.price}
                        </ProductId>
                      </Details>
                    </ProductDetail>
                    <PriceDetail>
                      <ProductAmountContainer>
                        <ProductAmount>{product.quantity}</ProductAmount>
                      </ProductAmountContainer>
                      <ProductPrice>
                        $ {product.price * orderProduct.quantity}
                      </ProductPrice>
                    </PriceDetail>
                  </Product>
                );
              })}
            <Hr />
          </Info>
          <Summary>
            <SummaryTitle>ORDER SUMMARY</SummaryTitle>
            <SummaryItem>
              <SummaryItemText>Status</SummaryItemText>
              <SummaryItemPrice>{order.status}</SummaryItemPrice>
            </SummaryItem>
            <SummaryItem>
              <SummaryItemText>City</SummaryItemText>
              <SummaryItemPrice>{order.city}</SummaryItemPrice>
            </SummaryItem>
            <SummaryItem>
              <SummaryItemText>Country</SummaryItemText>
              <SummaryItemPrice>{order.country}</SummaryItemPrice>
            </SummaryItem>
            <SummaryItem>
              <SummaryItemText>Line 1</SummaryItemText>
              <SummaryItemPrice>{order.line1}</SummaryItemPrice>
            </SummaryItem>
            <SummaryItem>
              <SummaryItemText>Postal Code</SummaryItemText>
              <SummaryItemPrice>{order.postalCode}</SummaryItemPrice>
            </SummaryItem>
            <SummaryItem type="total">
              <SummaryItemText>Total</SummaryItemText>
              <SummaryItemPrice>$ {order.amount}</SummaryItemPrice>
            </SummaryItem>
          </Summary>
        </Bottom>
      </Wrapper>
      <Footer />
    </Container>
  );
};

export default Order;
