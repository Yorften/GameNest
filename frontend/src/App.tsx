import { Outlet } from "react-router-dom"
import "./App.css"
import Navbar from "./components/Navbar"
import Breadcrumbs from "./components/BreadCrumbs"

const App = () => {
  return (
    <>
      <>
        <Navbar />
        <div className="flex">
          {/* <ProtectedRoute> */}
          {/* </ProtectedRoute> */}
          <div>
            {/* <Breadcrumbs /> */}
            <Outlet />
          </div>
        </div>
      </>
    </>
  )
}

export default App
