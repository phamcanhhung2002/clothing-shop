import User from "./pages/user/User";
import NewUser from "./pages/newUser/NewUser";
import ProductList from "./pages/productList/ProductList";
import Product from "./pages/product/Product";
import NewProduct from "./pages/newProduct/NewProduct";
import Home from "./pages/home/Home";
import UserList from "./pages/userList/UserList";

export const routes = [
  {
    path: "/",
    element: <Home />,
  },
  {
    path: "/user/:userId",
    element: <User />,
  },
  {
    path: "/newUser",
    element: <NewUser />,
  },
  {
    path: "/products",
    element: <ProductList />,
  },
  {
    path: "/product/:productId",
    element: <Product />,
  },
  {
    path: "/newproduct",
    element: <NewProduct />,
  },
  {
    path: "/users",
    element: <UserList />,
  },
];
