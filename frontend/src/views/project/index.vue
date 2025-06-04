<template>
  <div class="project-container">
    <!-- 搜索栏 -->
    <div class="search-bar">
      <el-input
        v-model="queryParams.keyword"
        placeholder="请输入项目名称或编码"
        clearable
        @keyup.enter="handleQuery"
        style="width: 200px"
      >
        <template #append>
          <el-button @click="handleQuery">
            <el-icon><Search /></el-icon>
          </el-button>
        </template>
      </el-input>
      <el-button type="primary" @click="handleAdd" v-permission="'project:create'">
        <el-icon><Plus /></el-icon>新增
      </el-button>
    </div>

    <!-- 项目列表 -->
    <el-table :data="projectList" v-loading="loading" border>
      <el-table-column prop="code" label="项目编码" width="120" />
      <el-table-column prop="name" label="项目名称" width="200" />
      <el-table-column prop="address" label="项目地址" show-overflow-tooltip />
      <el-table-column prop="description" label="项目描述" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{row}">
          <el-switch
            v-model="row.status"
            :active-value="1"
            :inactive-value="0"
            @change="handleStatusChange(row)"
            v-permission="'project:update'"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{row}">
          <el-button
            link
            type="primary"
            @click="handleEdit(row)"
            v-permission="'project:update'"
          >编辑</el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(row)"
            v-permission="'project:delete'"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <div class="pagination">
      <el-pagination
        v-model:current-page="queryParams.pageNum"
        v-model:page-size="queryParams.pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 项目表单对话框 -->
    <el-dialog
      :title="dialogTitle"
      v-model="dialogVisible"
      width="500px"
      append-to-body
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="80px"
      >
        <el-form-item label="项目编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入项目编码" />
        </el-form-item>
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入项目地址" />
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            placeholder="请输入项目描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import { getProjectList, createProject, updateProject, deleteProject, toggleProjectStatus } from '@/api/project'

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  keyword: ''
})

// 项目列表数据
const loading = ref(false)
const projectList = ref([])
const total = ref(0)

// 表单相关
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()
const form = reactive({
  id: undefined,
  code: '',
  name: '',
  address: '',
  description: ''
})

// 表单校验规则
const rules = {
  code: [{ required: true, message: '请输入项目编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  address: [{ required: true, message: '请输入项目地址', trigger: 'blur' }]
}

// 获取项目列表
const getList = async () => {
  loading.value = true
  try {
    const { data } = await getProjectList(queryParams)
    projectList.value = data.records
    total.value = data.total
  } finally {
    loading.value = false
  }
}

// 查询操作
const handleQuery = () => {
  queryParams.pageNum = 1
  getList()
}

// 重置表单
const resetForm = () => {
  form.id = undefined
  form.code = ''
  form.name = ''
  form.address = ''
  form.description = ''
  formRef.value?.resetFields()
}

// 新增操作
const handleAdd = () => {
  resetForm()
  dialogTitle.value = '新增项目'
  dialogVisible.value = true
}

// 编辑操作
const handleEdit = (row) => {
  Object.assign(form, row)
  dialogTitle.value = '编辑项目'
  dialogVisible.value = true
}

// 提交表单
const handleSubmit = async () => {
  await formRef.value.validate()
  try {
    if (form.id) {
      await updateProject(form)
      ElMessage.success('修改成功')
    } else {
      await createProject(form)
      ElMessage.success('新增成功')
    }
    dialogVisible.value = false
    getList()
  } catch (error) {
    ElMessage.error(error.message)
  }
}

// 删除操作
const handleDelete = (row) => {
  ElMessageBox.confirm('确认删除该项目吗？', '警告', {
    type: 'warning'
  }).then(async () => {
    await deleteProject(row.id)
    ElMessage.success('删除成功')
    getList()
  }).catch(() => {})
}

// 状态切换
const handleStatusChange = async (row) => {
  try {
    await toggleProjectStatus(row.id, row.status)
    ElMessage.success('状态修改成功')
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
    ElMessage.error(error.message)
  }
}

// 分页操作
const handleSizeChange = (val) => {
  queryParams.pageSize = val
  getList()
}

const handleCurrentChange = (val) => {
  queryParams.pageNum = val
  getList()
}

// 初始化
onMounted(() => {
  getList()
})
</script>

<style scoped>
.project-container {
  padding: 20px;
}

.search-bar {
  margin-bottom: 20px;
  display: flex;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>