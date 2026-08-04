<template>
  <div class="page-container">
    <!-- 操作按钮 -->
    <div class="table-container">
      <div class="button-group" style="margin-bottom: 15px;">
        <el-button v-permission="'menu:add'" type="primary" @click="handleAdd">新增菜单</el-button>
      </div>
      
      <!-- 数据表格 -->
      <el-table v-loading="loading" :data="tableData" border stripe row-key="id" :tree-props="{ children: 'children' }">
        <el-table-column prop="menuName" label="菜单名称" min-width="200" />
        <el-table-column prop="icon" label="图标" width="100">
          <template #default="{ row }">
            <el-icon v-if="row.icon"><component :is="row.icon" /></el-icon>
          </template>
        </el-table-column>
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column prop="type" label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.type === 'D' ? '' : row.type === 'M' ? 'success' : 'warning'">
              {{ row.type === 'D' ? '目录' : row.type === 'M' ? '菜单' : '按钮' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="permission" label="权限标识" width="150" />
        <el-table-column prop="path" label="路由路径" width="150" />
        <el-table-column prop="component" label="组件路径" width="180" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button v-permission="'menu:edit'" type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button v-permission="'menu:add'" type="success" link @click="handleAddChild(row)">新增</el-button>
            <el-button v-permission="'menu:delete'" type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 新增/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form ref="formRef" :model="form" :rules="formRules" label-width="100px">
        <el-form-item label="上级菜单" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="menuTreeData"
            :props="{ label: 'menuName', children: 'children', value: 'id' }"
            check-strictly
            :render-after-expand="false"
            placeholder="请选择上级菜单"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="form.type" @change="handleTypeChange">
            <el-radio value="D">目录</el-radio>
            <el-radio value="M">菜单</el-radio>
            <el-radio value="B">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="menuName">
          <el-input v-model="form.menuName" placeholder="请输入菜单名称" />
        </el-form-item>
        <el-form-item v-if="form.type !== 'B'" label="显示标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入显示标题" />
        </el-form-item>
        <el-form-item v-if="form.type !== 'B'" label="路由路径" prop="path">
          <el-input v-model="form.path" placeholder="请输入路由路径" />
        </el-form-item>
        <el-form-item v-if="form.type === 'M'" label="组件路径" prop="component">
          <el-input v-model="form.component" placeholder="请输入组件路径，如 system/user/index" />
        </el-form-item>
        <el-form-item v-if="form.type !== 'B'" label="图标" prop="icon">
          <el-input v-model="form.icon" placeholder="请输入图标名称" />
        </el-form-item>
        <el-form-item v-if="form.type === 'B'" label="权限标识" prop="permission">
          <el-input v-model="form.permission" placeholder="请输入权限标识，如 user:add" />
        </el-form-item>
        <el-form-item v-if="form.type !== 'B'" label="是否隐藏" prop="hidden">
          <el-radio-group v-model="form.hidden">
            <el-radio :value="0">显示</el-radio>
            <el-radio :value="1">隐藏</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" :max="999" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :value="1">启用</el-radio>
            <el-radio :value="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { getMenuTree, createMenu, updateMenu, deleteMenu } from '@/api/menu'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref<any[]>([])
const dialogVisible = ref(false)
const dialogType = ref<'add' | 'addChild' | 'edit'>('add')
const formRef = ref<FormInstance>()
const menuTreeData = ref<any[]>([])

const dialogTitle = computed(() => {
  switch (dialogType.value) {
    case 'add': return '新增菜单'
    case 'addChild': return '新增子菜单'
    case 'edit': return '编辑菜单'
    default: return '菜单'
  }
})

const form = reactive({
  id: undefined as number | undefined,
  parentId: 0,
  menuName: '',
  title: '',
  path: '',
  component: '',
  redirect: '',
  icon: '',
  permission: '',
  type: 'M',
  hidden: 0,
  keepAlive: 0,
  sort: 0,
  status: 1
})

const formRules: FormRules = {
  menuName: [{ required: true, message: '请输入菜单名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择菜单类型', trigger: 'change' }]
}

// 获取表格数据
const fetchData = async () => {
  loading.value = true
  try {
    const res = await getMenuTree()
    tableData.value = res.data
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 获取菜单树（用于选择上级菜单）
const fetchMenuTree = async () => {
  try {
    const res = await getMenuTree('D')
    menuTreeData.value = [{ id: 0, menuName: '顶级菜单', children: res.data }]
  } catch (error) {
    console.error(error)
  }
}

// 类型变更
const handleTypeChange = () => {
  if (form.type === 'D') {
    form.component = 'Layout'
  } else if (form.type === 'B') {
    form.component = ''
    form.path = ''
  }
}

// 新增
const handleAdd = () => {
  dialogType.value = 'add'
  resetForm()
  dialogVisible.value = true
}

// 新增子菜单
const handleAddChild = (row: any) => {
  dialogType.value = 'addChild'
  resetForm()
  form.parentId = row.id
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row: any) => {
  dialogType.value = 'edit'
  form.id = row.id
  form.parentId = row.parentId
  form.menuName = row.menuName
  form.title = row.title
  form.path = row.path
  form.component = row.component
  form.redirect = row.redirect
  form.icon = row.icon
  form.permission = row.permission
  form.type = row.type
  form.hidden = row.hidden
  form.keepAlive = row.keepAlive
  form.sort = row.sort
  form.status = row.status
  dialogVisible.value = true
}

// 删除
const handleDelete = async (row: any) => {
  try {
    await ElMessageBox.confirm('确认要删除该菜单吗？删除后子菜单也会被删除', '提示', {
      type: 'warning'
    })
    await deleteMenu(row.id)
    ElMessage.success('删除成功')
    fetchData()
  } catch (error) {
    console.error(error)
  }
}

// 提交表单
const handleSubmit = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return
  
  submitLoading.value = true
  try {
    if (dialogType.value === 'edit') {
      await updateMenu(form.id!, form)
      ElMessage.success('更新成功')
    } else {
      await createMenu(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    fetchData()
  } catch (error) {
    console.error(error)
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  form.id = undefined
  form.parentId = 0
  form.menuName = ''
  form.title = ''
  form.path = ''
  form.component = ''
  form.redirect = ''
  form.icon = ''
  form.permission = ''
  form.type = 'M'
  form.hidden = 0
  form.keepAlive = 0
  form.sort = 0
  form.status = 1
}

onMounted(() => {
  fetchData()
  fetchMenuTree()
})
</script>
