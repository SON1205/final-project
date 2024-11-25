<template>
  <div class="main-content">
    <div class="content">
      <h2 id="route-top-10">Route Top 10</h2>
      <Carousel :items-to-show="5" :wrap-around="true" :autoplay="true" :autoplay-timeout="3000" :transition="3000"
        :pause-autoplay-on-hover="true" :breakpoints="{
          1024: { itemsToShow: 3 },
          768: { itemsToShow: 2 },
          480: { itemsToShow: 1 },
        }">
        <Slide v-for="(item, index) in topRoutes" :key="index">
          <div class="slide-content">
            <img :src="item.imageUrl" alt="Slide Image" />
            <h3>{{ item.district }}</h3>
            <div class="rate">
              <span>Rate : </span>
              <span v-for="n in item.rate" :key="n">💚</span>
            </div>
          </div>
        </Slide>
      </Carousel>

      <div id="select-route-button">
        <button @click="navigateToOption">SELECT ROUTE</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import "vue3-carousel/dist/carousel.css";
import { Carousel, Slide } from "vue3-carousel";
import { ref, onMounted } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

const router = useRouter();

const topRoutes = ref([]);

const fetchTopReviews = async () => {
  try {
    const response = await axios.get("/api/routes/top");
    topRoutes.value = response.data.map((route) => ({
      district: route.districtName,
      imageUrl: route.imageUrl,
      rate: Math.min(Math.max(Math.round(route.averageRating), 1), 5), // 1~5로 제한
    }));
  } catch (error) {
    console.error("Failed to fetch top reviews:", error);
    alert("Failed to load top routes. Please try again later.");
  }
};


const navigateToOption = () => {
  router.push("/form");
};

onMounted(() => {
  fetchTopReviews();
});
</script>

<style scoped>
.rate {
  margin-top: 10px;
  margin-left: 3px;
  font-size: 1.1rem;
  display: flex;
  justify-content: center;
  gap: 2px;
}

#select-route-button button {
  margin-top: 2rem;
  display: inline-block;
  padding: 12px 24px;
  font-size: 16px;
  font-weight: bold;
  color: white;
  background-color: #ffb703;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

#select-route-button button:hover {
  background-color: #6a994e;
  transform: translateY(-3px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}

#select-route-button button:active {
  transform: translateY(1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

#route-top-10 {
  text-align: left;
  margin-left: 20px;
  margin-bottom: 20px;
}

.main-content {
  position: relative;
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.content {
  width: 80%;
  max-width: 1200px;
  height: 70%;
  max-height: 500px;
  text-align: center;
  z-index: 2;
  color: white;
}

.slide-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  width: 300px;
  height: 360px;
  background-color: rgb(22, 22, 22);
  border: 1px solid #ccc;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s ease, box-shadow 0.3s ease, width 0.3s ease, height 0.3s ease;
}

.slide-content:hover {
  width: 330px;
  height: 340px;
  transform: scale(1) translateY(-10px);
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.3);
}


.slide-content img {
  width: 100%;
  height: 210px;
  object-fit: scale-down;
}

.slide-content h3 {
  margin-top: 10px;
  font-size: 1.2rem;
  color: white;
}
</style>
