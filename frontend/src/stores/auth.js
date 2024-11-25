import { ref } from "vue";
import axios from "@/axios";
import { defineStore } from "pinia";

export const useAuthStore = defineStore("auth", () => {
  const isLoggedIn = ref(false);
  const username = ref("");
  const userId = ref("");

  // 로그인 처리
  const login = async (credentials) => {
    try {
      const response = await axios.post("/api/login", credentials);
      userId.value = response.data.userId; // 서버로부터 userId를 받아서 저장
      username.value = response.data.userName; // 서버로부터 username도 저장
      isLoggedIn.value = true;
    } catch (error) {
      console.error("로그인 실패:", error.response?.data?.message || error.message);
      throw new Error(error.response?.data?.message || "로그인에 실패했습니다. 아이디와 비밀번호를 확인해주세요.");
    }
  };

  // 로그아웃 처리
  const logout = async () => {
    try {
      await axios.post("/api/logout");
      isLoggedIn.value = false;
      username.value = "";
      userId.value = "";
    } catch (error) {
      console.error("로그아웃 실패:", error.response?.data?.message || error.message);
      throw new Error("로그아웃에 실패했습니다.");
    }
  };

  // 세션 확인
  const checkSession = async () => {
    try {
      const response = await axios.get("/secure/check");
      userId.value = response.data.userId; // 서버로부터 userId를 받아서 저장
      username.value = response.data.userName; // 서버로부터 username도 저장
      isLoggedIn.value = !!response.data.userId; // userId 존재 여부에 따라 로그인 상태 설정
    } catch (error) {
      console.error("세션 확인 실패:", error.message);
      isLoggedIn.value = false;
      username.value = "";
      userId.value = "";
    }
  };

  return {
    isLoggedIn,
    username,
    userId, // userId를 반환 추가
    login,
    logout,
    checkSession,
  };
});
