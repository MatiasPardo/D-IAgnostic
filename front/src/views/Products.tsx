import React, { useContext, useEffect, useState } from "react";
import { ItemOrder } from "../interfaces/ItemOrder"
import { Product } from "../components/Product"
import { AlertOk } from "../components/SweetAlert";
import { useParams, useSearchParams } from "react-router-dom";
import { findAll } from "../services/ProductsService";
import { findOrders } from "../services/OrdersService";
import {Order} from "../interfaces/Order";
import {OrdersContext} from "../context/OrdersContext";

export const Products = () => {
    const defaultResponse: ItemOrder[] = []
    const [searchParams, setSearchParams] = useSearchParams()
    const [products, setProducts] = useState(defaultResponse);
    const {orders, getOrders} = useContext(OrdersContext);
    const [selectedOrder, setSelectedOrder] = useState('');

    useEffect(() => {
        const fetch = async () => {
            await getOrders()
        }
        fetch()
    }, [])

    const getProducts = async (id: string | null) => {
        const p = await findAll(id)
        setProducts(p)
    }

    useEffect(() => {
        getProducts(searchParams.get('order_id'))
        setSelectedOrder(searchParams.get('order_id') ?? '')
    }, []);

    // Actualizar productos cuando cambie el pedido seleccionado
    useEffect(() => {
        if (selectedOrder) {
            getProducts(selectedOrder);
        }
    }, [selectedOrder]);


    // Manejar el cambio en la selección del pedido
    const handleOrderChange = async (event:any) => {
        const orderId = event.target.value;
        let items = await findAll(event.target.value)
        setProducts(items)
        setSearchParams({order_id: event.target.value})
    };

    return (
        <div className="p-4">
            <div className="form-group float-right">
                        <label htmlFor="orderSelect">Pedido:</label>
                        <select
                            className="form-control"
                            id="orderSelect"
                            onChange={handleOrderChange}
                        >
                            <option value="">Seleccionar un pedido...</option>
                            {orders.map((order:Order) => (
                                <option selected={order.id == selectedOrder} key={order.id} value={order.id}>
                                    {order.name}
                                </option>
                            ))}
                        </select>
</div>
            <h1>Productos</h1>
            
            <hr></hr>
            <div className="row rows-cols-1 row-cols-md-6">{
                products.map(io => (
                    <Product
                        key={io.item.id}
                        item={io.item}
                        // name={io.item.name}
                        // description={'Descripción del Producto'}
                        // price={io.item.price}
                        // picture={io.item.picture}
                        quantity={io.quantity}
                        total = {io.total}
                        />
                ))
            }
            </div>
        </div>
    );
}

