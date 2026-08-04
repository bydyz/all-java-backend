import request from '@/utils/request'

export interface UserInfo {
  id: number
  username: string
  nickname: string
  avatar: string
  roles: string[]
  permissions: string[]
}

// 获取当前用户信息
export function getUserInfo() {
  return request.get<any, { code: number; data: UserInfo }>('/user/info')
}

// 获取当前用户有权限的菜单树
export function getUserMenus() {
  return request.get<any, { code: number; data: any[] }>('/user/menus')
}

// 获取当前用户权限标识列表
export function getUserPermissions() {
  return request.get<any, { code: number; data: string[] }>('/user/permissions')
}
