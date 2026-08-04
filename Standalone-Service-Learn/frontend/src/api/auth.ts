import request from '@/utils/request'

export interface LoginParams {
  username: string
  password: string
}

export interface LoginResult {
  token: string
  userInfo: {
    id: number
    username: string
    nickname: string
    avatar: string
    roles: string[]
    permissions: string[]
  }
}

export interface RegisterParams {
  username: string
  password: string
  nickname?: string
  email?: string
  phone?: string
}

// 登录
export function login(data: LoginParams) {
  return request.post<any, { code: number; data: LoginResult }>('/auth/login', data)
}

// 登出
export function logout() {
  return request.post('/auth/logout')
}

// 注册
export function register(data: RegisterParams) {
  return request.post('/auth/register', data)
}
