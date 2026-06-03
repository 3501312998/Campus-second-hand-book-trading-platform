<template>
  <div class="home-container">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="logo">
            <el-icon :size="36" class="logo-icon"><Reading /></el-icon>
            <span class="logo-text">校园二手书交易平台</span>
          </div>
          
          <div class="nav-menu">
            <el-menu mode="horizontal" :default-active="'1'" router class="custom-nav-menu">
              <el-menu-item index="/"><el-icon><House /></el-icon> 首页</el-menu-item>
              <el-menu-item index="/books"><el-icon><Notebook /></el-icon> 书籍列表</el-menu-item>
            </el-menu>
          </div>
          
          <div class="user-actions">
            <template v-if="userStore.token">
              <el-button type="primary" class="publish-btn" @click="$router.push('/publish')">
                <el-icon><Plus /></el-icon> 发布书籍
              </el-button>
              <el-dropdown @command="handleCommand" class="user-dropdown">
                <span class="user-info">
                  <el-avatar :size="36" class="user-avatar" :icon="UserFilled" />
                  <span class="username">{{ userStore.userInfo?.username || '用户' }}</span>
                  <el-icon class="arrow-icon"><CaretBottom /></el-icon>
                </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item command="/profile"><el-icon><User /></el-icon> 个人中心</el-dropdown-item>
                    <el-dropdown-item command="/my-books"><el-icon><Document /></el-icon> 我的发布</el-dropdown-item>
                    <el-dropdown-item command="/orders"><el-icon><ShoppingCart /></el-icon> 我的订单</el-dropdown-item>
                    <el-dropdown-item command="/cart"><el-icon><ShoppingBag /></el-icon> 购物车</el-dropdown-item>
                    <el-dropdown-item divided command="logout"><el-icon><ArrowRight /></el-icon> 退出登录</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </template>
            <template v-else>
              <el-button class="login-btn" @click="$router.push('/login')">登录</el-button>
              <el-button type="primary" class="register-btn" @click="$router.push('/register')">注册</el-button>
            </template>
          </div>
        </div>
      </el-header>
      
      <el-main class="main-content">
        <div class="hero-section">
          <div class="hero-content">
            <h1 class="hero-title">
              <el-icon :size="48" class="hero-icon"><Notebook /></el-icon>
              发现你的下一本好书
            </h1>
            <p class="hero-subtitle">海量二手书籍，低价购好书，让知识流转起来</p>
          </div>
        </div>
        
        <div class="search-section">
          <el-card shadow="hover" class="search-card">
            <div class="search-box">
              <el-input
                v-model="searchKeyword"
                placeholder="搜索书名、作者、ISBN..."
                size="large"
                clearable
                class="search-input"
                @keyup.enter="handleSearch"
              >
                <template #prepend>
                  <el-icon><Search /></el-icon>
                </template>
                <template #append>
                  <button type="button" class="search-button-native" @click="handleSearch">搜索</button>
                </template>
              </el-input>
            </div>
          </el-card>
        </div>
        
        <div class="hot-books">
          <div class="section-header">
            <h2 class="section-title">
              <el-icon class="section-icon"><HotWater /></el-icon>
              热门书籍
            </h2>
            <span class="section-badge">人气推荐</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="book in hotBooks" :key="book.id">
              <el-card shadow="hover" class="book-card card-hover" @click="goToDetail(book.id)">
                <div class="book-image-wrapper">
                  <div v-if="!book.images" class="book-cover-placeholder">
                    <el-icon :size="64" class="book-icon"><Notebook /></el-icon>
                  </div>
                  <el-image v-else :src="book.images.split(',')[0]" fit="cover" class="book-image" />
                </div>
                <div class="book-info">
                  <h3 class="book-title">{{ book.title }}</h3>
                  <p class="book-author">{{ book.author }}</p>
                  <div class="book-meta">
                    <span class="publisher">{{ book.publisher }}</span>
                  </div>
                  <div class="book-price-row">
                    <span class="current-price">¥{{ book.price }}</span>
                    <span class="original-price" v-if="book.originalPrice">¥{{ book.originalPrice }}</span>
                    <span class="discount-tag" v-if="book.originalPrice">省¥{{ (book.originalPrice - book.price).toFixed(0) }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
        
        <div class="latest-books">
          <div class="section-header">
            <h2 class="section-title">
              <el-icon class="section-icon"><Clock /></el-icon>
              最新发布
            </h2>
            <span class="section-badge new-badge">新鲜上架</span>
          </div>
          <el-row :gutter="20">
            <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="book in latestBooks" :key="book.id">
              <el-card shadow="hover" class="book-card card-hover" @click="goToDetail(book.id)">
                <div class="book-image-wrapper">
                  <div v-if="!book.images" class="book-cover-placeholder">
                    <el-icon :size="64" class="book-icon"><Notebook /></el-icon>
                  </div>
                  <el-image v-else :src="book.images.split(',')[0]" fit="cover" class="book-image" />
                </div>
                <div class="book-info">
                  <h3 class="book-title">{{ book.title }}</h3>
                  <p class="book-author">{{ book.author }}</p>
                  <div class="book-meta">
                    <span class="publisher">{{ book.publisher }}</span>
                  </div>
                  <div class="book-price-row">
                    <span class="current-price">¥{{ book.price }}</span>
                    <span class="original-price" v-if="book.originalPrice">¥{{ book.originalPrice }}</span>
                    <span class="discount-tag" v-if="book.originalPrice">省¥{{ (book.originalPrice - book.price).toFixed(0) }}</span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
      
      <el-footer class="footer">
        <div class="footer-content">
          <div class="footer-logo">
            <el-icon :size="24"><Reading /></el-icon>
            <span>校园二手书交易平台</span>
          </div>
          <p class="footer-copyright">&copy; 2024 校园二手书交易平台 - 让知识流转起来</p>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Search, Reading, Plus, HotWater, Clock, UserFilled, House, Notebook, CaretBottom, User, Document, ShoppingCart, ShoppingBag, ArrowRight } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getHotBooks, getLatestBooks } from '@/api/book'
import type { Book } from '@/types'

const router = useRouter()
const userStore = useUserStore()
const searchKeyword = ref('')
const hotBooks = ref<Book[]>([])
const latestBooks = ref<Book[]>([])

const handleSearch = () => {
  const keyword = searchKeyword.value
  console.log('搜索关键词:', keyword)
  
  if (keyword && keyword.trim()) {
    window.location.href = '/#/books?keyword=' + encodeURIComponent(keyword.trim())
  } else {
    window.location.href = '/#/books'
  }
}

const goToDetail = (id: number) => {
  router.push(`/book/${id}`)
}

const handleCommand = (command: string) => {
  if (command === 'logout') {
    userStore.logout()
    ElMessage.success('已退出登录')
    router.push('/')
  } else {
    router.push(command)
  }
}

const loadData = async () => {
  try {
    const [hotRes, latestRes] = await Promise.all([
      getHotBooks(8),
      getLatestBooks(8)
    ])
    hotBooks.value = hotRes.data || []
    latestBooks.value = latestRes.data || []
  } catch (error) {
    console.error('加载数据失败:', error)
  }
}

onMounted(() => {
  if (userStore.token && !userStore.userInfo) {
    userStore.getUserInfoAction()
  }
  loadData()
})
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  background-attachment: fixed;
}

.header {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 100%;
  padding: 0 20px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  color: #667eea;
  animation: float 3s ease-in-out infinite;
}

.logo-text {
  font-size: 22px;
  font-weight: 700;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nav-menu {
  flex: 1;
  margin: 0 60px;
}

.custom-nav-menu {
  background: transparent;
  border: none;
}

.custom-nav-menu .el-menu-item {
  color: #333;
  font-weight: 500;
  transition: all 0.3s ease;
}

.custom-nav-menu .el-menu-item:hover,
.custom-nav-menu .el-menu-item.is-active {
  color: #667eea;
  background: rgba(102, 126, 234, 0.1);
}

.user-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.publish-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  padding: 8px 20px;
  border-radius: 25px;
  transition: all 0.3s ease;
}

.publish-btn:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4090 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 15px;
  border-radius: 30px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: rgba(102, 126, 234, 0.1);
}

.user-avatar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.username {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.arrow-icon {
  font-size: 14px;
  color: #999;
  transition: transform 0.3s ease;
}

.user-info:hover .arrow-icon {
  transform: rotate(180deg);
}

.login-btn {
  color: #667eea;
  border-color: #667eea;
  padding: 8px 20px;
  border-radius: 25px;
  transition: all 0.3s ease;
}

.login-btn:hover {
  background: rgba(102, 126, 234, 0.1);
}

.register-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  color: white;
  padding: 8px 20px;
  border-radius: 25px;
  transition: all 0.3s ease;
}

.register-btn:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4090 100%);
  transform: translateY(-2px);
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 40px 20px;
}

.hero-section {
  text-align: center;
  padding: 60px 0;
  margin-bottom: 30px;
}

.hero-content {
  animation: fadeInUp 0.8s ease-out;
}

.hero-title {
  font-size: 42px;
  font-weight: 700;
  color: white;
  margin-bottom: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 15px;
}

.hero-icon {
  animation: float 3s ease-in-out infinite;
}

.hero-subtitle {
  font-size: 18px;
  color: rgba(255, 255, 255, 0.85);
}

.search-section {
  margin-bottom: 40px;
}

.search-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  border: none;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
}

.search-box {
  max-width: 700px;
  margin: 0 auto;
}

.search-input {
  border-radius: 30px;
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.2);
}

.search-btn {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 0 30px 30px 0;
  padding: 0 30px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
}

.search-btn:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4090 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.search-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.3);
}

.search-button-native {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  border-radius: 0 30px 30px 0;
  padding: 12px 30px;
  color: white;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.4);
  height: 100%;
}

.search-button-native:hover {
  background: linear-gradient(135deg, #5a6fd6 0%, #6a4090 100%);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.5);
}

.search-button-native:active {
  transform: translateY(0);
  box-shadow: 0 2px 10px rgba(102, 126, 234, 0.3);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
}

.section-title {
  display: flex;
  align-items: center;
  gap: 12px;
  color: white;
  font-size: 28px;
  font-weight: 600;
}

.section-icon {
  color: #ffd700;
}

.section-badge {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a5a 100%);
  color: white;
  padding: 5px 15px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.new-badge {
  background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
}

.book-card {
  cursor: pointer;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 15px;
  border: none;
  margin-bottom: 20px;
  overflow: hidden;
}

.book-card:hover {
  box-shadow: 0 15px 40px rgba(0, 0, 0, 0.2);
}

.book-image-wrapper {
  width: 100%;
  height: 220px;
  overflow: hidden;
  position: relative;
}

.book-image {
  width: 100%;
  height: 100%;
  transition: transform 0.3s ease;
}

.book-card:hover .book-image {
  transform: scale(1.05);
}

.book-cover-placeholder {
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.book-cover-placeholder::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(45deg, rgba(255,255,255,0.1) 25%, transparent 25%, transparent 50%, rgba(255,255,255,0.1) 50%, rgba(255,255,255,0.1) 75%, transparent 75%, transparent);
  background-size: 20px 20px;
  animation: shimmer 3s linear infinite;
}

.book-icon {
  color: rgba(255, 255, 255, 0.9);
  z-index: 1;
}

.book-info {
  padding: 20px;
}

.book-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  color: #333;
}

.book-author {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.book-meta {
  margin-bottom: 12px;
}

.publisher {
  font-size: 12px;
  color: #999;
  background: #f5f5f5;
  padding: 3px 10px;
  border-radius: 10px;
}

.book-price-row {
  display: flex;
  align-items: center;
  gap: 10px;
}

.current-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: 700;
}

.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.discount-tag {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a5a 100%);
  color: white;
  padding: 3px 8px;
  border-radius: 8px;
  font-size: 12px;
  font-weight: 500;
}

.footer {
  background: rgba(0, 0, 0, 0.3);
  backdrop-filter: blur(10px);
  color: white;
  text-align: center;
  padding: 30px 20px;
  margin-top: 60px;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-bottom: 10px;
  color: rgba(255, 255, 255, 0.9);
}

.footer-copyright {
  color: rgba(255, 255, 255, 0.7);
  font-size: 14px;
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

@keyframes float {
  0%, 100% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-8px);
  }
}

@keyframes shimmer {
  0% {
    transform: translateX(-100%);
  }
  100% {
    transform: translateX(100%);
  }
}
</style>
