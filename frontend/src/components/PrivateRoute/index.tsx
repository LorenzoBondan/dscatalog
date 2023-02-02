import { Redirect, Route } from 'react-router-dom';
import { isAuthenticated } from 'util/requests';


type Props = {
  children: React.ReactNode;
  path: string;
};


const PrivateRoute = ({ children, path }: Props) => {


  return (
    <Route
      path={path}
      render={({location}) =>
        isAuthenticated() ? <>children</> : <Redirect to={{
          pathname: "/admin/auth/login",
          state: { from : location}
        }} /> // renderiza o children passado caso esteja autenticado, se não estiver, joga p tela de login
      }
    />
  );
};

export default PrivateRoute;