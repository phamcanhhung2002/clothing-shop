import { createSlice } from "@reduxjs/toolkit";

const cartSlice = createSlice({
  name: "cart",
  initialState: {
    products: [],
    quantity: 0,
    total: 0,
  },
  reducers: {
    addProduct: (state, action) => {
      const updatedProductIndex = state.products.findIndex(
        (product) =>
          product.id === action.payload.id &&
          product.color === action.payload.color &&
          product.size === action.payload.size
      );
      if (updatedProductIndex !== -1) {
        state.products[updatedProductIndex].quantity += action.payload.quantity;
      } else {
        state.quantity += 1;
        state.products.push(action.payload);
      }
      state.total += action.payload.price * action.payload.quantity;
    },
    subProduct: (state, action) => {
      const updatedProductIndex = state.products.findIndex(
        (product) =>
          product.id === action.payload.id &&
          product.color === action.payload.color &&
          product.size === action.payload.size
      );
      if (updatedProductIndex !== -1) {
        state.products[updatedProductIndex].quantity -= action.payload.quantity;

        state.products = state.products.filter(
          (product) => product.quantity > 0
        );

        state.total = state.products.reduce(
          (total, product) => total + product.quantity * product.price,
          0
        );

        state.quantity = state.products.length;
      }
    },
    clearCart: (state) => {
      state.products = [];
      state.quantity = 0;
      state.total = 0;
    },
  },
});

export const { addProduct, subProduct, clearCart } = cartSlice.actions;
export default cartSlice.reducer;
