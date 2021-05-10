import axios from 'axios'
import authHeader from "./AuthHeader";

const IMAGE_API_BASE_URL = "https://meetonapi.herokuapp.com/api/v1/users";
class ImageService {

    uploadAvatar(userId, imageFile) {
        return axios.post(IMAGE_API_BASE_URL + '/' + userId + '/avatar', imageFile  , { headers: authHeader() });
    }
}

export default new ImageService()