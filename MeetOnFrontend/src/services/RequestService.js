import axios from 'axios'
import authHeader from "./AuthHeader";

const MEETING_API_BASE_URL = "http://localhost:8080/api/v1/requests";

class RequestService{
    createRequest(request) {
        return axios.post(MEETING_API_BASE_URL, request, { headers: authHeader() });
    }

    getRequestsByUserId(userId) {
        console.log(MEETING_API_BASE_URL + '/' + userId);
        return axios.get(MEETING_API_BASE_URL + '/byUser/' + userId, { headers: authHeader() });
    }

    getRequestsByMeetingId(meetingId) {
        console.log(MEETING_API_BASE_URL);
        return axios.get(MEETING_API_BASE_URL + '/byMeeting/' + meetingId, { headers: authHeader() });
    }

    updateRequestStatus(status, requestId) {
        return axios.put(MEETING_API_BASE_URL + '/changeStatus/' + requestId, status, { headers: authHeader() });
    }
    checkRequestExistence(meetingId,userId) {
        console.log("Request")
        console.log(MEETING_API_BASE_URL + `/check?meetingId=${meetingId}&userId=${userId}`)
        return axios.get(MEETING_API_BASE_URL + `/check?meetingId=${meetingId}&userId=${userId}`, { headers: authHeader() })
    }
}
export default new RequestService()