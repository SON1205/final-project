<template>
  <header class="header">
    <div class="logo-container">
      <img src="@/assets/logo/team_logo.png" alt="Logo" class="logo-image" />
      <span class="app-name">URS</span>
    </div>
    <button class="notification-button" @click="handleNotification">
      <span class="icon">&#x1F514;</span>
    </button>
  </header>
</template>

<script>
export default {
  methods: {
    handleNotification() {
      alert('알림 버튼이 클릭되었습니다!');
    },
  },
};
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  padding: 10px 20px;
  background-color: #343434;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  border-radius: 20px;
  border-radius: 20px;
}

.logo-container {
  display: flex;
  align-items: center;
}

.logo-image {
  width: 60px;
  height: 60px;
  margin-right: 10px;
}

.app-name {
  font-size: 40px;
  font-weight: bold;
  color: rgb(255, 255, 255);
}

.notification-button {
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 20px;
  position: relative;
}

.icon {
  font-size: 25px;
}
</style>