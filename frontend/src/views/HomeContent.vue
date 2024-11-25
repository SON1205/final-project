<template>
    <div class="main-content">
        <div class="content">
            <h1>Welcome to <br>Urban Recommendation Stroll</h1>
            <p>Please log in to continue.</p>
            <div class="buttons">
                <router-link to="login" class="button">Log In</router-link>
                <router-link to="signup" class="button">Sign Up</router-link>
            </div>
        </div>
    </div>
</template>

<script setup>
</script>

<style scoped>
h1 {
    font-size: 3rem;
    margin-bottom: 20px;
    text-shadow:
        -1px -1px 0 #000,
        /* 왼쪽 위 */
        1px -1px 0 #000,
        /* 오른쪽 위 */
        -1px 1px 0 #000,
        /* 왼쪽 아래 */
        1px 1px 0 #000;
    /* 오른쪽 아래 */
}

p {
    font-size: 1.5rem;
    text-shadow:
        -1px -1px 0 #000,
        1px -1px 0 #000,
        -1px 1px 0 #000,
        1px 1px 0 #000;
}

.main-content {
    position: relative;
    width: 100vw;
    height: 100vh;
}

.content {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    text-align: center;
    z-index: 2;
    color: white;
}

h1 {
    font-size: 3rem;
    margin-bottom: 20px;
}

p {
    font-size: 1.5rem;
}

.buttons {
    margin-top: 20px;
}

.button {
    display: inline-block;
    margin: 0 10px;
    padding: 10px 20px;
    color: white;
    background-color: #ffb703;
    border-radius: 5px;
    text-decoration: none;
    font-size: 1rem;
    transition: background-color 0.3s ease;
}

.button:hover {
    background-color: #6a994e;
}

/* 모달 스타일 */
.modal {
    position: relative;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 10;
}
</style>