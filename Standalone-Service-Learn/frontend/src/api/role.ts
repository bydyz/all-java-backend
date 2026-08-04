import request from '@/utils/request'

export interface RoleParams {
  pageNum?: number
  pageSize?: number
  roleName?: string
  status?: number
}

export interface RoleData {
  id: number
  roleName: string
  roleKey: string
  description: string
  status: number
  createTime: string
  menuIds: number[]
}

export interface RoleCreateParams {
  roleName: string
  roleKey: string
  description?: string
  status?: number
  menuIds?: number[]
}

export interface RoleUpdateParams {
  roleName?: string
  description?: string
  status?: number
  menuIds?: number[]
}

// 获取角色分页列表
export function getRolePage(params: RoleParams) {
  return request.get<any, { code: number; data: { records: RoleData[]; total: number } }>('/roles', { params })
}

// 获取角色列表（用于下拉选择）
export function getRoleList() {
  return request.get<any, { code: number; data: RoleData[] }>('/roles/list')
}

// 获取角色详情
export function getRoleById(id: number) {
  return request.get<any, { code: number; data: RoleData }>(`/roles/${id}`)
}

// 创建角色
export function createRole(data: RoleCreateParams) {
  return request.post('/roles', data)
}

// 更新角色
export function updateRole(id: number, data: RoleUpdateParams) {
  return request.put(`/roles/${id}`, data)
}

// 删除角色
export function deleteRole(id: number) {
  return request.delete(`/roles/${id}`)
}

// 修改角色状态
export function updateRoleStatus(id: number, status: number) {
  return request.put(`/roles/${id}/status`, status)
}
