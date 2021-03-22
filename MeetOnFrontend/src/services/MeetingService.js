import axios from 'axios'
import authHeader from "./AuthHeader";

const MEETING_API_BASE_URL = "http://localhost:8080/api/v1/meetings";
class MeetingService {
    getMeetings() {
        return axios.get(MEETING_API_BASE_URL);
    }

    createMeeting(meeting) {
        return axios.post(MEETING_API_BASE_URL, meeting, { headers: authHeader() });
    }

    getMeetingById(meetingId) {
        console.log(MEETING_API_BASE_URL+ '/' + meetingId);
        return axios.get(MEETING_API_BASE_URL+ '/' + meetingId, { headers: authHeader() });
    }

    updateMeeting(meeting, meetingId) {
        return axios.put(MEETING_API_BASE_URL+ '/' + meetingId, meeting, { headers: authHeader() });
    }

    deleteMeeting(meetingId) {
        return axios.delete(MEETING_API_BASE_URL + '/' + meetingId, { headers: authHeader() });
    }
    getMeetingsByTags(tags) {
        let params="?tags="
        for(let it in tags){
            params+=tags[it]+","
        }
        params=params.substring(0,params.length - 1)
        console.log(MEETING_API_BASE_URL + params);
        return axios.get(MEETING_API_BASE_URL + params);
    }

}

export default new MeetingService()