import { useEffect } from "react";

import { Toaster } from 'sonner'
import { Outlet, ScrollRestoration, useLocation } from "react-router-dom";

import Navbar from "./components/Navbar";
import Footer from "./components/Footer";
import useJwtMonitor from "./hooks/useJwtMonitor";
import { useAppDispatch, useAppSelector } from "./app/hooks";
import { selectCurrentUser, updateUserInstallationId } from "./features/auth/authSlice";

import "./App.css";

const App = () => {
  const location = useLocation();
  const dispatch = useAppDispatch();
  const user = useAppSelector(selectCurrentUser)

  // Check if current path starts with "/dashboard"
  const isDashboard = location.pathname.startsWith("/dashboard");

  useJwtMonitor();

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
      <ScrollRestoration />
      {!isDashboard && <Navbar />}
      <div className="min-h-screen h-full bg-gray-primary">
        {/* <Breadcrumbs /> */}
        <Outlet />
      </div>
      {!isDashboard && <Footer />}
      <Toaster />

    </>
  );
};

export default App;
function useCookieMonitor() {
  throw new Error("Function not implemented.");
}

