import PrivateRoute from "components/PrivateRoute";
import { Route, Switch } from "react-router-dom";
import Navbar from "./Navbar";
import './styles.css';
import Users from "./User";

// privateroute -> componente criado para verificar se o usuário está autenticado para ter acesso às rotas

const Admin = () => {
    return (
        <div className="admin-container">
            <Navbar />

            <div className="admin-content">
                
                <Switch>
                    <PrivateRoute path="/admin/products">
                        <h1>product CRUD</h1>
                    </PrivateRoute>

                    <PrivateRoute path="/admin/categories">
                        <h1>Category CRUD</h1>
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