import request from '@/utils/request'

export const getBookList = (params: any) => {
  return request({
    url: '/book/list',
    method: 'GET',
    params
  })
}

export const getBookDetail = (id: number) => {
  return request({
    url: `/book/detail/${id}`,
    method: 'GET'
  })
}

export const publishBook = (data: any) => {
  return request({
    url: '/book/publish',
    method: 'POST',
    data
  })
}

export const editBook = (id: number, data: any) => {
  return request({
    url: `/book/edit/${id}`,
    method: 'PUT',
    data
  })
}

export const deleteBook = (id: number) => {
  return request({
    url: `/book/delete/${id}`,
    method: 'DELETE'
  })
}

export const getMyBooks = (params: any) => {
  return request({
    url: '/book/my-books',
    method: 'GET',
    params
  })
}

export const getHotBooks = (limit?: number) => {
  return request({
    url: '/book/hot',
    method: 'GET',
    params: { limit }
  })
}

export const getLatestBooks = (limit?: number) => {
  return request({
    url: '/book/latest',
    method: 'GET',
    params: { limit }
  })
}
