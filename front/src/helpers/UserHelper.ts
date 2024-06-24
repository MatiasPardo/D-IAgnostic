import { User } from "../interfaces/User"

export const getUser = ():User => {
    const jwt = localStorage.getItem('token') ?? '';
    const decodedJwt = decode(jwt);
    const user: User = JSON.parse(decodedJwt);  
    return user;   
}

const decode = (token: string) => {
    const base64Url = token.split('.')[1];
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return jsonPayload;
}