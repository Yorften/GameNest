import { createBrowserRouter, Navigate } from "react-router";
import App from "./App";
import Home from "./pages/Home";
import About from "./pages/About";
import NotFound from "./pages/NotFound";
import ProtectedRoute from "./components/guards/ProtectedRoute";
import Dashboard from "./components/layouts/Dashboard";
import HomeDashboard from "./pages/dashboard/HomeDashboard";
import GameDetails from "./pages/dashboard/GameDetails";
import Games from "./pages/Games";
import GamePage from "./pages/GamePage";
import AdminRoute from "./components/guards/AdminRoute";
import Categories from "./pages/dashboard/Categories";
import Tags from "./pages/dashboard/Tags";
import UserGames from "./pages/dashboard/UserGames";
import NewGame from "./pages/dashboard/NewGame";
import CallBackPage from "./pages/CallBackPage";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <App />,
    children: [
      {
        path: "",
        element: <Home />,
      },
      {
        path: "/about",
        element: <About />,
      },
      {
        path: "/games",
        element: <Games />,
      },
      {
        path: "/games/:id",
        element: <GamePage />,
      },
      {
        path: "/dashboard",
        element: (
          <ProtectedRoute>
            <Dashboard />
          </ProtectedRoute>
        ),
        children: [
          {
            path: "",
            element: <Navigate to={"stats"} />,
          },
          {
            index: true,
            path: "stats",
            element: <HomeDashboard />,
          },
          {
            path: "games",
            element: <UserGames />,
          },
          {
            path: "games/new",
            element: <NewGame />,
          },
          {
            path: "games/:id",
            element: <GameDetails />,
          },
          {
            path: "categories",
            element:
              <AdminRoute>
                <Categories />
              </AdminRoute>,
          },
          {
            path: "tags",
            element:
              <AdminRoute>
                <Tags />
              </AdminRoute>,
          },
          {
            path: "*",
            element: <NotFound />,
          },
        ]
      },
      {
        path: "*",
        element: <NotFound />,
      },
    ],

  },

  {
    path: "callback",
    element: <CallBackPage />,
  }
]);
