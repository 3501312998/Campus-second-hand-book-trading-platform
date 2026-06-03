<template>
  <div class="publish-book">
    <div class="container">
      <h2>发布书籍</h2>
      
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="书名" prop="title">
          <el-input v-model="form.title" placeholder="请输入书名" />
        </el-form-item>
        
        <el-form-item label="作者" prop="author">
          <el-input v-model="form.author" placeholder="请输入作者" />
        </el-form-item>
        
        <el-form-item label="出版社" prop="publisher">
          <el-input v-model="form.publisher" placeholder="请输入出版社" />
        </el-form-item>
        
        <el-form-item label="ISBN" prop="isbn">
          <el-input v-model="form.isbn" placeholder="请输入ISBN" />
        </el-form-item>
        
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类">
            <el-option
              v-for="item in categories"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        
        <el-form-item label="售价" prop="price">
          <el-input-number v-model="form.price" :min="0.01" :precision="2" />
        </el-form-item>
        
        <el-form-item label="原价" prop="originalPrice">
          <el-input-number v-model="form.originalPrice" :min="0.01" :precision="2" />
        </el-form-item>
        
        <el-form-item label="成色" prop="condition">
          <el-select v-model="form.condition" placeholder="请选择成色">
            <el-option label="全新" :value="5" />
            <el-option label="九成新" :value="4" />
            <el-option label="八成新" :value="3" />
            <el-option label="七成新" :value="2" />
            <el-option label="六成新及以下" :value="1" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="4"
            placeholder="请输入书籍描述"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="submitForm">发布</el-button>
          <el-button @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { publishBook } from '@/api/book'
import { getCategoryList } from '@/api/category'

const router = useRouter()
const formRef = ref()
const categories = ref([])

const form = reactive({
  title: '',
  author: '',
  publisher: '',
  isbn: '',
  categoryId: null,
  price: 0.01,
  originalPrice: 0.01,
  condition: null,
  description: ''
})

const rules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  categoryId: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入售价', trigger: 'blur' }],
  condition: [{ required: true, message: '请选择成色', trigger: 'change' }]
}

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    categories.value = res.data
  } catch (error) {
    console.error('加载分类失败', error)
  }
}

const submitForm = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      try {
        await publishBook(form)
        ElMessage.success('发布成功，等待审核')
        router.push('/my-books')
      } catch (error) {
        console.error('发布失败', error)
      }
    }
  })
}

const resetForm = () => {
  formRef.value?.resetFields()
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.publish-book {
  padding: 20px;
}

.container {
  max-width: 800px;
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
