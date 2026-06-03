import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'
import router from '@/router'

const service: AxiosInstance = axios.create({
  baseURL: '/api',
  timeout: 30000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
})

service.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    return config
  },
  (error) => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

service.interceptors.response.use(
  (response: AxiosResponse) => {
    const res = response.data
    
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      
      if (res.code === 401) {
        const userStore = useUserStore()
        userStore.logout()
        router.push('/login')
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    
    return res
  },
  (error) => {
    console.error('响应错误：', error)
    
    if (error.response) {
      const status = error.response.status
      
      switch (status) {
        case 400:
          ElMessage.error('请求参数错误')
          break
        case 401:
          ElMessage.error('未授权，请先登录')
          const userStore = useUserStore()
          userStore.logout()
          router.push('/login')
          break
        case 403:
          // 如果是公共接口返回403，可能是localStorage中有无效token
          const publicPaths = ['/user/login', '/user/register', '/category/list', '/book/list', '/book/detail']
          const requestUrl = error.config?.url || ''
          const isPublic = publicPaths.some(path => requestUrl.startsWith(path))
          
          if (isPublic) {
            // 清除无效token并重试
            const userStore = useUserStore()
            if (userStore.token) {
              userStore.logout()
              // 重试请求（不携带token）
              const newConfig = { ...error.config }
              delete newConfig.headers['Authorization']
              return service(newConfig)
            } else {
              // token已经为空，仍然返回403，可能是其他问题
              ElMessage.error('拒绝访问')
            }
          } else {
            ElMessage.error('拒绝访问')
          }
          break
        case 404:
          ElMessage.error('请求的资源不存在')
          break
        case 500:
          ElMessage.error('服务器错误')
          break
        default:
          ElMessage.error(error.response.data?.message || '请求失败')
      }
    } else if (error.request) {
      ElMessage.error('网络错误，请检查您的网络连接')
    } else {
      ElMessage.error('请求配置错误')
    }
    
    return Promise.reject(error)
  }
)

export default service
