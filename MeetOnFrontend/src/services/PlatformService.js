import axios from 'axios'

const PLATFORM_API_BASE_URL = "https://meetonapi.herokuapp.com/api/v1/platforms";
class PlatformService {
    getAllPlatforms() {
        console.log(PLATFORM_API_BASE_URL);
        return axios.get(PLATFORM_API_BASE_URL);
    }
    
    getPlatformByName(name) {
        console.log(PLATFORM_API_BASE_URL + "?name=" + name);
        return axios.get(PLATFORM_API_BASE_URL + "/by?name=" + name);
    }
}

export default new PlatformService()