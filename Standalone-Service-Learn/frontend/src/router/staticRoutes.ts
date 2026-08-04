import type { RouteRecordRaw } from 'vue-router'

// 静态路由（仅登录和404，其他路由由后端动态返回）
const staticRoutes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/index.vue'),
    meta: {
      title: '登录',
      hidden: true
    }
  },
  {
    path: '/404',
    name: 'NotFound',
    component: () => import('@/views/error/404.vue'),
    meta: {
      title: '404',
      hidden: true
    }
  },
  {
    path: '/',
    redirect: '/dashboard'
  }
]

export default staticRoutes
