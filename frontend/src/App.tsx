import { Outlet, useLocation } from "react-router-dom";
import "./App.css";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import { ToastContainer } from "react-toastify";

const App = () => {
  const location = useLocation();
  // Check if current path starts with "/dashboard"
  const isDashboard = location.pathname.startsWith("/dashboard");
  return (
    <>
      {!isDashboard && <Navbar />}
      <div className="min-h-screen h-full bg-gray-primary">
        {/* <Breadcrumbs /> */}
        <Outlet />
      </div>
      {!isDashboard && <Footer />}
      <ToastContainer />
    </>
  );
};

export default App;
