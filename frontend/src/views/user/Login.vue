<template>
  <div class="login-container">
    <div class="login-bg-decoration">
      <div class="bg-circle bg-circle-1"></div>
      <div class="bg-circle bg-circle-2"></div>
      <div class="bg-circle bg-circle-3"></div>
    </div>
    
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <div class="logo-wrapper">
            <el-icon :size="50" class="logo-icon"><Reading /></el-icon>
          </div>
          <h2 class="card-title">校园二手书交易平台</h2>
          <p class="card-subtitle">让知识流转起来</p>
        </div>
      </template>
      
      <div class="login-form">
        <div class="input-wrapper">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            class="custom-input"
          >
            <template #prefix>
              <el-icon class="input-icon"><User /></el-icon>
            </template>
          </el-input>
        </div>
        
        <div class="input-wrapper">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
            class="custom-input"
            @keyup.enter="handleLogin"
          >
            <template #prefix>
              <el-icon class="input-icon"><Lock /></el-icon>
            </template>
          </el-input>
        </div>
        
        <div class="login-button-wrapper">
          <el-button
            type="primary"
            :loading="loading"
            class="login-button"
            @click="handleLogin"
          >
            <span v-if="!loading">登 录</span>
            <span v-else>登录中...</span>
          </el-button>
        </div>
        
        <div class="form-footer">
          <router-link to="/register" class="register-link">
            <el-icon><Plus /></el-icon>
            还没有账号？立即注册
          </router-link>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Reading, Plus, User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import type { LoginForm } from '@/types'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)

const loginForm = reactive<LoginForm>({
  username: '',
  password: ''
})

const handleLogin = async () => {
  if (!loginForm.username || !loginForm.password) {
    ElMessage.error('请填写用户名和密码')
    return
  }
  
  if (loginForm.username.length < 3) {
    ElMessage.error('用户名至少3个字符')
    return
  }
  
  if (loginForm.password.length < 6) {
    ElMessage.error('密码至少6个字符')
    return
  }
  
  loading.value = true
  
  try {
    await userStore.loginAction(loginForm)
    ElMessage.success('登录成功！')
    
    const redirect = (route.query.redirect as string) || '/'
    router.push(redirect)
  } catch (error) {
    console.error('登录失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
}

.login-bg-decoration {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  overflow: hidden;
}

.bg-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.3;
}

.bg-circle-1 {
  width: 400px;
  height: 400px;
  background: rgba(255, 255, 255, 0.2);
  top: -100px;
  left: -100px;
  animation: floatCircle1 15s ease-in-out infinite;
}

.bg-circle-2 {
  width: 300px;
  height: 300px;
  background: rgba(255, 255, 255, 0.15);
  bottom: -50px;
  right: -50px;
  animation: floatCircle2 12s ease-in-out infinite;
}

.bg-circle-3 {
  width: 200px;
  height: 200px;
  background: rgba(255, 255, 255, 0.1);
  top: 50%;
  left: 20%;
  animation: floatCircle3 18s ease-in-out infinite;
}

.login-card {
  width: 420px;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(20px);
  border-radius: 20px;
  border: none;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.25);
  position: relative;
  z-index: 10;
  animation: fadeInUp 0.6s ease-out;
}

.card-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  padding-bottom: 20px;
}

.logo-wrapper {
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 8px 30px rgba(102, 126, 234, 0.4);
  animation: pulse 2s ease-in-out infinite;
}

.logo-icon {
  color: white;
}

.card-title {
  margin: 0;
  font-size: 26px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.card-subtitle {
  margin: 0;
  color: #999;
  font-size: 14px;
}

.login-form {
  padding: 20px 30px 30px;
}

.input-wrapper {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.custom-input {
  width: 100%;
  max-width: 300px;
}

.custom-input :deep(.el-input__wrapper) {
  height: 52px;
  border-radius: 14px;
  border: 2px solid #e8e8e8;
  background: #ffffff;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.custom-input :deep(.el-input__wrapper:hover) {
  border-color: #d5d5d5;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 0 0 4px rgba(102, 126, 234, 0.15), 0 2px 12px rgba(102, 126, 234, 0.15);
}

.custom-input :deep(.el-input__prefix) {
  color: #999;
  transition: color 0.3s ease;
}

.custom-input :deep(.el-input__wrapper.is-focus .el-input__prefix) {
  color: #667eea;
}

.login-button-wrapper {
  display: flex;
  justify-content: center;
  margin: 10px 0 20px;
}

.login-button {
  width: 100%;
  max-width: 300px;
  height: 52px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 14px;
  font-size: 16px;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.login-button:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4090 100%);
  transform: translateY(-2px);
  box-shadow: 0 8px 25px rgba(102, 126, 234, 0.4);
}

.login-button:active {
  transform: translateY(0);
}

.form-footer {
  text-align: center;
  margin-top: 20px;
}

.register-link {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  color: #667eea;
  text-decoration: none;
  font-size: 14px;
  transition: all 0.3s ease;
}

.register-link:hover {
  color: #764ba2;
  transform: translateY(-1px);
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@keyframes pulse {
  0%, 100% {
    box-shadow: 0 8px 30px rgba(102, 126, 234, 0.4);
  }
  50% {
    box-shadow: 0 8px 40px rgba(102, 126, 234, 0.6);
  }
}

@keyframes floatCircle1 {
  0%, 100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(50px, 50px);
  }
}

@keyframes floatCircle2 {
  0%, 100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(-40px, -40px);
  }
}

@keyframes floatCircle3 {
  0%, 100% {
    transform: translate(0, 0);
  }
  50% {
    transform: translate(30px, -30px);
  }
}
</style>
