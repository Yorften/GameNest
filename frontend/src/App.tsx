import { Outlet, useLocation } from "react-router-dom";
import "./App.css";
import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import { ToastContainer } from "react-toastify";
import { useAppDispatch } from "./app/hooks";
import { useEffect } from "react";
import { updateUserInstallationId } from "./features/auth/authSlice";

const App = () => {
  const location = useLocation();
  const dispatch = useAppDispatch();
  // Check if current path starts with "/dashboard"
  const isDashboard = location.pathname.startsWith("/dashboard");


  useEffect(() => {
    function handleMessage(event: MessageEvent) {      
      if (event.origin !== window.location.origin) return;
      if (event.data?.type === 'github-installation') {

        const { installationId } = event.data;
        dispatch(updateUserInstallationId(installationId));

      }
    }

    window.addEventListener('message', handleMessage);
    return () => window.removeEventListener('message', handleMessage);
  }, [dispatch]);

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
