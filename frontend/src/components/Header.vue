<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getUserInfo, removeUserInfo } from '@/utils/storage.js'

const router = useRouter()
const userInfo = ref(getUserInfo())
const handleLogout = () => {
  removeUserInfo()
  ElMessage.success('退出成功')
  router.push('/login')
}
</script>

<template>
  <div class="header">
    <div class="logo">
      <img src="../assets/logo/logo.svg" alt="logo" width="80dx" height="auto" />
      <span style="align-content: center; color: #FFFFFF; font-size: 22px; font-weight: bold; font-style: italic">&nbsp;&nbsp;LibroSphere&nbsp;&nbsp;</span>
    </div>
    <div class="navmenu">
      <el-menu
          default-active="1"
          class="navmenu"
          mode="horizontal"
          background-color="#72A6C9"
          text-color="#FFFFFF"
          active-text-color="#E6E6E6"
          style="height: 100%; width: 700px; border: none;"
      >
        <el-menu-item index="1" @click="$router.push('/home')" style="font-size: 18px; font-weight: bold; font-style: italic">&nbsp;首页&nbsp;</el-menu-item>
        <el-menu-item index="2" @click="$router.push('/user')" style="font-size: 18px; font-weight: bold; font-style: italic" v-if="userInfo.role === '1'">&nbsp;用户管理&nbsp;</el-menu-item>
        <el-menu-item index="3" @click="$router.push('/book')" style="font-size: 18px; font-weight: bold; font-style: italic" v-if="userInfo.role === '1'">&nbsp;图书管理&nbsp;</el-menu-item>
        <el-menu-item index="4" @click="$router.push('/comment')" style="font-size: 18px; font-weight: bold; font-style: italic" v-if="userInfo.role === '1'">&nbsp;评论管理&nbsp;</el-menu-item>
        <el-menu-item index="5" @click="$router.push('/info')" style="font-size: 18px; font-weight: bold; font-style: italic">&nbsp;个人信息&nbsp;</el-menu-item>
      </el-menu>
    </div>
    <!-- 用户名显示和退出按钮 -->
    <div class="user-info">
      <el-dropdown>
        <span class="el-dropdown-link username" style="font-size: 16px; color: #FFFFFF">
          欢迎您，{{ userInfo.name }} <i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<style scoped>
.header {
  background-color: #72A6C9;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
}
.logo {
  display: flex;
  align-items: center;
  flex: 6;
  height: 100%;
}
.navmenu {
  display: flex;
  align-items: center;
  flex: 11;
  height: 100%;
  cursor: pointer;
}
.user-info {
  display: flex;
  align-items: center;
  flex: 3;
  height: 100%;
}
.username {
  color: #FFFFFF;
  font-weight: bold;
  font-style: italic;
  cursor: pointer;
}
</style>
