/**
 * 验证工具函数
 */

// 验证邮箱
export function isEmail(email: string): boolean {
  const reg = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return reg.test(email)
}

// 验证手机号
export function isPhone(phone: string): boolean {
  const reg = /^1[3-9]\d{9}$/
  return reg.test(phone)
}

// 验证用户名（字母、数字、下划线，3-20位）
export function isValidUsername(username: string): boolean {
  const reg = /^[a-zA-Z0-9_]{3,20}$/
  return reg.test(username)
}

// 验证密码（至少6位）
export function isValidPassword(password: string): boolean {
  return password.length >= 6
}
