import { useEffect, useState } from "react";
import { useDispatch } from "react-redux";
import { logout } from "../features/auth/authSlice";

const useJwtMonitor = () => {
  const dispatch = useDispatch();
  const [hasJwtChanged, setHasJwtChanged] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      if (!localStorage.getItem("token")) {
        setHasJwtChanged(true);
        dispatch(logout());
      } else {
        setHasJwtChanged(false);
      }
    }, 1000);

    return () => clearInterval(interval);
  }, []);

  return hasJwtChanged;
};

export default useJwtMonitor;
