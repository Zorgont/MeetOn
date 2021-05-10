import axios from 'axios'

const TAGS_API_BASE_URL = "https://meetonapi.herokuapp.com/api/v1/tags";
class TagService {
    getTags() {
        return axios.get(TAGS_API_BASE_URL);
    }
}

export default new TagService();