<template>
  <div>
    <h2>Question 2: Choose a stroll time</h2>
    <ul class="options-container">
      <li v-for="option in step2Options" :key="option" class="option-item">
        <button class="option-button" @click="selectOption(option)">{{ option }}</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { useFormStore } from '../stores/form';
import { defineProps, defineEmits } from 'vue';
import { useRouter } from 'vue-router';

const store = useFormStore();
const router = useRouter();
const step2Options = store.step2Options;

const props = defineProps(["options"]);
const emit = defineEmits(["submit"]);

function selectOption(option) {
  store.setStep2(option); // Pinia 스토어에 선택 저장
  emit("submit", option); // 부모 컴포넌트로 선택된 값 전송
  router.push("/form/result"); // 결과 페이지로 이동
}
</script>

<style scoped>
/* 옵션 컨테이너 */
.options-container {
  display: flex;
  gap: 20px;
  justify-content: center;
  list-style: none;
  padding: 0;
  margin: 20px auto;
  max-width: 800px;
}

.option-item {
  flex: 1 1 calc(50% - 20px);
  display: flex;
  justify-content: center;
}

.option-button {
  min-width: 200px;
  width: 100%;
  height: 100px;
  font-size: 25px;
  font-weight: bold;
  border: 2px solid #ffb703;
  border-radius: 12px;
  background-color: #f9f9f9;
  color: #333;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  justify-content: center;
  align-items: center;
}

.option-button:hover {
  background-color: #ffb703;
  color: #fff;
  transform: scale(1.05);
}
</style>
