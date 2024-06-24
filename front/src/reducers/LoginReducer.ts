export const LoginReducer = (state = {}, action: any) => {
    switch (action.type) {
        case 'login':
            return {
                isAuth: true,
                user: action.payload
            }
        case 'logout':
            return {
                isAuth: false,
            }
        default:
            return state;
    }
}
