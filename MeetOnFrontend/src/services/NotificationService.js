import axios from 'axios'
import authHeader from "./AuthHeader";

const NOTIFICATION_API_BASE_URL = "https://meetonapi.herokuapp.com/api/v1/notifications";
class NotificatoinService {
    getNotificationsByUser(userId) {
        console.log(NOTIFICATION_API_BASE_URL + '/byUser' + userId);
        return axios.get(NOTIFICATION_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }
    getNotificationsByUserAndStatus(userId) {
        console.log(NOTIFICATION_API_BASE_URL + '/byUser' + userId);
        return axios.get(NOTIFICATION_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }
    setNotificationStatusViewed(notificationId) {
        return axios.put(NOTIFICATION_API_BASE_URL + `/changeStatus/${notificationId}?status=VIEWED`, null, { headers: authHeader() });
    }
}

export default new NotificatoinService()