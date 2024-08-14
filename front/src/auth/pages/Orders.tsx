import { useContext, useEffect, useState } from 'react';
import { Order } from "../../interfaces/Order";
import { OrdersContext } from '../../context/OrdersContext';
import { OrderPage } from './OrderPage';
// import { OrderRequest } from '../../interfaces/OrderRequest';
import { InputNameModal } from '../../components/InputNameModal';
import ImageUploader from '../../components/ImageUploader';

export const Orders = () => {
    const {orders, getOrders, handleSaveTomography} = useContext(OrdersContext);
    const [show, setShow] = useState(false);
    const [uploadedImage, setUploadedImage] = useState<string | ArrayBuffer | null>(null);

    useEffect(() => { 
        const fetch = async () => {
            // await getOrders();
        };
        fetch();
    }, []);

    const handleAcceptModal = async (type: string, nameOrId: string) => {
        if (type === 'new') await handleSaveTomography({items: [], name: nameOrId});
        if (type === 'other') await handleSaveTomography({items: [], id: nameOrId});
    };

    return (
        <div className="container mt-4">
            <h1 className="mb-4">Solicitar un Informe</h1>
            
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
