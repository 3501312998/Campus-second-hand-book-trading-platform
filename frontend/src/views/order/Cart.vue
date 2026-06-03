<template>
  <div class="cart">
    <div class="container">
      <h2>购物车</h2>
      
      <el-table :data="cartItems" style="width: 100%" v-loading="loading">
        <el-table-column width="50">
          <template #default="{ row }">
            <el-checkbox v-model="row.selected" @change="handleSelectChange" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="书名">
          <template #default="{ row }">
            <div>{{ row.book.title }}</div>
            <div class="book-info">作者：{{ row.book.author }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="价格" width="120">
          <template #default="{ row }">
            <span class="price">¥{{ row.book.price }}</span>
          </template>
        </el-table-column>
        <el-table-column label="卖家" width="120">
          <template #default="{ row }">
            {{ row.sellerUsername }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="{ row }">
            <el-button link type="danger" @click="handleRemove(row.id)">
              移除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="cart-footer" v-if="cartItems.length > 0">
        <div class="total">
          已选择 {{ selectedCount }} 件商品，总计：
          <span class="total-price">¥{{ totalPrice }}</span>
        </div>
        <el-button type="primary" size="large" @click="handleCheckout">
          去结算
        </el-button>
      </div>
      
      <el-empty v-else description="购物车是空的" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getCartList, removeFromCart, createOrder } from '@/api/order'

const router = useRouter()
const loading = ref(false)
const cartItems = ref([])

const selectedCount = computed(() => {
  return cartItems.value.filter((item: any) => item.selected).length
})

const totalPrice = computed(() => {
  return cartItems.value
    .filter((item: any) => item.selected)
    .reduce((sum: number, item: any) => sum + item.book.price, 0)
    .toFixed(2)
})

const loadCart = async () => {
  loading.value = true
  try {
    const res = await getCartList()
    cartItems.value = res.data.map((item: any) => ({
      ...item,
      selected: false
    }))
  } catch (error) {
    console.error('加载失败', error)
  } finally {
    loading.value = false
  }
}

const handleSelectChange = () => {
  // 选中状态变化
}

const handleRemove = async (id: number) => {
  try {
    await ElMessageBox.confirm('确定要移除这件商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    await removeFromCart(id)
    ElMessage.success('移除成功')
    loadCart()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('移除失败', error)
    }
  }
}

const handleCheckout = async () => {
  const selectedItems = cartItems.value.filter((item: any) => item.selected)
  
  if (selectedItems.length === 0) {
    ElMessage.warning('请选择要结算的商品')
    return
  }
  
  try {
    await ElMessageBox.confirm('确定要创建订单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'info'
    })
    
    const bookIds = selectedItems.map((item: any) => item.bookId)
    await createOrder({ bookIds })
    ElMessage.success('订单创建成功')
    router.push('/orders')
  } catch (error) {
    if (error !== 'cancel') {
      console.error('创建订单失败', error)
    }
  }
}

onMounted(() => {
  loadCart()
})
</script>

<style scoped>
.cart {
  padding: 20px;
}

.container {
  max-width: 1000px;
  margin: 0 auto;
  background: white;
  padding: 30px;
  border-radius: 8px;
}

h2 {
  margin-bottom: 30px;
  text-align: center;
}

.book-info {
  font-size: 12px;
  color: #999;
  margin-top: 4px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

.cart-footer {
  margin-top: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 8px;
}

.total {
  font-size: 16px;
}

.total-price {
  font-size: 24px;
  color: #f56c6c;
  font-weight: bold;
  margin-left: 10px;
}
</style>
