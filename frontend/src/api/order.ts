import request from '@/utils/request'

/**
 * 获取购物车列表
 */
export function getCartList() {
  return request({
    url: '/cart/list',
    method: 'get'
  })
}

/**
 * 添加到购物车
 */
export function addToCart(data: { bookId: number }) {
  return request({
    url: '/cart/add',
    method: 'post',
    data
  })
}

/**
 * 从购物车移除
 */
export function removeFromCart(cartId: number) {
  return request({
    url: `/cart/${cartId}`,
    method: 'delete'
  })
}

/**
 * 创建订单
 */
export function createOrder(data: { bookIds: number[] }) {
  return request({
    url: '/order/create',
    method: 'post',
    data
  })
}

/**
 * 获取订单列表
 */
export function getOrderList(params: any) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

/**
 * 获取订单详情
 */
export function getOrderDetail(orderId: number) {
  return request({
    url: `/order/${orderId}`,
    method: 'get'
  })
}

/**
 * 取消订单
 */
export function cancelOrder(orderId: number) {
  return request({
    url: `/order/${orderId}/cancel`,
    method: 'put'
  })
}

/**
 * 付款订单
 */
export function payOrder(orderId: number) {
  return request({
    url: `/order/${orderId}/pay`,
    method: 'put'
  })
}

/**
 * 确认收货
 */
export function confirmOrder(orderId: number) {
  return request({
    url: `/order/${orderId}/confirm`,
    method: 'put'
  })
}
