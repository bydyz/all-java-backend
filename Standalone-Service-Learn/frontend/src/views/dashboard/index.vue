<template>
  <div class="page-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用 RBAC 权限管理系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <p>这是一个基于 Spring Boot + Vue3 的角色权限管理系统。</p>
        <p>功能包括：</p>
        <ul>
          <li>用户管理 - 用户的增删改查、状态管理</li>
          <li>角色管理 - 角色的增删改查</li>
          <li>菜单管理 - 菜单的增删改查</li>
          <li>权限管理 - 基于角色的菜单权限分配</li>
        </ul>
        <p class="tip">请从左侧菜单选择功能进行操作。</p>
      </div>
    </el-card>
  </div>
</template>

<script setup lang="ts">
</script>

<style lang="scss" scoped>
.welcome-card {
  .card-header {
    font-size: 18px;
    font-weight: bold;
  }
  
  .welcome-content {
    p {
      margin: 10px 0;
      line-height: 1.8;
    }
    
    ul {
      padding-left: 20px;
      
      li {
        line-height: 2;
        color: #666;
      }
    }
    
    .tip {
      margin-top: 20px;
      color: #409eff;
    }
  }
}
</style>
