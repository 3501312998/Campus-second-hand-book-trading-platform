<template>
  <div class="my-books">
    <div class="container">
      <h2>我的发布</h2>
      
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待审核" name="pending" />
        <el-tab-pane label="已上架" name="approved" />
        <el-tab-pane label="已拒绝" name="rejected" />
      </el-tabs>
      
      <el-table :data="books" style="width: 100%" v-loading="loading">
        <el-table-column prop="title" label="书名" />
        <el-table-column prop="price" label="价格" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="condition" label="成色" width="100">
          <template #default="{ row }">
            {{ getConditionText(row.condition) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览量" width="100" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-if="total > 0"
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadBooks"
        style="margin-top: 20px; justify-content: center"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMyBooks, deleteBook } from '@/api/book'

const loading = ref(false)
const activeTab = ref('all')
const books = ref([])
const total = ref(0)
const pagination = reactive({
  pageNum: 1,
  pageSize: 10
})

const statusMap = {
  0: '待审核',
  1: '已上架',
  2: '已下架',
  3: '已拒绝'
}

const conditionMap = {
  5: '全新',
  4: '九成新',
  3: '八成新',
  2: '七成新',
  1: '六成新及以下'
}

const getStatusText = (status: number) => statusMap[status] || '未知'
const getConditionText = (condition: number) => conditionMap[condition] || '未知'

const getStatusType = (status: number) => {
  const types: any = {
    0: 'warning',
    1: 'success',
    2: 'info',
    3: 'danger'
  }
  return types[status] || 'info'
}

const handleTabChange = () => {
  pagination.pageNum = 1
  loadBooks()
}

const loadBooks = async () => {
  loading.value = true
  try {
    const status = activeTab.value === 'all' ? null : 
      activeTab.value === 'pending' ? 0 :
      activeTab.value === 'approved' ? 1 : 3
    
    const res = await getMyBooks({
      ...pagination,
      status
    })
    books.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载失败', error)
  } finally {
    loading.value = false
  }
}

const handleEdit = (row: any) => {
  ElMessage.info('编辑功能开发中，敬请期待')
}

const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要删除这本书吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await deleteBook(row.id)
    ElMessage.success('删除成功')
    loadBooks()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('删除失败', error)
    }
  }
}

onMounted(() => {
  loadBooks()
})
</script>

<style scoped>
.my-books {
  padding: 20px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  background: white;
  padding: 30px;
  border-radius: 8px;
}

h2 {
  margin-bottom: 30px;
  text-align: center;
}
</style>
