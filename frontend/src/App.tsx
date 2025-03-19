import { Outlet } from "react-router-dom";
import "./App.css";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";

const App = () => {
  return (
    <>
      <Navbar />
      <div className="min-h-[50vh] lg:min-h-screen h-full bg-gray-primary">
        {/* <Breadcrumbs /> */}
          <Outlet />
      </div>
      <Footer/>
    </>
  );
};

export default App;
