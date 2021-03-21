import axios from 'axios'
import authHeader from "./AuthHeader";

const USER_API_BASE_URL = "http://localhost:8080/api/v1/users";
const API_URL = 'http://localhost:8080/api/v1/test/';
class UserService {
    getUsers() {
        let users = axios.get(USER_API_BASE_URL);
        return users;
    }

    createUser(user) {
        return axios.post(USER_API_BASE_URL, user);
    }

    getUserById(userId) {
        return axios.get(USER_API_BASE_URL + '/' + userId);
    }

    updateUser(user, userId) {
        return axios.put(USER_API_BASE_URL + '/' + userId, user);
    }

    deleteUser(userId) {
        return axios.delete(USER_API_BASE_URL + '/' + userId);
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
}

export default new UserService()