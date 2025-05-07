<script setup>
import Header from '@/components/Header.vue'
import Footer from '@/components/Footer.vue'
</script>

<template>
  <el-container>
    <el-header>
      <Header />
    </el-header>
    <el-main>
      <router-view />
    </el-main>
    <el-footer>
      <Footer />
    </el-footer>
  </el-container>
</template>

<style scoped>
.el-container {
  display: flex;
  flex-direction: column;
  min-height: 124vh;
}
.el-header {
  background-color: #72A6C9;
  height: 12vh;
  display: flex;
}
.el-main {
  background-color: #F5F5F5;
}
.el-footer {
  background-color: #72A6C9;
  height: 12vh;
}
</style>