<template>
  <div class="book-detail-container">
    <div class="container" v-if="book">
      <el-card class="book-detail-card">
        <el-row :gutter="40">
          <el-col :span="10">
            <div class="book-images">
              <el-image 
                :src="book.images?.split(',')[0] || '/placeholder.jpg'" 
                fit="cover"
                class="main-image"
              />
              <div class="thumbnail-list" v-if="book.images?.includes(',')">
                <el-image
                  v-for="(img, index) in book.images.split(',')"
                  :key="index"
                  :src="img"
                  fit="cover"
                  @error="onThumbError(index)"
                  :class="{ active: currentImageIndex === index }"
                  @click="currentImageIndex = index"
                />
              </div>
            </div>
          </el-col>
          
          <el-col :span="14">
            <div class="book-info">
              <h1 class="book-title">{{ book.title }}</h1>
              
              <div class="book-meta">
                <span class="meta-item">
                  <el-icon><User /></el-icon>
                  作者：{{ book.author || '未知' }}
                </span>
                <span class="meta-item">
                  <el-icon><OfficeBuilding /></el-icon>
                  出版社：{{ book.publisher || '未知' }}
                </span>
                <span class="meta-item">
                  <el-icon><Collection /></el-icon>
                  分类：{{ book.categoryName || '未分类' }}
                </span>
              </div>
              
              <div class="price-section">
                <div class="price-info">
                  <span class="current-price">¥{{ book.price }}</span>
                  <span class="original-price" v-if="book.originalPrice">
                    原价：¥{{ book.originalPrice }}
                  </span>
                  <span class="discount" v-if="book.originalPrice">
                    {{ Math.round((book.price / book.originalPrice) * 10) }}折
                  </span>
                </div>
                <div class="condition">
                  <el-tag type="success" size="large">{{ book.condition }}</el-tag>
                </div>
              </div>
              
              <div class="stats">
                <span class="stat-item">
                  <el-icon><View /></el-icon>
                  {{ book.viewCount }} 次浏览
                </span>
                <span class="stat-item">
                  <el-icon><Star /></el-icon>
                  {{ book.favoriteCount }} 次收藏
                </span>
              </div>
              
              <div class="description">
                <h3>书籍描述</h3>
                <p>{{ book.description || '暂无描述' }}</p>
              </div>
              
              <div class="contact-info">
                <h3>联系方式</h3>
                <div class="contact-item" v-if="book.contactQq">
                  <el-icon><ChatDotRound /></el-icon>
                  QQ：{{ book.contactQq }}
                </div>
                <div class="contact-item" v-if="book.contactWechat">
                  <el-icon><ChatLineRound /></el-icon>
                  微信：{{ book.contactWechat }}
                </div>
              </div>
              
              <div class="action-buttons">
                <el-button type="primary" size="large" @click="handleAddToCart">
                  <el-icon><ShoppingCart /></el-icon>
                  加入购物车
                </el-button>
                <el-button size="large" @click="handleFavorite">
                  <el-icon><Star /></el-icon>
                  收藏
                </el-button>
              </div>
              
              <div class="publish-info">
                <el-divider />
                <div class="publisher">
                  <el-avatar :icon="UserFilled" />
                  <div class="publisher-info">
                    <span class="publisher-name">{{ book.userName || '匿名用户' }}</span>
                    <span class="publish-time">发布于 {{ formatTime(book.createTime) }}</span>
                  </div>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-card>
    </div>
    
    <div v-else class="loading">
      <el-skeleton :rows="10" animated />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  User, OfficeBuilding, Collection, View, Star, 
  ShoppingCart, ChatDotRound, ChatLineRound, UserFilled 
} from '@element-plus/icons-vue'
import { getBookDetail } from '@/api/book'
import { addToCart } from '@/api/order'
import { useUserStore } from '@/stores/user'
import type { Book } from '@/types'
import dayjs from 'dayjs'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const book = ref<Book | null>(null)
const currentImageIndex = ref(0)

const formatTime = (time: string) => {
  return dayjs(time).format('YYYY-MM-DD HH:mm')
}

const onThumbError = (index: number) => {
  console.log('图片加载失败:', index)
}

const handleAddToCart = async () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  if (!book.value) return
  
  try {
    await addToCart({ bookId: book.value.id })
    ElMessage.success('添加购物车成功')
  } catch (error) {
    console.error('添加购物车失败:', error)
    ElMessage.error('添加失败')
  }
}

const handleFavorite = () => {
  if (!userStore.token) {
    ElMessage.warning('请先登录')
    router.push('/login')
    return
  }
  ElMessage.info('功能开发中，敬请期待')
}

const loadBookDetail = async () => {
  try {
    const id = Number(route.params.id)
    const res = await getBookDetail(id)
    book.value = res.data
  } catch (error) {
    console.error('加载书籍详情失败:', error)
    ElMessage.error('加载失败')
  }
}

onMounted(() => {
  loadBookDetail()
})
</script>

<style scoped>
.book-detail-container {
  background-color: #f5f7fa;
  padding: 30px 0;
  min-height: calc(100vh - 200px);
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.book-detail-card {
  padding: 20px;
}

.book-images {
  position: sticky;
  top: 20px;
}

.main-image {
  width: 100%;
  height: 400px;
  border-radius: 8px;
  margin-bottom: 15px;
}

.thumbnail-list {
  display: flex;
  gap: 10px;
  overflow-x: auto;
}

.thumbnail-list .el-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  cursor: pointer;
  border: 2px solid transparent;
  transition: border-color 0.3s;
}

.thumbnail-list .el-image.active {
  border-color: #409eff;
}

.book-title {
  font-size: 28px;
  margin-bottom: 20px;
  color: #333;
}

.book-meta {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #666;
}

.price-section {
  display: flex;
  align-items: center;
  gap: 20px;
  margin-bottom: 20px;
  padding: 20px;
  background: linear-gradient(135deg, #fff5f5 0%, #fff0f0 100%);
  border-radius: 8px;
}

.price-info {
  display: flex;
  align-items: baseline;
  gap: 15px;
}

.current-price {
  font-size: 36px;
  color: #f56c6c;
  font-weight: bold;
}

.original-price {
  font-size: 16px;
  color: #999;
  text-decoration: line-through;
}

.discount {
  padding: 4px 8px;
  background-color: #f56c6c;
  color: white;
  border-radius: 4px;
  font-size: 12px;
}

.stats {
  display: flex;
  gap: 30px;
  margin-bottom: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #999;
}

.description, .contact-info {
  margin-bottom: 20px;
}

.description h3, .contact-info h3 {
  font-size: 18px;
  margin-bottom: 10px;
  color: #333;
}

.description p {
  color: #666;
  line-height: 1.8;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin: 30px 0;
}

.action-buttons .el-button {
  flex: 1;
  height: 50px;
  font-size: 16px;
}

.publisher {
  display: flex;
  align-items: center;
  gap: 15px;
}

.publisher-info {
  display: flex;
  flex-direction: column;
}

.publisher-name {
  font-weight: bold;
  color: #333;
}

.publish-time {
  font-size: 12px;
  color: #999;
}

.loading {
  max-width: 800px;
  margin: 50px auto;
  padding: 0 20px;
}
</style>
