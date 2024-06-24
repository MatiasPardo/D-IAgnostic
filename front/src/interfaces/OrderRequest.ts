import { ItemRequest } from "./ItemRequest";

export interface OrderRequest {
    id: string,
    items: ItemRequest[],
    name: string
}