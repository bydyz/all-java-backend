import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { RouteRecordRaw } from 'vue-router'
import { login, logout } from '@/api/auth'
import { getUserMenus, getUserInfo as fetchUserInfo } from '@/api/permission'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router from '@/router'

// 动态导入所有 Vue 组件
const modules = import.meta.glob('@/views/**/*.vue')

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref<string>(getToken() || '')
  const userInfo = ref<any>(null)
  const roles = ref<string[]>([])
  const permissions = ref<string[]>([])
  const menus = ref<any[]>([])
  const addRoutes = ref<RouteRecordRaw[]>([])

  // 登录
  async function loginAction(username: string, password: string) {
    const res = await login({ username, password })
    const data = res.data
    token.value = data.token
    userInfo.value = data.userInfo
    roles.value = data.userInfo.roles
    permissions.value = data.userInfo.permissions
    setToken(data.token)
    return data
  }

  // 登出
  async function logoutAction() {
    try {
      await logout()
    } finally {
      resetState()
    }
  }

  // 获取用户信息
  async function getUserInfo() {
    const res = await fetchUserInfo()
    const data = res.data
    userInfo.value = data
    roles.value = data.roles
    permissions.value = data.permissions
    return data
  }

  // 生成动态路由
  async function generateRoutes(): Promise<RouteRecordRaw[]> {
    const res = await getUserMenus()
    const menuData = res.data
    
    // 保存菜单数据
    menus.value = menuData
    
    // 生成路由（后端已按sort排序）
    const routes = generateRoutesFromMenus(menuData)
    addRoutes.value = routes
    
    return routes
  }

  // 从菜单数据生成路由
  function generateRoutesFromMenus(menuData: any[], parentName?: string): RouteRecordRaw[] {
    const routes: RouteRecordRaw[] = []
    
    menuData.forEach((menu) => {
      // 修复路径：避免重复的 /
      const routePath = menu.parentId === 0 
        ? (menu.path.startsWith('/') ? menu.path : `/${menu.path}`)
        : menu.path
      
      // 生成唯一名称：子路由加上父级前缀，避免与祖先同名
      const routeName = parentName 
        ? `${parentName}_${menu.menuName}` 
        : menu.menuName
      
      const route: RouteRecordRaw = {
        path: routePath,
        name: routeName,
        component: menu.component === 'Layout' 
          ? () => import('@/views/layout/index.vue')
          : modules[`/src/views/${menu.component}.vue`] || (() => import('@/views/error/404.vue')),
        meta: {
          title: menu.title,
          icon: menu.icon,
          hidden: menu.hidden === 1,
          keepAlive: menu.keepAlive === 1
        },
        children: []
      }
      
      if (menu.children && menu.children.length > 0) {
        route.children = generateRoutesFromMenus(menu.children, routeName)
      }
      
      routes.push(route)
    })
    
    return routes
  }

  // 重置状态
  function resetState() {
    token.value = ''
    userInfo.value = null
    roles.value = []
    permissions.value = []
    menus.value = []
    addRoutes.value = []
    removeToken()
  }

  return {
    token,
    userInfo,
    roles,
    permissions,
    menus,
    addRoutes,
    loginAction,
    logoutAction,
    getUserInfo,
    generateRoutes,
    resetState
  }
})
