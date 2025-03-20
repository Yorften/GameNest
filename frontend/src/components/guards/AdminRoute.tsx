import { useSelector } from "react-redux";
import { Navigate } from "react-router-dom";
import { selectCurrentUser } from "../../features/auth/authSlice";

interface AdminRouteProps {
    children: JSX.Element;
}

const AdminRoute = ({ children }: AdminRouteProps) => {
    const user = useSelector(selectCurrentUser);

    if (!user || user.role?.name !== "ROLE_ADMIN") {
        return <Navigate to="/dashboard" replace />;
    }

    return children;
};

export default AdminRoute;
