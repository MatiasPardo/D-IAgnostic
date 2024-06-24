import React from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';
import {Home, Login, Register} from '../auth/pages';
import {Orders} from '../auth/pages/Orders';
import {Navbar} from '../components/navbar/Navbar';
import { Products } from '../views'
import { OrdersProvider } from '../context/OrdersProvider';

class ProtectedRoute extends React.Component<{ element: any }> {
    render() {
        let {element} = this.props;
        console.log('token: ', localStorage.getItem('token'));
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
                            path="/products" 
                            element={<ProtectedRoute element={<Products />} />}
                        />

                        {/* Redirección desde la raíz ("/") a la página de inicio ("/home") */}
                        <Route path="/" element={<Navigate to="/home" />} />

                        <Route path="/items" element={<Products />} />
                    </Routes>
                </div>
            </OrdersProvider>
        </>
    );
};

