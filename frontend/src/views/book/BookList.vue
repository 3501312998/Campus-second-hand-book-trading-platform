<template>
    <div class="book-list-container">
        <div class="header">
            <div class="container">
                <div class="header-content">
                    <h1 class="page-title">书籍列表</h1>
                    <p class="page-subtitle">发现更多优质二手书籍</p>
                </div>
            </div>
        </div>

        <div class="container main-content">
            <el-row :gutter="24">
                <el-col :span="8" class="sidebar">
                    <el-card class="filter-card">
                        <template #header>
                            <div class="filter-header">
                                <el-icon class="filter-icon">
                                    <Filter />
                                </el-icon>
                                <span>筛选条件</span>
                            </div>
                        </template>

                        <el-form label-position="top" class="filter-form">
                            <el-form-item label="分类">
                                <el-select v-model="queryParams.categoryId" placeholder="选择分类" clearable
                                    class="filter-select">
                                    <el-option v-for="category in categories" :key="category.id" :label="category.name"
                                        :value="category.id" />
                                </el-select>
                            </el-form-item>

                            <el-form-item label="成色">
                                <el-select v-model="queryParams.condition" placeholder="选择成色" clearable
                                    class="filter-select">
                                    <el-option label="全新" value="全新" />
                                    <el-option label="九成新" value="九成新" />
                                    <el-option label="八成新" value="八成新" />
                                    <el-option label="七成新" value="七成新" />
                                    <el-option label="六成新及以下" value="六成新及以下" />
                                </el-select>
                            </el-form-item>

                            <el-form-item label="价格区间">
                                <div class="price-range">
                                    <el-input-number v-model="queryParams.minPrice" :min="0" placeholder="最低价"
                                        class="price-input" />
                                    <span class="price-separator">-</span>
                                    <el-input-number v-model="queryParams.maxPrice" :min="0" placeholder="最高价"
                                        class="price-input" />
                                </div>
                            </el-form-item>

                            <el-form-item class="filter-actions">
                                <el-button type="primary" class="search-btn" @click="handleSearch">
                                    <el-icon>
                                        <Search />
                                    </el-icon> 搜索
                                </el-button>
                                <el-button class="reset-btn" @click="handleReset">重置</el-button>
                            </el-form-item>
                        </el-form>
                    </el-card>
                </el-col>

                <el-col :span="16">
                    <div class="search-bar">
                        <el-input v-model="queryParams.keyword" placeholder="搜索书名、作者、ISBN..." clearable
                            class="search-input" @keyup.enter="handleSearch">
                            <template #prepend>
                                <el-icon class="search-icon">
                                    <Search />
                                </el-icon>
                            </template>
                            <template #append>
                                <el-button type="primary" class="search-button" @click="handleSearch">搜索</el-button>
                            </template>
                        </el-input>
                    </div>

                    <div class="results-info">
                        <span>共找到 <strong>{{ total }}</strong> 本图书</span>
                    </div>

                    <div class="book-grid">
                        <el-row :gutter="20">
                            <el-col :xs="24" :sm="12" :md="8" v-for="book in books" :key="book.id">
                                <el-card shadow="hover" class="book-card card-hover" @click="goToDetail(book.id)">
                                    <div class="book-image-wrapper">
                                        <div v-if="!book.images || imageErrors.has(book.id)" class="book-cover-placeholder">
                                            <div class="placeholder-title">{{ book.title }}</div>
                                            <el-icon :size="48" class="book-icon">
                                                <Notebook />
                                            </el-icon>
                                        </div>
                                        <el-image v-else :src="book.images.split(',')[0]" fit="cover"
                                            class="book-image" @error="onImageError(book.id)" />
                                    </div>
                                    <div class="book-info">
                                        <h3 class="book-title">{{ book.title }}</h3>
                                        <p class="book-author">{{ book.author || '未知作者' }}</p>
                                        <div class="book-meta">
                                            <el-tag size="small" class="condition-tag">{{ book.condition }}</el-tag>
                                            <span class="view-count">浏览 {{ book.viewCount }}</span>
                                        </div>
                                        <div class="book-price-row">
                                            <span class="current-price">¥{{ book.price }}</span>
                                            <span class="original-price" v-if="book.originalPrice">¥{{
                                                book.originalPrice }}</span>
                                            <span class="discount-tag" v-if="book.originalPrice">省¥{{
                                                (book.originalPrice -
                                                    book.price).toFixed(0) }}</span>
                                        </div>
                                    </div>
                                </el-card>
                            </el-col>
                        </el-row>
                    </div>

                    <div class="empty-state" v-if="total === 0">
                        <el-empty description="暂无符合条件的书籍" />
                    </div>

                    <div class="pagination" v-if="total > 0">
                        <el-pagination v-model:current-page="queryParams.pageNum"
                            v-model:page-size="queryParams.pageSize" :total="total" :page-sizes="[9, 12, 24, 36]"
                            layout="total, sizes, prev, pager, next, jumper" class="pagination-component"
                            @size-change="handleSizeChange" @current-change="handleCurrentChange" />
                    </div>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { Search, Filter, Notebook } from '@element-plus/icons-vue'
import { getBookList } from '@/api/book'
import type { Book, PageResult } from '@/types'

const router = useRouter()
const route = useRoute()

const books = ref<Book[]>([])
const total = ref(0)
const categories = ref<any[]>([])

const imageErrors = ref(new Set<number>())

const onImageError = (bookId: number) => {
    imageErrors.value.add(bookId)
}

const queryParams = reactive({
    pageNum: 1,
    pageSize: 12,
    keyword: '',
    categoryId: null as number | null,
    condition: '',
    minPrice: null as number | null,
    maxPrice: null as number | null
})

const handleSearch = () => {
    queryParams.pageNum = 1
    loadBooks()
}

const handleReset = () => {
    queryParams.categoryId = null
    queryParams.condition = ''
    queryParams.minPrice = null
    queryParams.maxPrice = null
    handleSearch()
}

const handleSizeChange = (val: number) => {
    queryParams.pageSize = val
    loadBooks()
}

const handleCurrentChange = (val: number) => {
    queryParams.pageNum = val
    loadBooks()
}

const goToDetail = (id: number) => {
    router.push(`/book/${id}`)
}

const loadBooks = async () => {
    try {
        const res = await getBookList(queryParams)
        books.value = res.data.records || []
        total.value = res.data.total || 0
    } catch (error) {
        console.error('加载书籍列表失败:', error)
    }
}

onMounted(() => {
    if (route.query.keyword) {
        queryParams.keyword = route.query.keyword as string
    }
    loadBooks()
})
</script>

<style scoped>
.header {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    color: white;
    padding: 50px 0;
}

.header-content {
    animation: fadeInUp 0.6s ease-out;
}

.page-title {
    margin: 0;
    font-size: 36px;
    font-weight: 700;
}

.page-subtitle {
    margin: 10px 0 0;
    font-size: 16px;
    opacity: 0.9;
}

.container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 20px;
}

.main-content {
    padding: 40px 20px;
    background: #f5f5f5;
    min-height: calc(100vh - 200px);
}

.sidebar {
    margin-bottom: 20px;
}

.filter-card {
    background: rgba(255, 255, 255, 0.95);
    backdrop-filter: blur(10px);
    border-radius: 15px;
    border: none;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.filter-header {
    display: flex;
    align-items: center;
    gap: 8px;
    font-weight: 600;
    color: #333;
}

.filter-icon {
    color: #667eea;
}

.filter-form {
    padding-top: 10px;
}

.filter-select {
    width: 100%;
    border-radius: 10px;
}

.price-range {
    display: flex;
    align-items: center;
    gap: 10px;
}

.price-input {
    flex: 1;
    border-radius: 10px;
}

.price-separator {
    color: #999;
}

.filter-actions {
    display: flex;
    gap: 10px;
    margin-top: 10px;
}

.search-btn {
    flex: 1;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    border-radius: 10px;
}

.reset-btn {
    flex: 1;
    border-radius: 10px;
}

.search-bar {
    margin-bottom: 20px;
}

.search-input {
    border-radius: 30px;
    box-shadow: 0 2px 15px rgba(102, 126, 234, 0.15);
    border: 2px solid #e8e8e8;
    transition: all 0.3s ease;
}

.search-input:focus {
    border-color: #667eea;
    box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.search-icon {
    color: #999;
}

.search-button {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border: none;
    border-radius: 0 30px 30px 0;
    padding: 0 30px;
}

.results-info {
    color: #666;
    font-size: 14px;
    margin-bottom: 20px;
}

.results-info strong {
    color: #667eea;
    font-weight: 600;
}

.book-grid {
    margin-top: 10px;
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
    box-shadow: 0 15px 40px rgba(0, 0, 0, 0.15);
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
    background: linear-gradient(45deg, rgba(255, 255, 255, 0.1) 25%, transparent 25%, transparent 50%, rgba(255, 255, 255, 0.1) 50%, rgba(255, 255, 255, 0.1) 75%, transparent 75%, transparent);
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
    margin-bottom: 12px;
}

.book-meta {
    display: flex;
    align-items: center;
    gap: 10px;
    margin-bottom: 12px;
}

.condition-tag {
    background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
    color: white;
    border: none;
}

.view-count {
    display: flex;
    align-items: center;
    gap: 4px;
    color: #999;
    font-size: 12px;
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

.empty-state {
    text-align: center;
    padding: 60px 0;
}

.pagination {
    margin-top: 40px;
    display: flex;
    justify-content: center;
}

.pagination-component {
    background: rgba(255, 255, 255, 0.95);
    padding: 15px 30px;
    border-radius: 40px;
    box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
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
