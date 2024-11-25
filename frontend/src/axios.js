import axios from "axios";

// Axios 기본 설정
axios.defaults.baseURL = "http://localhost:8080/ssafy-urs"; // 백엔드 주소
axios.defaults.withCredentials = true; // 쿠키를 포함하여 요청

export default axios;
