# 校园二手书交易平台

> 基于 Spring Boot + Vue3 + TypeScript 的校园二手书交易平台

## 📚 项目简介

本项目是一个面向高校学生的二手书交易平台，旨在为学生提供便捷的二手书买卖服务，促进书籍资源的循环利用。

## ✨ 功能特性

### 用户功能
- **用户注册/登录** - 支持账号密码登录
- **个人信息管理** - 查看和修改个人资料
- **书籍浏览** - 浏览所有在售书籍，支持分类筛选
- **书籍搜索** - 按书名、作者等关键词搜索
- **书籍详情** - 查看书籍详细信息和卖家联系方式
- **购物车** - 添加、查看、删除购物车商品
- **订单管理** - 创建订单、查看订单状态
- **发布书籍** - 发布二手书出售信息

### 技术特性
- 前后端分离架构
- JWT 身份认证
- RESTful API 设计
- 响应式前端界面
- MySQL 数据库支持

## 🛠️ 技术栈

### 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 编程语言 |
| Spring Boot | 3.2.1 | 后端框架 |
| Spring Security | 6.x | 安全框架 |
| MyBatis Plus | 3.5.5 | ORM框架 |
| JWT | 0.12.3 | 身份认证 |
| MySQL | 8.0+ | 数据库 |

### 前端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.x | 前端框架 |
| TypeScript | 5.4.x | 类型安全 |
| Vite | 5.4.x | 构建工具 |
| Element Plus | 2.6.x | UI组件库 |
| Pinia | 2.1.x | 状态管理 |
| Vue Router | 4.3.x | 路由管理 |
| Axios | 1.6.x | HTTP客户端 |

## 📁 项目结构

```
校园二手书交易平台/
├── backend/                    # 后端代码
│   ├── src/main/java/         # Java源码
│   ├── src/main/resources/    # 配置文件
│   ├── sql/                   # 数据库脚本
│   └── pom.xml                # Maven配置
├── frontend/                  # 前端代码
│   ├── src/                   # Vue源码
│   ├── dist/                  # 构建产物
│   └── package.json           # npm配置
├── report/                    # 课程设计报告
└── README.md                  # 项目说明
```

## 🚀 快速开始

### 环境要求

- JDK 17+
- Node.js 18+
- MySQL 8.0+
- Maven 3.8+

### 1. 数据库配置

创建数据库并执行初始化脚本：

```sql
CREATE DATABASE IF NOT EXISTS bookshare CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE bookshare;
```

执行 SQL 脚本：
```bash
mysql -u username -p bookshare < backend/sql/init.sql
mysql -u username -p bookshare < backend/sql/insert-test-data.sql
```

### 2. 后端启动

```bash
cd backend
mvn spring-boot:run
```

后端服务默认运行在 `http://localhost:8080/api`

### 3. 前端启动

```bash
cd frontend
npm install
npm run dev
```

前端服务默认运行在 `http://localhost:5173`

## 🔧 配置说明

### 后端配置 (`backend/src/main/resources/application.yml`)

```yaml
server:
  port: 8080                    # 端口
  servlet:
    context-path: /api          # 上下文路径

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/bookshare?useSSL=false&serverTimezone=Asia/Shanghai
    username: root              # 数据库用户名
    password: password          # 数据库密码
```

### 前端配置 (`frontend/src/utils/request.ts`)

```typescript
const service = axios.create({
  baseURL: '/api',              # 后端API地址
  timeout: 30000
})
```

## 🔌 API 接口

### 用户接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/user/register` | 用户注册 |
| POST | `/api/user/login` | 用户登录 |
| GET | `/api/user/info` | 获取用户信息 |
| PUT | `/api/user/update` | 更新用户信息 |

### 书籍接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/book/list` | 获取书籍列表 |
| GET | `/api/book/detail/{id}` | 获取书籍详情 |
| POST | `/api/book/publish` | 发布书籍 |
| DELETE | `/api/book/delete/{id}` | 删除书籍 |

### 分类接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/category/list` | 获取分类列表 |

### 购物车接口
| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/cart/list` | 获取购物车列表 |
| POST | `/api/cart/add` | 添加购物车 |
| DELETE | `/api/cart/delete/{id}` | 删除购物车项 |

### 订单接口
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/order/create` | 创建订单 |
| GET | `/api/order/list` | 获取订单列表 |

## 📱 页面结构

```
├── /           # 首页 - 书籍推荐
├── /books      # 书籍列表页
├── /book/{id}  # 书籍详情页
├── /publish    # 发布书籍页
├── /mybooks    # 我的书籍页
├── /cart       # 购物车页
├── /orders     # 订单列表页
├── /login      # 登录页
├── /register   # 注册页
└── /profile    # 个人中心页
```

## 📝 开发说明

### 代码规范

- Java代码遵循阿里巴巴Java开发规范
- TypeScript代码使用ESLint检查
- 提交信息使用规范格式：`feat: 添加功能`、`fix: 修复bug`、`docs: 更新文档`

### 部署说明

**生产环境打包：**

```bash
# 前端打包
cd frontend
npm run build

# 后端打包
cd backend
mvn clean package
```

**运行打包后的Jar：**

```bash
java -jar target/bookshare-1.0.0.jar
```

## 📄 许可证

MIT License

## 🤝 贡献

欢迎提交Issue和Pull Request！

---

*项目完成于 2026年*
