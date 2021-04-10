import axios from 'axios'
import authHeader from "./AuthHeader";

const NOTIFICATION_API_BASE_URL = "http://localhost:8080/api/v1/notifications";
class NotificatoinService {
    getNotificationsByUser(userId) {
        console.log(NOTIFICATION_API_BASE_URL + '/byUser' + userId);
        return axios.get(NOTIFICATION_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }
}

export default new NotificatoinService()