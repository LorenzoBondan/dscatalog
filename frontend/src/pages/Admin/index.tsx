import PrivateRoute from "components/PrivateRoute";
import { Switch } from "react-router-dom";
import Categories from "./Categories";
import Navbar from "./Navbar";
import Products from "./Products";
import './styles.css';
import Users from "./Users";

// privateroute -> componente criado para verificar se o usuário está autenticado para ter acesso às rotas

const Admin = () => {
    return (
        <div className="admin-container">
            <Navbar />
            <div className="admin-content">
                <Switch>
                    <PrivateRoute path="/admin/products">
                        <Products/>
                    </PrivateRoute>
                    <PrivateRoute path="/admin/categories">
                        <Categories/>
                    </PrivateRoute>
                    <PrivateRoute path="/admin/users">
                        <Users />
                    </PrivateRoute>
                </Switch>
            </div>
        </div>
    );
};

export default Admin;