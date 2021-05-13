import axios from 'axios'
import authHeader from "./AuthHeader";

const IMAGE_API_BASE_URL = "http://localhost:8080/api/v1/users";
class ImageService {

    uploadAvatar(userId, imageFile) {
        return axios.post(IMAGE_API_BASE_URL + '/' + userId + '/avatar', imageFile  , { headers: authHeader() });
    }
}

export default new ImageService()