import "./App.css";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import { routes } from "./routes";
import DefaultLayout from "./layout";
import RequireAuth from "./auth";
import Login from "./pages/login/Login";

function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<RequireAuth />}>
          {routes.map((route, index) => (
            <Route
              key={index}
              path={route.path}
              element={<DefaultLayout>{route.element}</DefaultLayout>}
            />
          ))}
        </Route>
        <Route path="/login" element={<Login />} />
      </Routes>
    </BrowserRouter>
  );
}

export default App;
