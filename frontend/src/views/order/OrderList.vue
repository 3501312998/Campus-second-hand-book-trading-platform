<template>
  <div class="order-list">
    <div class="container">
      <h2>我的订单</h2>
      
      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待付款" name="pending" />
        <el-tab-pane label="待发货" name="paid" />
        <el-tab-pane label="已完成" name="completed" />
        <el-tab-pane label="已取消" name="cancelled" />
      </el-tabs>
      
      <el-table :data="orders" style="width: 100%" v-loading="loading">
        <el-table-column type="expand">
          <template #default="{ row }">
            <div class="order-items">
              <div v-for="item in row.items" :key="item.id" class="order-item">
                <span>{{ item.bookTitle }}</span>
                <span>× 1</span>
                <span class="price">¥{{ item.price }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="totalAmount" label="总价" width="120">
          <template #default="{ row }">
            <span class="price">¥{{ row.totalAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="180" />
        <el-table-column label="操作" width="150">
          <template #default="{ row }">
            <el-button 
              v-if="row.status === 0" 
              link 
              type="primary" 
              @click="handlePay(row)"
            >
              付款
            </el-button>
            <el-button 
              v-if="row.status === 0" 
              link 
              type="danger" 
              @click="handleCancel(row)"
            >
              取消
            </el-button>
            <el-button 
              v-if="row.status === 3" 
              link 
              type="success" 
              @click="handleConfirm(row)"
            >
              确认收货
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <el-pagination
        v-if="total > 0"
        v-model:current-page="pagination.pageNum"
        v-model:page-size="pagination.pageSize"
        :total="total"
        layout="total, prev, pager, next"
        @current-change="loadOrders"
        style="margin-top: 20px; justify-content: center"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOrderList, cancelOrder, payOrder, confirmOrder } from '@/api/order'

const loading = ref(false)
const activeTab = ref('all')
const orders = ref([])
const total = ref(0)
const pagination = reactive({
  pageNum: 1,
  pageSize: 10
})

const statusMap: any = {
  0: '待付款',
  1: '待发货',
  2: '已发货',
  3: '已完成',
  4: '已取消'
}

const getStatusText = (status: number) => statusMap[status] || '未知'

const getStatusType = (status: number) => {
  const types: any = {
    0: 'warning',
    1: 'info',
    2: 'primary',
    3: 'success',
    4: 'info'
  }
  return types[status] || 'info'
}

const handleTabChange = () => {
  pagination.pageNum = 1
  loadOrders()
}

const loadOrders = async () => {
  loading.value = true
  try {
    const status = activeTab.value === 'all' ? null : 
      activeTab.value === 'pending' ? 0 :
      activeTab.value === 'paid' ? 1 :
      activeTab.value === 'completed' ? 3 : 4
    
    const res = await getOrderList({
      ...pagination,
      status
    })
    orders.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('加载失败', error)
  } finally {
    loading.value = false
  }
}

const handlePay = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要付款吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    await payOrder(row.id)
    ElMessage.success('付款成功')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('付款失败', error)
    }
  }
}

const handleCancel = async (row: any) => {
  try {
    await ElMessageBox.confirm('确定要取消订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await cancelOrder(row.id)
    ElMessage.success('订单已取消')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('取消失败', error)
    }
  }
}

const handleConfirm = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认已收到货物吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    await confirmOrder(row.id)
    ElMessage.success('已确认收货')
    loadOrders()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('确认失败', error)
    }
  }
}

onMounted(() => {
  loadOrders()
})
</script>

<style scoped>
.order-list {
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

.order-items {
  padding: 10px 0;
}

.order-item {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid #eee;
}

.order-item:last-child {
  border-bottom: none;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}
</style>
