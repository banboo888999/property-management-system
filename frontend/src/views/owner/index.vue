<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true">
      <el-form-item label="业主姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入业主姓名" clearable />
      </el-form-item>
      <el-form-item label="手机号码" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入手机号码" clearable />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">搜索</el-button>
        <el-button @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          v-permission="['owner:create']"
          @click="handleAdd"
        >新增</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="ownerList">
      <el-table-column label="业主姓名" prop="name" />
      <el-table-column label="身份证号" prop="idCard" />
      <el-table-column label="手机号码" prop="phone" />
      <el-table-column label="邮箱" prop="email" />
      <el-table-column label="状态" align="center">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template #default="scope">
          <el-button
            type="text"
            v-permission="['owner:update']"
            @click="handleUpdate(scope.row)"
          >修改</el-button>
          <el-button
            type="text"
            v-permission="['owner:delete']"
            @click="handleDelete(scope.row)"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="业主姓名" prop="name">
          <el-input v-model="form.name" placeholder="请输入业主姓名" />
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="form.idCard" placeholder="请输入身份证号" />
        </el-form-item>
        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getOwnerPage, getOwnerDetail, createOwner, updateOwner, deleteOwner } from '@/api/owner'
import { useStore } from 'vuex'

const store = useStore()

// 遮罩层
const loading = ref(false)
// 总条数
const total = ref(0)
// 业主表格数据
const ownerList = ref([])
// 弹出层标题
const title = ref('')
// 是否显示弹出层
const open = ref(false)

// 查询参数
const queryParams = reactive({
  pageNum: 1,
  pageSize: 10,
  projectId: store.state.project.currentProjectId,
  name: undefined,
  phone: undefined
})

// 表单参数
const form = reactive({
  id: undefined,
  name: undefined,
  idCard: undefined,
  phone: undefined,
  email: undefined,
  status: 1,
  remark: undefined,
  projectId: store.state.project.currentProjectId
})

// 表单校验规则
const rules = {
  name: [{ required: true, message: '请输入业主姓名', trigger: 'blur' }],
  idCard: [{ required: true, message: '请输入身份证号', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入手机号码', trigger: 'blur' }],
  email: [{ type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }]
}

// 表单引用
const form = ref()

/** 查询业主列表 */
function getList() {
  loading.value = true
  getOwnerPage(queryParams).then(response => {
    ownerList.value = response.data.records
    total.value = response.data.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value.resetFields()
  form.id = undefined
  form.name = undefined
  form.idCard = undefined
  form.phone = undefined
  form.email = undefined
  form.status = 1
  form.remark = undefined
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  queryForm.value.resetFields()
  handleQuery()
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  title.value = '添加业主'
  open.value = true
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const id = row.id
  getOwnerDetail(id).then(response => {
    Object.assign(form, response.data)
    title.value = '修改业主'
    open.value = true
  })
}

/** 提交按钮 */
function submitForm() {
  form.value.validate(valid => {
    if (valid) {
      if (form.id != undefined) {
        updateOwner(form).then(response => {
          ElMessage.success('修改成功')
          open.value = false
          getList()
        })
      } else {
        createOwner(form).then(response => {
          ElMessage.success('新增成功')
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const id = row.id
  ElMessageBox.confirm('是否确认删除该业主数据项?', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(function() {
    return deleteOwner(id)
  }).then(() => {
    getList()
    ElMessage.success('删除成功')
  })
}

onMounted(() => {
  getList()
})
</script>