-- =========================================
-- 校园二手书交易平台数据库初始化脚本
-- 数据库版本：MySQL 5.7
-- 编码：UTF-8 MB4
-- 创建时间：2024-01-01
-- 作者：课程设计生成器
-- =========================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS bookshare 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE bookshare;

-- =========================================
-- 1. 用户表（普通用户）
-- =========================================
DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID，主键自增',
    username VARCHAR(50) NOT NULL COMMENT '用户名，用于登录',
    student_id VARCHAR(20) NOT NULL COMMENT '学号，唯一标识',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) NOT NULL COMMENT '手机号码',
    email VARCHAR(100) COMMENT '电子邮箱',
    password VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密存储）',
    avatar VARCHAR(500) COMMENT '头像图片URL',
    gender TINYINT DEFAULT 0 COMMENT '性别：0未知，1男，2女',
    major VARCHAR(100) COMMENT '专业',
    grade VARCHAR(20) COMMENT '年级',
    address VARCHAR(200) COMMENT '收货地址',
    status TINYINT DEFAULT 1 COMMENT '账号状态：0禁用，1正常',
    role TINYINT DEFAULT 0 COMMENT '角色：0普通用户，1管理员',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_student_id (student_id),
    UNIQUE KEY uk_username (username),
    KEY idx_phone (phone),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表，存储普通用户信息';

-- =========================================
-- 2. 管理员表
-- =========================================
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID，主键自增',
    username VARCHAR(50) NOT NULL COMMENT '管理员用户名',
    password VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密存储）',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) COMMENT '手机号码',
    email VARCHAR(100) COMMENT '电子邮箱',
    avatar VARCHAR(500) COMMENT '头像图片URL',
    role VARCHAR(20) DEFAULT 'admin' COMMENT '角色标识：admin-超级管理员，operator-操作员',
    status TINYINT DEFAULT 1 COMMENT '账号状态：0禁用，1正常',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统管理员表';

-- =========================================
-- 3. 书籍分类表
-- =========================================
DROP TABLE IF EXISTS t_category;
CREATE TABLE t_category (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID，主键自增',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    level TINYINT DEFAULT 1 COMMENT '分类层级',
    sort INT DEFAULT 0 COMMENT '排序号，数字越小越靠前',
    icon VARCHAR(200) COMMENT '分类图标URL',
    description VARCHAR(500) COMMENT '分类描述',
    status TINYINT DEFAULT 1 COMMENT '状态：0禁用，1启用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    KEY idx_parent_id (parent_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍分类表';

-- =========================================
-- 4. 书籍表
-- =========================================
DROP TABLE IF EXISTS t_book;
CREATE TABLE t_book (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '书籍ID，主键自增',
    title VARCHAR(200) NOT NULL COMMENT '书名',
    author VARCHAR(100) COMMENT '作者',
    publisher VARCHAR(100) COMMENT '出版社',
    publish_date DATE COMMENT '出版日期',
    isbn VARCHAR(50) COMMENT 'ISBN国际标准书号',
    price DECIMAL(10,2) NOT NULL COMMENT '售价',
    original_price DECIMAL(10,2) COMMENT '原价',
    `condition` INT NOT NULL COMMENT '成色：1-全新，2-九成新，3-八成新，4-七成新，5-六成新及以下',
    category_id BIGINT COMMENT '所属分类ID',
    user_id BIGINT NOT NULL COMMENT '发布者用户ID',
    description TEXT COMMENT '书籍详细描述',
    images VARCHAR(2000) COMMENT '书籍图片URL，多个用逗号分隔',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    favorite_count INT DEFAULT 0 COMMENT '收藏次数',
    contact_qq VARCHAR(20) COMMENT '联系人QQ',
    contact_wechat VARCHAR(50) COMMENT '联系人微信',
    status TINYINT DEFAULT 0 COMMENT '状态：0待审核，1已上架，2已下架，3审核拒绝',
    reject_reason VARCHAR(500) COMMENT '审核拒绝原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    KEY idx_title (title),
    KEY idx_author (author),
    KEY idx_category_id (category_id),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time),
    FULLTEXT KEY ft_search (title, author, description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表，存储二手书信息';

-- =========================================
-- 5. 购物车表
-- =========================================
DROP TABLE IF EXISTS t_cart;
CREATE TABLE t_cart (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '购物车ID，主键自增',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '书籍ID',
    quantity INT DEFAULT 1 COMMENT '数量',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入购物车时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_book (user_id, book_id),
    KEY idx_user_id (user_id),
    KEY idx_book_id (book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- =========================================
-- 6. 订单表
-- =========================================
DROP TABLE IF EXISTS t_order;
CREATE TABLE t_order (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID，主键自增',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号，唯一',
    user_id BIGINT NOT NULL COMMENT '买家用户ID',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '订单总金额',
    actual_amount DECIMAL(10,2) COMMENT '实际支付金额',
    status TINYINT DEFAULT 0 COMMENT '订单状态：0待支付，1待发货，2待收货，3已完成，4已取消，5已退款',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收货地址',
    remark VARCHAR(500) COMMENT '订单备注',
    pay_time DATETIME COMMENT '支付时间',
    ship_time DATETIME COMMENT '发货时间',
    receive_time DATETIME COMMENT '收货时间',
    cancel_time DATETIME COMMENT '取消时间',
    cancel_reason VARCHAR(500) COMMENT '取消原因',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_user_id (user_id),
    KEY idx_status (status),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- =========================================
-- 7. 订单明细表
-- =========================================
DROP TABLE IF EXISTS t_order_item;
CREATE TABLE t_order_item (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单明细ID，主键自增',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    book_id BIGINT NOT NULL COMMENT '书籍ID',
    seller_id BIGINT NOT NULL COMMENT '卖家用户ID',
    title VARCHAR(200) NOT NULL COMMENT '书名（冗余字段，防止书籍信息变更）',
    author VARCHAR(100) COMMENT '作者（冗余字段）',
    price DECIMAL(10,2) NOT NULL COMMENT '购买时的单价',
    quantity INT DEFAULT 1 COMMENT '购买数量',
    subtotal DECIMAL(10,2) NOT NULL COMMENT '小计金额',
    book_image VARCHAR(500) COMMENT '书籍图片（冗余字段）',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id),
    KEY idx_book_id (book_id),
    KEY idx_seller_id (seller_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单明细表';

-- =========================================
-- 8. 收藏表
-- =========================================
DROP TABLE IF EXISTS t_favorite;
CREATE TABLE t_favorite (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID，主键自增',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    book_id BIGINT NOT NULL COMMENT '书籍ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    UNIQUE KEY uk_user_book (user_id, book_id),
    KEY idx_user_id (user_id),
    KEY idx_book_id (book_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- =========================================
-- 9. 消息表（用于用户间沟通）
-- =========================================
DROP TABLE IF EXISTS t_message;
CREATE TABLE t_message (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID，主键自增',
    from_user_id BIGINT NOT NULL COMMENT '发送者用户ID',
    to_user_id BIGINT NOT NULL COMMENT '接收者用户ID',
    book_id BIGINT COMMENT '关联的书籍ID',
    content TEXT NOT NULL COMMENT '消息内容',
    type TINYINT DEFAULT 0 COMMENT '消息类型：0文本，1系统消息',
    is_read TINYINT DEFAULT 0 COMMENT '是否已读：0未读，1已读',
    read_time DATETIME COMMENT '阅读时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标记：0未删除，1已删除',
    PRIMARY KEY (id),
    KEY idx_from_user_id (from_user_id),
    KEY idx_to_user_id (to_user_id),
    KEY idx_book_id (book_id),
    KEY idx_is_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息表';

-- =========================================
-- 10. 操作日志表
-- =========================================
DROP TABLE IF EXISTS t_operation_log;
CREATE TABLE t_operation_log (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID，主键自增',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '操作用户名',
    operation VARCHAR(100) NOT NULL COMMENT '操作描述',
    method VARCHAR(200) COMMENT '请求方法',
    url VARCHAR(500) COMMENT '请求URL',
    ip VARCHAR(50) COMMENT 'IP地址',
    params TEXT COMMENT '请求参数',
    result TEXT COMMENT '操作结果',
    status TINYINT COMMENT '操作状态：0失败，1成功',
    error_msg TEXT COMMENT '错误信息',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    PRIMARY KEY (id),
    KEY idx_user_id (user_id),
    KEY idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- =========================================
-- 初始化数据
-- =========================================

-- 插入默认管理员账号（密码：123456，BCrypt加密）
INSERT INTO sys_user (username, password, real_name, phone, role, status) VALUES
('admin', '$2a$10$EixZaYVK1sBWybMU914IJOK.VbDCBe8lC7S3h6N6OH5Lk8d1KKzGe', 'admin', '13800138000', 'admin', 1);

-- 插入书籍分类
INSERT INTO t_category (name, parent_id, level, sort, description, status) VALUES
('教材教辅', 0, 1, 1, '大学教材、考试辅导书等', 1),
('专业书籍', 0, 1, 2, '计算机、经济、管理等专业书籍', 1),
('文学小说', 0, 1, 3, '各类文学、小说、散文等', 1),
('考试考证', 0, 1, 4, '各类职业资格证书、考试用书', 1),
('工具词典', 0, 1, 5, '字典、词典、工具书等', 1),
('其他', 0, 1, 6, '其他类型书籍', 1);

-- 插入教材教辅子分类
INSERT INTO t_category (name, parent_id, level, sort, description, status) VALUES
('高等数学', 1, 2, 1, '高等数学、线性代数等', 1),
('大学英语', 1, 2, 2, '大学英语、听说读写', 1),
('大学物理', 1, 2, 3, '大学物理及相关实验', 1),
('专业基础课', 1, 2, 4, '各专业基础课程教材', 1);

-- 插入专业书籍子分类
INSERT INTO t_category (name, parent_id, level, sort, description, status) VALUES
('计算机', 2, 2, 1, '编程语言、算法、数据库等', 1),
('经济学', 2, 2, 2, '经济学、金融、会计等', 1),
('管理学', 2, 2, 3, '企业管理、人力资源等', 1),
('法学', 2, 2, 4, '法律、法规等', 1);

-- 插入考试考证子分类
INSERT INTO t_category (name, parent_id, level, sort, description, status) VALUES
('考研', 4, 2, 1, '考研相关书籍', 1),
('公务员', 4, 2, 2, '公务员考试用书', 1),
('职业资格', 4, 2, 3, '各类职业资格证书', 1),
('四六级', 4, 2, 4, '英语四六级考试', 1);

-- =========================================
-- 创建测试用户（密码：123456，BCrypt加密）
-- =========================================
INSERT INTO t_user (username, student_id, real_name, phone, password, major, grade, status) VALUES
('test001', '2021001001', 'test001', '13900001001', '$2a$10$EixZaYVK1sBWybMU914IJOK.VbDCBe8lC7S3h6N6OH5Lk8d1KKzGe', '计算机科学与技术', '2021级', 1),
('test002', '2021001002', 'test002', '13900001002', '$2a$10$EixZaYVK1sBWybMU914IJOK.VbDCBe8lC7S3h6N6OH5Lk8d1KKzGe', '软件工程', '2021级', 1),
('test003', '2021001003', 'test003', '13900001003', '$2a$10$EixZaYVK1sBWybMU914IJOK.VbDCBe8lC7S3h6N6OH5Lk8d1KKzGe', '信息安全', '2021级', 1);

-- =========================================
-- 创建测试书籍数据
-- =========================================
INSERT INTO t_book (title, author, publisher, isbn, price, original_price, `condition`, category_id, user_id, description, images, status) VALUES
('深入理解计算机系统', 'Randal E. Bryant', '机械工业出版社', '978-7-111-54493-7', 68.00, 139.00, 2, 6, 1, '计算机专业经典教材，系统学习计算机底层原理，书中只有少量笔记。', "https://picsum.photos/seed/book1/400/600", 1),
('算法导论（第3版）', 'Thomas H. Cormen', '电子工业出版社', '978-7-121-17955-0', 55.00, 128.00, 3, 6, 1, '算法领域经典著作，研究生必读，内容全面详实。', "https://picsum.photos/seed/book2/400/600", 1),
('Java核心技术卷I', 'Cay S. Horstmann', '机械工业出版社', '978-7-111-54742-6', 45.00, 119.00, 2, 6, 2, 'Java入门经典书籍，适合初学者，附带随书代码。', "https://picsum.photos/seed/book3/400/600", 1),
('高等数学（同济七版）', '同济大学数学系', '高等教育出版社', '978-7-04-039663-8', 25.00, 47.00, 4, 7, 2, '大学高等数学教材，包含上下册，少量习题痕迹。', "https://picsum.photos/seed/book4/400/600", 1),
('大学英语精读（第三版）', '翟象俊', '上海外语教育出版社', '978-7-5446-5000-5', 18.00, 45.00, 3, 8, 3, '大学英语精读教材1-3册全套，保存完好。', "https://picsum.photos/seed/book5/400/600", 1),
('考研数学复习全书', '李永乐', '国家开放大学出版社', '978-7-304-09012-7', 35.00, 89.00, 2, 13, 3, '考研数学必备复习资料，内容完整，习题基本未做。', "https://picsum.photos/seed/book6/400/600", 1),
('Python编程：从入门到实践', 'Eric Matthes', '人民邮电出版社', '978-7-115-42802-4', 40.00, 89.00, 5, 6, 1, 'Python入门书籍，塑封未拆，全新正版。', "https://picsum.photos/seed/book7/400/600", 1);

-- =========================================
-- 创建测试购物车数据
-- =========================================
INSERT INTO t_cart (user_id, book_id, quantity) VALUES
(2, 1, 1),
(2, 3, 1),
(3, 4, 1);

-- =========================================
-- 创建测试订单数据
-- =========================================
INSERT INTO t_order (order_no, user_id, total_amount, status, receiver_name, receiver_phone, receiver_address) VALUES
('ORD202401150001', 2, 113.00, 2, 'test002', '13900001002', '北京市海淀区中关村大街1号'),
('ORD202401150002', 3, 25.00, 1, 'test003', '13900001003', '上海市浦东新区张江高科技园区');

-- =========================================
-- 创建测试订单明细
-- =========================================
INSERT INTO t_order_item (order_id, book_id, seller_id, title, author, price, quantity, subtotal) VALUES
(1, 1, 1, '深入理解计算机系统', 'Randal E. Bryant', 68.00, 1, 68.00),
(1, 3, 1, 'Java核心技术卷I', 'Cay S. Horstmann', 45.00, 1, 45.00),
(2, 4, 2, '高等数学（同济七版）', '同济大学数学系', 25.00, 1, 25.00);

-- =========================================
-- 创建测试收藏数据
-- =========================================
INSERT INTO t_favorite (user_id, book_id) VALUES
(2, 2),
(2, 6),
(3, 1);

-- =========================================
-- 数据库设计说明完成
-- =========================================
