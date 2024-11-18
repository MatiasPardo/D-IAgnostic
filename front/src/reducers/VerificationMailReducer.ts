export const VerificationMailReducer = (state = {}, action: any) => {
    switch (action.type) {
      case 'SEND_VERIFICATION_MAIL':
        return action.payload;
      default:
        return state;
    }
  };
  