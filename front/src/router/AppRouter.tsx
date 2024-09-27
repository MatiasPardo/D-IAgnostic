import React from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';
import {Home, Login, Register} from '../auth/pages';
import {Orders} from '../auth/pages/Orders';
import {Navbar} from '../components/navbar/Navbar';
import { Tomographies } from '../views'
import { OrdersProvider } from '../context/OrdersProvider';
import { TomographiesProvider } from '../context/TomographiesProvider';

class ProtectedRoute extends React.Component<{ element: any }> {
    render() {
        let {element} = this.props;
        if (localStorage.getItem('token') !== null) {
            return element;
        } else {
            return <Navigate to="/login"/>;
        }
    }
}

export const AppRouter = () => {

    return (
        <>
            <Navbar />

            <OrdersProvider>
                <TomographiesProvider>
                    <div className="containers">
                        <Routes>
                            {/* Rutas públicas */}
                            <Route path="/login" element={<Login />} />
                            <Route path="/register" element={<Register />} />
                            <Route path="/home" element={<Home />} />

                            {/* Rutas protegidas (requiere autenticación) */}
                            <Route
                                path="/orders"
                                element={<ProtectedRoute element={<Orders />} />}
                            />
                            <Route 
                                path="/tomographies" 
                                element={<ProtectedRoute element={<Tomographies />} />}
                            />

                            {/* Redirección desde la raíz ("/") a la página de inicio ("/home") */}
                            <Route path="/" element={<Navigate to="/home" />} />
                        </Routes>
                    </div>
                </TomographiesProvider>
            </OrdersProvider>
        </>
    );
};

