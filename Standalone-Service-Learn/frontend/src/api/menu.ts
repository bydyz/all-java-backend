import request from '@/utils/request'

export interface MenuData {
  id: number
  menuName: string
  parentId: number
  path: string
  component: string
  redirect: string
  icon: string
  title: string
  hidden: number
  keepAlive: number
  permission: string
  type: string
  sort: number
  status: number
  children?: MenuData[]
}

export interface MenuCreateParams {
  menuName: string
  parentId: number
  path?: string
  component?: string
  redirect?: string
  icon?: string
  title?: string
  hidden?: number
  keepAlive?: number
  permission?: string
  type: string
  sort?: number
  status?: number
}

export interface MenuUpdateParams {
  menuName?: string
  parentId?: number
  path?: string
  component?: string
  redirect?: string
  icon?: string
  title?: string
  hidden?: number
  keepAlive?: number
  permission?: string
  type?: string
  sort?: number
  status?: number
}

// 获取菜单树
export function getMenuTree(type?: string) {
  return request.get<any, { code: number; data: MenuData[] }>('/menus/tree', { params: { type } })
}

// 获取菜单详情
export function getMenuById(id: number) {
  return request.get<any, { code: number; data: MenuData }>(`/menus/${id}`)
}

// 创建菜单
export function createMenu(data: MenuCreateParams) {
  return request.post('/menus', data)
}

// 更新菜单
export function updateMenu(id: number, data: MenuUpdateParams) {
  return request.put(`/menus/${id}`, data)
}

// 删除菜单
export function deleteMenu(id: number) {
  return request.delete(`/menus/${id}`)
}
