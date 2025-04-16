import { Avatar, Dropdown, Navbar } from "flowbite-react";
import { Link, useLocation, useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../app/hooks";
import { logout, selectCurrentUser } from "../features/auth/authSlice";
import Button from "./miscs/Button";
import { useState } from "react";

import { LoginModal } from "./LoginModal";
import { RegisterModal } from "./RegisterModal";

export default function NavBar() {
  const navigate = useNavigate();
  const location = useLocation();
  const dispatch = useAppDispatch();
  const user = useAppSelector(selectCurrentUser);

  const [isLoginOpen, setLoginOpen] = useState(false);
  const [isRegisterOpen, setRegisterOpen] = useState(false);

  const isHome = location.pathname === "/";

  return (
    <>
      <Navbar
        className={`shadow-xl fixed z-20 w-full ${isHome ? "bg-transparent" : "bg-nav-primary"
          } backdrop-blur-sm`}
        fluid
      >
        <Navbar.Brand as={Link} to="/">
          <img src="/assets/GameNest_logo.png" className="mr-3 h-12" alt="Flowbite React Logo" />
          <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white text-white">
            GameNest
          </span>
        </Navbar.Brand>
        <div className="flex md:order-2 !z-50">
          {user ? (
            <Dropdown
              arrowIcon={false}
              inline
              label={<Avatar alt="User settings" img="/assets/default_user.png" rounded />}
            >
              <Dropdown.Header>
                <span className="block text-sm">{user.username}</span>
                <span className="block truncate text-sm font-medium">{user.email}</span>
              </Dropdown.Header>
              <Dropdown.Item onClick={() => navigate("/dashboard")}>Dashboard</Dropdown.Item>
              <Dropdown.Divider />
              <Dropdown.Item onClick={() => dispatch(logout())}> Sign out </Dropdown.Item>
            </Dropdown>
          ) : (
            <div className="flex items-center space-x-4">
              <Button
                onClick={() => setLoginOpen(true)}
                className="cursor-pointer text-white !text-base font-medium !px-4"
                title="Login"
              />
              <Button
                onClick={() => setRegisterOpen(true)}
                className="cursor-pointer text-white !text-base font-medium !px-4"
                title="Register"
              />
            </div>
          )}
          <Navbar.Toggle />
        </div>
        <Navbar.Collapse className="mr-24 *:!text-[16px]">
          <Navbar.Link as={Link} to="/" className="cursor-pointer text-white hover:!text-[#5696c3] font-medium">
            Home
          </Navbar.Link>
          <Navbar.Link as={Link} to="/games" className="cursor-pointer text-white hover:!text-[#5696c3] font-medium">
            Games
          </Navbar.Link>
          <Navbar.Link
            onClick={() => navigate("/#about_us")}
            className="cursor-pointer text-white hover:!text-[#5696c3] font-medium">
            About
          </Navbar.Link>
          <Navbar.Link
            onClick={() => navigate("/#services")}
            className="cursor-pointer text-white hover:!text-[#5696c3] font-medium">
            Services
          </Navbar.Link>
          <Navbar.Link
            onClick={() => navigate("/#contact_us")}
            className="cursor-pointer text-white hover:!text-[#5696c3] font-medium">
            Contact
          </Navbar.Link>
        </Navbar.Collapse>
      </Navbar >
      <LoginModal isOpen={isLoginOpen} onClose={() => setLoginOpen(false)} />
      <RegisterModal isOpen={isRegisterOpen} onClose={() => setRegisterOpen(false)} />
    </>
  );
}
