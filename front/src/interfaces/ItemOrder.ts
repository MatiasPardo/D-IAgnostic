import {Item} from "./Item";

export interface ItemOrder {
    item: Item,
    quantity: number,
    total: number
}