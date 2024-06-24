import { Order } from "../interfaces/Order";

export const OrdersReducer = (state: Order[] = [], action: any) => {
    switch (action.type) {
        case 'LOAD_ORDERS':
            return [...action.payload]
        case 'UPDATE_ORDERS':
            return [...state, {
                id: action.payload,
                name: '',
                items: []
            }]
        default:
            break;
    }
}

