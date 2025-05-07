<template>
  <div class="info-view">
    <el-card class="user-info-card" shadow="hover">
      <h2>用户信息</h2>
      <el-form :model="user" label-width="100px">
        <el-form-item label="账号">
          <el-input v-model="user.account" disabled></el-input>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input v-model="user.name" disabled></el-input>
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="user.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdateUserInfo">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="password-update-card" shadow="hover">
      <h2>修改密码</h2>
      <el-form label-width="100px">
        <el-form-item label="新密码">
          <el-input v-model="newPassword" type="password" placeholder="请输入新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="confirmPassword" type="password" placeholder="请确认新密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleUpdatePassword">提交</el-button>
          <el-button @click="resetPasswordFields">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getUserInfo } from '@/utils/storage';
import { getUserByName, updatePassword, updateUser } from '@/apis/user.js';

export default {
  data() {
    return {
      user: {},
      newPassword: '',
      confirmPassword: ''
    };
  },
  created() {
    const userInfo = getUserInfo();
    if (userInfo && userInfo.name) {
      getUserByName(userInfo.name)
          .then(response => {
            this.user = response.data;
          })
          .catch(error => {
            this.$message.error(`获取用户信息失败: ${error.response?.data?.message || error.message}`);
          });
    } else {
      this.$message.error('用户信息获取失败');
    }
  },
  methods: {
    async handleUpdateUserInfo() {
      try {
        await updateUser(this.user.id, {
          account: this.user.account,
          name: this.user.name,
          phone: this.user.phone,
          authority: this.user.authority,
          password: this.user.password
        });
        this.$message.success('用户信息修改成功');
      } catch (error) {
        this.$message.error('用户信息修改失败');
      }
    },
    async handleUpdatePassword() {
      if (this.newPassword !== this.confirmPassword) {
        this.$message.error('两次输入的密码不一致');
        return;
      }
      if (this.newPassword === this.user.password) {
        this.$message.warning('新密码不能与旧密码相同');
        return;
      }
      try {
        await updatePassword(this.user.id, { password: this.newPassword });
        this.$message.success('密码修改成功');
        this.resetPasswordFields();
      } catch (error) {
        this.$message.error('密码修改失败');
      }
    },
    resetPasswordFields() {
      this.newPassword = '';
      this.confirmPassword = '';
    }
  }
};
</script>

<style scoped>
.info-view {
  padding: 20px;
  max-width: 900px;
  margin: auto;
  display: flex;
  flex-direction: column;
  gap: 20px;
  border-radius: 10px;
  border: 1px solid #e0e0e0;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #f9f9f9;
}

.user-info-card,
.password-update-card {
  padding: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-bottom: 20px;
}
</style>
