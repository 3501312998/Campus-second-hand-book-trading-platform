export interface LoginForm {
  username: string
  password: string
  captcha?: string
  captchaKey?: string
}

export interface RegisterForm {
  username: string
  studentId: string
  realName: string
  phone: string
  password: string
  confirmPassword: string
  email?: string
}

export interface Book {
  id: number
  title: string
  author: string
  publisher: string
  isbn: string
  price: number
  originalPrice: number
  condition: string
  categoryId: number
  categoryName?: string
  userId: number
  userName?: string
  description: string
  images: string
  viewCount: number
  favoriteCount: number
  contactQq: string
  contactWechat: string
  status: number
  createTime: string
}

export interface Category {
  id: number
  name: string
  parentId: number
  level: number
  sort: number
  icon: string
  description: string
  status: number
  children?: Category[]
}

export interface Order {
  id: number
  orderNo: string
  userId: number
  totalAmount: number
  actualAmount: number
  status: number
  statusText?: string
  receiverName: string
  receiverPhone: string
  receiverAddress: string
  remark: string
  createTime: string
  items?: OrderItem[]
}

export interface OrderItem {
  id: number
  orderId: number
  bookId: number
  sellerId: number
  title: string
  author: string
  price: number
  quantity: number
  subtotal: number
  bookImage: string
}

export interface CartItem {
  id: number
  userId: number
  bookId: number
  quantity: number
  book?: Book
}

export interface PageResult<T> {
  records: T[]
  total: number
  size: number
  current: number
  pages: number
}

export interface Result<T = any> {
  code: number
  message: string
  data: T
  timestamp: number
}
