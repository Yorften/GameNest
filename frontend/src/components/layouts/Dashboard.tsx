import { useSelector } from "react-redux";
import { Link, NavLink, Outlet, useNavigate } from "react-router";
import { logout, selectCurrentUser } from "../../features/auth/authSlice";
import Breadcrumbs from "../BreadCrumbs";
import { Avatar, Dropdown } from "flowbite-react";
import { useAppDispatch } from "../../app/hooks";
import CssBaseline from "@mui/material/CssBaseline";

type Props = {};

export default function Dashboard({ }: Props) {
  const user = useSelector(selectCurrentUser);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const isAdmin = user?.role.name.startsWith("ROLE_ADMIN");

  const toggleNavBar = () => {
    const mobileNavbar = document.getElementById("mobile-navbar");
    mobileNavbar?.classList.toggle("hidden");
  };
  return (
    <>
      <div className="relative h-screen bg-dashboard-secondary">
        {/* <!--header--> */}
        <nav className="border-b h-[8%] !z-50 lg:border-primary bg-dashboard-primary shadow-xl py-3.5 px-6 w-full fixed">
          <div className="flex items-center justify-between gap-1 sm:gap-6 lg:flex-row flex-col">
            <div className="flex justify-between items-center lg:w-auto w-full">
              <Link to="/" className="flex items-center">
                <img src="/assets/GameNest_logo.png" className="mr-3 h-6 md:h-12" alt="GameNest Logo" />
                <span className="self-center whitespace-nowrap text-base md:text-xl font-semibold text-white">
                  GameNest
                </span>
              </Link>
              <button
                id="navbar-toggle"
                type="button"
                className="inline-flex text-white items-center p-2 ml-3 text-sm rounded-lg lg:hidden hover:bg-gray-500/50 focus:outline-none"
                aria-controls="navbar-default"
                aria-expanded="false"
                onClick={toggleNavBar}
              >
                <span className="sr-only">Open main menu</span>
                <svg
                  className="w-6 h-6"
                  aria-hidden="true"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    fillRule="evenodd"
                    d="M3 5a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 10a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zM3 15a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z"
                    clipRule="evenodd"
                  ></path>
                </svg>
              </button>
            </div>
            <div
              id="mobile-navbar"
              className="hidden lg:flex flex-row w-full flex-1 shadow-sm lg:shadow-none bg-dashboard-primary/85 rounded-xl py-4 lg:py-0"
            >
              <ul className="text-center flex lg:flex-row flex-col lg:gap-2 xl:gap-4 gap-2 items-center lg:ml-auto md:pr-40">
                <li>
                  <NavLink
                    to={"stats"}
                    className={({ isActive }) =>
                      (isActive ? 'text-primary' : 'text-white') + ' py-1.5 px-3 transition-all duration-500 ease-in-out text-sm font-semibold rounded-md'}
                  >
                    Dashboard
                  </NavLink>
                </li>
                <li>
                  <NavLink
                    to={"games"}
                    className={({ isActive }) =>
                      (isActive ? 'text-primary' : 'text-white') + ' py-1.5 px-3 transition-all duration-500 ease-in-out text-sm font-semibold rounded-md'}
                  >
                    My Games
                  </NavLink>
                </li>
                {isAdmin && (
                  <>
                    <li>
                      <NavLink
                        to={"categories"}
                        className={({ isActive }) =>
                          (isActive ? 'text-primary' : 'text-white') + ' py-1.5 px-3 transition-all duration-500 ease-in-out text-sm font-semibold rounded-md'}
                      >
                        Categories
                      </NavLink>
                    </li>
                    <li>
                      <NavLink
                        to={"tags"}
                        className={({ isActive }) =>
                          (isActive ? 'text-primary' : 'text-white') + ' py-1.5 px-3 transition-all duration-500 ease-in-out text-sm font-semibold rounded-md'}
                      >
                        Tags
                      </NavLink>
                    </li>
                  </>
                )}
              </ul>
              <div className="text-center hidden lg:flex items-center gap-1  sm:gap-4 lg:ml-auto">
                <Dropdown
                  arrowIcon={false}
                  inline
                  label={<Avatar alt="User settings" img="/assets/default_user.png" rounded />}
                >
                  <Dropdown.Header>
                    <span className="block text-sm">{user?.username}</span>
                    <span className="block truncate text-sm font-medium">{user?.email}</span>
                  </Dropdown.Header>
                  <Dropdown.Item onClick={() => navigate("/dashboard")}>Dashboard</Dropdown.Item>
                  <Dropdown.Divider />
                  <Dropdown.Item onClick={() => dispatch(logout())}> Sign out </Dropdown.Item>
                </Dropdown>
              </div>
            </div>
          </div>
        </nav>
        {/* <!--main content--> */}
        <div className="pt-[70px] h-full overflow-hidden">
          <div className=" flex flex-col py-4 lg:px-8 px-3 h-[9%] bg-dashboard-secondary">
            <Breadcrumbs className="text-white" />
          </div>
          <div className="w-full h-full min-h-[91%] px-4 pb-4 rounded-3xl">
            <div className="border-primary bg-dashboard-primary border h-[91%] rounded-xl text-white p-4">
              <Outlet />
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
