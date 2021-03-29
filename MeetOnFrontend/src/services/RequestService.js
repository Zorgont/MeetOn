import axios from 'axios'
import authHeader from "./AuthHeader";

const REQUEST_API_BASE_URL = "http://localhost:8080/api/v1/requests";

class RequestService{
    createRequest(request) {
        return axios.post(REQUEST_API_BASE_URL, request, { headers: authHeader() });
    }

    getRequestsByUserId(userId) {
        return axios.get(REQUEST_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }

    getRequestsByMeetingId(meetingId) {
        return axios.get(REQUEST_API_BASE_URL + '/byMeeting/' + meetingId, { headers: authHeader() });
    }

    updateRequestStatus(requestId, status) {
        return axios.put(REQUEST_API_BASE_URL + `/changeStatus/${requestId}?status=${status}`, null, { headers: authHeader() });
    }

    checkRequestExistence(meetingId, userId) {
        return axios.get(REQUEST_API_BASE_URL + `/check?meetingId=${meetingId}&userId=${userId}`, { headers: authHeader() });
    }

    removeRequest(id) {
        return axios.delete(REQUEST_API_BASE_URL + '/'+ id, { headers: authHeader() });
    }
}
export default new RequestService()