import request from '@/utils/request'

/**
 * 获取所有分类
 */
export function getCategoryList() {
  return request({
    url: '/category/list',
    method: 'get'
  })
}

/**
 * 根据父分类ID获取子分类
 */
export function getCategoryByParentId(parentId: number) {
  return request({
    url: `/category/${parentId}/children`,
    method: 'get'
  })
}
