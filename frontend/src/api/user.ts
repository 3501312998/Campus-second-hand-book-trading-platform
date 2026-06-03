import request from '@/utils/request'
import type { LoginForm, RegisterForm } from '@/types'

export const login = (data: LoginForm) => {
  return request({
    url: '/user/login',
    method: 'POST',
    data
  })
}

export const register = (data: RegisterForm) => {
  return request({
    url: '/user/register',
    method: 'POST',
    data
  })
}

export const getUserInfo = () => {
  return request({
    url: '/user/info',
    method: 'GET'
  })
}

export const updateUserInfo = (data: any) => {
  return request({
    url: '/user/update',
    method: 'PUT',
    data
  })
}

export const changePassword = (oldPassword: string, newPassword: string) => {
  return request({
    url: '/user/password',
    method: 'PUT',
    params: { oldPassword, newPassword }
  })
}
