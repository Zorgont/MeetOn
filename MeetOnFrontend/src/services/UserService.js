import axios from 'axios'
import authHeader from "./AuthHeader";

const USER_API_BASE_URL = "https://meetonapi.herokuapp.com/api/v1/users";
const API_URL = 'https://meetonapi.herokuapp.com/api/v1/test/';
class UserService {
    getUsers() {
        return axios.get(USER_API_BASE_URL,{ headers: authHeader() });
    }

    createUser(user) {
        return axios.post(USER_API_BASE_URL, user , { headers: authHeader() });
    }

    getUserById(userId) {
        return axios.get(USER_API_BASE_URL + '/' + userId , { headers: authHeader() });
    }

    getUserById(userId) {
        return axios.get(USER_API_BASE_URL + '/' + userId , { headers: authHeader() });
    }

    updateUser(user, userId) {
        return axios.put(USER_API_BASE_URL + '/' + userId, user , { headers: authHeader() });
    }

    deleteUser(userId) {
        return axios.delete(USER_API_BASE_URL + '/' + userId , { headers: authHeader() });
    }
    getPublicContent() {
        return axios.get(API_URL + 'all');
    }


    getUserBoard() {
        return axios.get(API_URL + 'user', { headers: authHeader() });

    }

    getModeratorBoard() {
        return axios.get(API_URL + 'mod', { headers: authHeader() });
    }

    getAdminBoard() {
        return axios.get(API_URL + 'admin', { headers: authHeader() });
    }

    updateUserSettings(user, userId) {
        return axios.put(USER_API_BASE_URL + '/changeSettings/' + userId , user , { headers: authHeader()})
    }
}

export default new UserService()