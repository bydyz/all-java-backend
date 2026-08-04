import request from '@/utils/request'

export interface UserParams {
  pageNum?: number
  pageSize?: number
  username?: string
  status?: number
}

export interface UserData {
  id: number
  username: string
  nickname: string
  email: string
  phone: string
  avatar: string
  status: number
  createTime: string
  roles: RoleInfo[]
}

export interface RoleInfo {
  id: number
  roleName: string
  roleKey: string
}

export interface UserCreateParams {
  username: string
  password: string
  nickname?: string
  email?: string
  phone?: string
  status?: number
  roleIds?: number[]
}

export interface UserUpdateParams {
  nickname?: string
  email?: string
  phone?: string
  avatar?: string
  status?: number
  roleIds?: number[]
}

// 获取用户分页列表
export function getUserPage(params: UserParams) {
  return request.get<any, { code: number; data: { records: UserData[]; total: number } }>('/users', { params })
}

// 获取用户详情
export function getUserById(id: number) {
  return request.get<any, { code: number; data: UserData }>(`/users/${id}`)
}

// 创建用户
export function createUser(data: UserCreateParams) {
  return request.post('/users', data)
}

// 更新用户
export function updateUser(id: number, data: UserUpdateParams) {
  return request.put(`/users/${id}`, data)
}

// 删除用户
export function deleteUser(id: number) {
  return request.delete(`/users/${id}`)
}

// 修改用户状态
export function updateUserStatus(id: number, status: number) {
  return request.put(`/users/${id}/status`, status)
}

// 重置用户密码
export function resetPassword(id: number, password: string) {
  return request.put(`/users/${id}/password`, password)
}
