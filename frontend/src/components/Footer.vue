<script setup>
import { ref } from 'vue';

// 获取当前年份
const currentYear = ref(new Date().getFullYear());

function scrollToTop() {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });
}
</script>

<template>
  <div class="footer">
    <div class="footer-top">
      <span>Copyright &copy; {{ currentYear }} All rights reserved.</span>
    </div>
    <div class="footer-bottom">
      <div class="logos">
        <img src="../assets/logo/img_logo_bushi.png" alt="bushi" class="footer-logo" style="width: 37px; height: 20px; margin: 0 10px;">
        <img src="../assets/logo/img_logo_gsc.png" alt="gsc" class="footer-logo" style="width: 66px; height: 18px; margin: 0 10px;">
        <img src="../assets/logo/img_logo_tokyomx.png" alt="tokyomx" class="footer-logo" style="width: 64px; height: 9px; margin: 0 10px;">
      </div>
      <button class="scroll-to-top" @click="scrollToTop">⮙</button>
    </div>
  </div>
</template>

<style scoped>
.footer {
  text-align: center;
  color: #FFFFFF;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 16px;
  font-weight: bold;
  font-style: italic;
  background-color: #72A6C9;
}

.footer-top {
  margin-bottom: 10px;
}

.footer-bottom {
  width: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.logos {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
}

.footer-logo {
  margin: 0 10px;
}

.scroll-to-top {
  position: absolute;
  right: 10px;
  width: 30px;
  height: 30px;
  background-color: #FFFFFF;
  color: #72A6C9;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}

.scroll-to-top:hover {
  background-color: #d1d1d1;
}
</style>