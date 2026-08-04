<template>
  <div class="page-404">
    <div class="content">
      <h1>404</h1>
      <p>抱歉，您访问的页面不存在</p>
      <el-button type="primary" @click="goHome">返回首页</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'

const router = useRouter()

const goHome = () => {
  router.push('/')
}
</script>

<style lang="scss" scoped>
.page-404 {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;

  .content {
    text-align: center;

    h1 {
      font-size: 120px;
      font-weight: bold;
      color: #409eff;
      margin: 0;
    }

    p {
      font-size: 18px;
      color: #666;
      margin: 20px 0 30px;
    }
  }
}
</style>
