import { defineStore } from 'pinia';

export const useFormStore = defineStore('form', {
  state: () => ({
    // 선택 가능한 지역 목록
    districtOptions: [
      '강남구', '강동구', '강북구', '강서구', '관악구', '광진구', '구로구', '금천구', '노원구', '도봉구', '동대문구', '동작구', '마포구', '서대문구',
      '서초구', '성동구', '성북구', '송파구', '양천구', '영등포구', '용산구', '은평구', '종로구', '중구', '중랑구',
    ],
    // 1단계 옵션
    step1Options: ['DEMURE', 'CALMLY', 'LIVELY', 'ALONE'],
    // 2단계 옵션
    step2Options: [15, 25, 45],
    // 지역별 격자 정보
    grid: {
      '강남구': [61, 126],
      '강동구': [62, 126],
      '강북구': [61, 128],
      '강서구': [58, 126],
      '관악구': [59, 125],
      '광진구': [62, 126],
      '구로구': [58, 125],
      '금천구': [59, 124],
      '노원구': [61, 129],
      '도봉구': [61, 129],
      '동대문구': [61, 127],
      '동작구': [59, 125],
      '마포구': [59, 127],
      '서대문구': [59, 127],
      '서초구': [61, 125],
      '성동구': [61, 127],
      '성북구': [61, 127],
      '송파구': [62, 126],
      '양천구': [58, 126],
      '영등포구': [58, 126],
      '용산구': [60, 126],
      '은평구': [59, 127],
      '종로구': [60, 127],
      '중구': [60, 127],
      '중랑구': [62, 128],
    },
    // 선택된 지역 및 옵션
    selectedDistrict: null,
    selectedStep1: null,
    selectedStep2: null,
  }),
  getters: {
    // 선택된 지역의 grid 좌표 반환
    districtGrid: (state) => {
      return state.selectedDistrict ? state.grid[state.selectedDistrict] : null;
    },
  },
  actions: {
    // 지역 설정
    setDistrict(option) {
      this.selectedDistrict = option;
    },
    // 1단계 선택 옵션 설정
    setStep1(option) {
      this.selectedStep1 = option;
    },
    // 2단계 선택 옵션 설정
    setStep2(option) {
      this.selectedStep2 = option;
    },
  },
});
