<template>
  <div class="login-view">
    <div class="container" :class="{ 'right-panel-active': !isLogin }">
      <!-- 登录表单 -->
      <div class="container__form container--signin">
        <el-form ref="loginForm" :model="formModel" :rules="formRules" class="form" @submit.native.prevent="submitForm">
          <h2 class="form__title">登&nbsp;录</h2>
          <el-form-item prop="account">
            <el-input v-model="formModel.account" placeholder="Account" class="input"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="formModel.password" type="password" placeholder="Password" class="input"></el-input>
          </el-form-item>
          <el-form-item  prop="authority">
            <el-select v-model="formModel.authority" placeholder="Authority" class="input">
              <el-option label="ADMIN" value="1"></el-option>
              <el-option label="USER" value="2"></el-option>
            </el-select>
          </el-form-item>
          <button type="submit" class="btn">Sign In</button>
        </el-form>
      </div>
      <!-- 注册表单 -->
      <div class="container__form container--signup">
        <el-form ref="registerForm" :model="formModel" :rules="formRules" class="form" @submit.native.prevent="submitForm">
          <h2 class="form__title">注&nbsp;册</h2>
          <el-form-item prop="account">
            <el-input v-model="formModel.account" placeholder="Account" class="input"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="formModel.password" type="password" placeholder="Password" class="input"></el-input>
          </el-form-item>
          <el-form-item prop="name">
            <el-input v-model="formModel.name" placeholder="Name" class="input"></el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input v-model="formModel.phone" placeholder="Phone" class="input"></el-input>
          </el-form-item>
          <button type="submit" class="btn">Sign Up</button>
        </el-form>
      </div>
      <!-- 覆盖层 -->
      <div class="container__overlay">
        <div class="overlay">
          <div class="overlay__panel overlay--left">
            <button class="btn" @click="switchForm(true)">Sign In</button>
          </div>
          <div class="overlay__panel overlay--right">
            <button class="btn" @click="switchForm(false)">Sign Up</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { login } from '@/apis/login.js';
import { addUser } from '@/apis/user.js';
import { saveUserLoginToken, saveUserInfo } from '@/utils/storage.js';
import { ElMessage } from 'element-plus';

export default {
  name: 'LoginView',
  data() {
    return {
      isLogin: true,
      formModel: {
        account: '',
        password: '',
        authority: '',
        name: '',
        phone: ''
      },
      formRules: {
        account: [
          { required: true, message: '请输入账号', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    async submitForm() {
      try {
        if (this.isLogin) {
          await this.login();
        } else {
          await this.register();
        }
      } catch (error) {
        console.error('Form submission error:', error);
        ElMessage.error('操作失败，请稍后再试！');
      }
    },
    async login() {
      try {
        const result = await login(this.formModel);
        if (result.data && result.data.token) {
          await this.saveInfo(result.data);
          ElMessage.success('登录成功！');
          this.$router.push('/home');
        } else {
          ElMessage.error('账号或密码错误！');
        }
      } catch (error) {
        console.error('Login error:', error);
        ElMessage.error('登录失败，请检查账号、密码和身份！');
      }
    },
    async register() {
      try {
        const valid = await this.$refs.registerForm.validate();
        if (!valid) {
          return;
        }

        await addUser({
          account: this.formModel.account,
          name: this.formModel.name,
          password: this.formModel.password,
          phone: this.formModel.phone
        });
        ElMessage.success('注册成功！');
        this.switchForm(true);
      } catch (error) {
        console.error('Register error:', error);
        if (error.response && error.response.data && error.response.data.message) {
          ElMessage.error(error.response.data.message);
        } else if (error.message === '账号已存在') {
          ElMessage.error('账号已存在，请选择其他账号！');
        } else {
          ElMessage.error('注册失败，请检查输入信息！');
        }
      }
    },
    async saveInfo(data) {
      try {
        if (data && data.token) {
          saveUserLoginToken(data.token);
          saveUserInfo(data);
        } else {
          console.error('不正确的用户信息：', data);
        }
      } catch (error) {
        console.error('错误的用户信息：', error);
      }
    },
    switchForm(isLogin) {
      this.isLogin = isLogin;
      this.resetForm();
    },
    resetForm() {
      this.formModel = {
        account: '',
        password: '',
        authority: '',
        name: '',
        phone: ''
      };
    }
  }
};
</script>

<style scoped>
.login-view {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  background: #ffffff;
  background-size: cover;
}

.form__title {
  font-weight: 300;
  margin: 0 0 1.25rem;
}
.link {
  color: #6c757d;
  font-size: 0.9rem;
  margin: 1.5rem 0;
  text-decoration: none;
}
.container {
  background-color: #ffffff;
  border-radius: 20px;
  box-shadow: 0 0.9rem 1.7rem rgba(0, 0, 0, 0.25),
  0 0.7rem 0.7rem rgba(0, 0, 0, 0.22);
  height: 600px;
  max-width: 800px;
  overflow: hidden;
  position: relative;
  width: 100%;
}
.container__form {
  height: 100%;
  position: absolute;
  top: 0;
  transition: all 0.6s ease-in-out;
}
.container--signin {
  left: 0;
  width: 50%;
  z-index: 2;
}
.container.right-panel-active .container--signin {
  transform: translateX(100%);
}
.container--signup {
  left: 0;
  opacity: 0;
  width: 50%;
  z-index: 1;
}
.container.right-panel-active .container--signup {
  animation: show 0.6s;
  opacity: 1;
  transform: translateX(100%);
  z-index: 5;
}
.container__overlay {
  height: 100%;
  left: 50%;
  overflow: hidden;
  position: absolute;
  top: 0;
  transition: transform 0.6s ease-in-out;
  width: 50%;
  z-index: 100;
}
.container.right-panel-active .container__overlay {
  transform: translateX(-100%);
}
.overlay {
  background: #72A6C9;
  background-size: cover;
  height: 100%;
  left: -100%;
  position: relative;
  transform: translateX(0);
  transition: transform 0.6s ease-in-out;
  width: 200%;
}
.container.right-panel-active .overlay {
  transform: translateX(50%);
}
.overlay__panel {
  align-items: center;
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: center;
  position: absolute;
  text-align: center;
  top: 0;
  transform: translateX(0);
  transition: transform 0.6s ease-in-out;
  width: 50%;
}
.overlay--left {
  transform: translateX(-20%);
}
.container.right-panel-active .overlay--left {
  transform: translateX(0);
}
.overlay--right {
  right: 0;
  transform: translateX(0);
}
.container.right-panel-active .overlay--right {
  transform: translateX(20%);
}
.btn {
  background-color: #72A6C9;
  background-image: linear-gradient(90deg, #72A6C9 0%, #E6F7FA 74%);
  border-radius: 20px;
  border: 1px solid #ffffff;
  color: #ffffff;
  cursor: pointer;
  font-size: 0.8rem;
  font-weight: bold;
  letter-spacing: 0.1rem;
  padding: 0.9rem 4rem;
  text-transform: uppercase;
  transition: transform 80ms ease-in;
}
.btn:active {
  transform: scale(0.95);
}
.btn:focus {
  outline: none;
}
.form {
  background-color: #ffffff;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
  padding: 0 3rem;
  height: 100%;
  text-align: center;
}
.input {
  background-color: #F5F5F5;
  border: none;
  padding: 0.9rem 1.2rem;
  margin: 0.5rem 0;
  width: 100%;
}
@keyframes show {
  0%,
  49.99% {
    opacity: 0;
    z-index: 1;
  }
  50%,
  100% {
    opacity: 1;
    z-index: 5;
  }
}
</style>
