import { Outlet } from "react-router-dom";
import "./App.css";
import Navbar from "./components/Navbar";

const App = () => {
  return (
    <>
      <Navbar />
      <div className="min-h-[50vh] lg:min-h-screen h-full">
        {/* <Breadcrumbs /> */}
        <Outlet />
      </div>
    </>
  );
};

export default App;
