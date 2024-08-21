import { Tomography } from "../interfaces/Tomography";


export const TomographiesReducer = (state: Tomography[] = [], action: any) => {
    switch (action.type) {
      case 'LOAD_TOMOGRAPHIES':
        return action.payload;
      default:
        return state;
    }
  };
  