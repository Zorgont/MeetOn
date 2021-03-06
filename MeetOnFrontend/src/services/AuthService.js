import axios from "axios";

const API_URL = "https://meetonapi.herokuapp.com/api/v1/auth/";

class AuthService {
    login(username, password) {
        return axios
            .post(API_URL + "signin", {
                username,
                password
            })
            .then(response => {
                console.log(response);
                if (response.data.token) {
                    console.log(JSON.stringify(response.data));
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    loginViaGoogle(user) {
        return axios
            .post(API_URL + "google", user)
            .then(response => {
                console.log(response);
                if (response.data.token) {
                    console.log(JSON.stringify(response.data));
                    localStorage.setItem("user", JSON.stringify(response.data));
                }

                return response.data;
            });
    }

    existsUserByUsername(username) {
        return axios.get(API_URL + 'existsByName/' + username);
    }

    existsUserByEmail(email) {
        return axios.get(API_URL + 'existsByEmail/' + email);
    }

    logout() {
        localStorage.removeItem("user");
    }

    register(username, email, password) {
        return axios.post(API_URL + "signup", {
            username,
            email,
            password
        });
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));

    }
    confirmUser(token) {
        return axios.get(API_URL + "confirmAccount?token=" + token)
    }
}

export default new AuthService();