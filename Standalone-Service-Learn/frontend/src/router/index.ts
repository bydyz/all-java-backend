import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import { useUserStore } from '@/store/user'
import { getToken } from '@/utils/auth'
import staticRoutes from './staticRoutes'

// 创建路由实例
const router = createRouter({
  history: createWebHistory(),
  routes: staticRoutes,
  scrollBehavior: () => ({ top: 0 })
})

// 路由守卫
const whiteList = ['/login', '/404']

router.beforeEach(async (to, _from, next) => {
  const token = getToken()
  
  if (token) {
    if (to.path === '/login') {
      next({ path: '/' })
    } else {
      const userStore = useUserStore()
      
      if (userStore.addRoutes.length === 0) {
        try {
          // 获取用户信息
          await userStore.getUserInfo()
          // 获取动态路由并添加
          const accessRoutes = await userStore.generateRoutes()
          accessRoutes.forEach((route: RouteRecordRaw) => {
            router.addRoute(route)
          })
          // 确保路由已添加
          next({ ...to, replace: true })
        } catch (error) {
          // 获取用户信息失败，清除 token 并跳转登录页
          userStore.resetState()
          next(`/login?redirect=${to.path}`)
        }
      } else {
        next()
      }
    }
  } else {
    if (whiteList.includes(to.path)) {
      next()
    } else {
      next(`/login?redirect=${to.path}`)
    }
  }
})

export default router
