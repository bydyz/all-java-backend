import type { Directive, DirectiveBinding } from 'vue'
import { useUserStore } from '@/store/user'

/**
 * 权限指令
 * 用法: v-permission="'user:add'"
 */
export const permissionDirective: Directive = {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    const { value } = binding
    
    if (value) {
      const userStore = useUserStore()
      const permissions = userStore.permissions
      
      if (value instanceof Array) {
        // 支持数组形式: v-permission="['user:add', 'user:edit']"
        const hasPermission = value.some((permission: string) => {
          return permissions.includes(permission)
        })
        
        if (!hasPermission) {
          el.parentNode?.removeChild(el)
        }
      } else {
        // 单个权限: v-permission="'user:add'"
        if (!permissions.includes(value)) {
          el.parentNode?.removeChild(el)
        }
      }
    }
  }
}
