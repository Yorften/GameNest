import { Outlet } from "react-router-dom";
import "./App.css";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import { ToastContainer } from "react-toastify";

const App = () => {
  return (
    <>
      <Navbar />
      <div className="min-h-[50vh] lg:min-h-screen h-full bg-gray-primary">
        {/* <Breadcrumbs /> */}
          <Outlet />
      </div>
      <Footer/>
      <ToastContainer />
    </>
  );
};

export default App;
