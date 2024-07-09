import { useContext, useEffect, useState } from 'react';
import { Order } from "../../interfaces/Order";
import { OrdersContext } from '../../context/OrdersContext';
import { OrderPage } from './OrderPage';
// import { OrderRequest } from '../../interfaces/OrderRequest';
import { InputNameModal } from '../../components/InputNameModal';
import ImageUploader from '../../components/ImageUploader';

export const Orders = () => {
    const {orders, getOrders, handleCreateOrder} = useContext(OrdersContext);
    const [show, setShow] = useState(false);
    const [uploadedImage, setUploadedImage] = useState<string | ArrayBuffer | null>(null);

    useEffect(() => { 
        const fetch = async () => {
            await getOrders();
        };
        fetch();
    }, []);

    const handleAcceptModal = async (type: string, nameOrId: string) => {
        if (type === 'new') await handleCreateOrder({items: [], name: nameOrId});
        if (type === 'other') await handleCreateOrder({items: [], id: nameOrId});
    };

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Solicitar un Informe</h1>
            {
                orders.length === 0 ? (
                    <p className="my-3">Para solicitar un informe, importe una imagen de una Tomografía.</p>
                ) :
                <ul className="list-group">{
                    orders.map((o: Order) => (
                        <OrderPage key={o.id} id={o.id} name={o.name} items={o.items} userId={o.userId} status={o.status} hasItems={o.hasItems}/>
                    ))
                }
                </ul>
            }

            <ImageUploader setUploadedImage={setUploadedImage} />

            <button className="btn btn-success my-3"
                onClick={() => setShow(true)}
                disabled={!uploadedImage}
            >
                Solicitar informe
            </button>

            <InputNameModal show={show} handleClose={() => setShow(false)} handleFunc={handleAcceptModal} ></InputNameModal>
        </div>
    );
};
