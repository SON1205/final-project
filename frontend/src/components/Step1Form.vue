<template>
  <div>
    <h2>Question 1: Choose the atmosphere</h2>
    <ul class="options-container">
      <li v-for="option in step1Options" :key="option" class="option-item">
        <button class="option-button" @click="emitNext(option)">
          {{ option }}
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { useFormStore } from '../stores/form';
import { defineProps, defineEmits } from "vue";

const store = useFormStore();
const step1Options = store.step1Options;

const props = defineProps(["options"]);
const emit = defineEmits(["next"]);

function emitNext(option) {
  store.setStep1(option);
  emit("next", option); // 부모에게 선택된 옵션 전달
}
</script>

<style scoped>
/* 옵션 컨테이너 */
.options-container {
  display: flex;
  flex-wrap: wrap;
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
  min-width: 300px;
  width: 100%;
  height: 100px;
  font-size: 20px;
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
