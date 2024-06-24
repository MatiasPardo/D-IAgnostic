import { ItemOrder } from "../interfaces/ItemOrder";

export const ProductsReducer = (state: ItemOrder[] = [], action: any) => {
    switch (action.type) {
        case 'LOAD_PRODUCTS':
            return action.payload
        default:
            break;
    }
}
