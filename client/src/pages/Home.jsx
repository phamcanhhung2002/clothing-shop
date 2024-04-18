import React, { useState } from "react";
import Announcement from "../components/Announcement";
import Categories from "../components/Categories";
import Footer from "../components/Footer";
import Navbar from "../components/Navbar";
import Newsletter from "../components/Newsletter";
import Products from "../components/Products";
import Slider from "../components/Slider";
import { useEffect } from "react";
import { publicRequest } from "../requestMethods";

const Home = () => {
  const [products, setProducts] = useState([]);
  const [categories, setCategories] = useState([]);

  const getProductsAndCategories = async () => {
    try {
      const productsPromise = publicRequest.get("/products", {
        params: {
          size: 5,
        },
      });

      const categoriesPromise = publicRequest.get("categories");

      const [products, categories] = await Promise.all([
        productsPromise,
        categoriesPromise,
      ]);

      setProducts(products.data.content);
      setCategories(categories.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getProductsAndCategories();
  }, []);

  return (
    <div>
      <Announcement />
      <Navbar />
      <Slider />
      <Categories categories={categories} />
      <Products products={products} />
      <Newsletter />
      <Footer />
    </div>
  );
};

export default Home;
