import axios from 'axios'

const TAGS_API_BASE_URL = "http://app:8080/api/v1/tags";
class TagService {
    getTags() {
        return axios.get(TAGS_API_BASE_URL);
    }
}

export default new TagService();