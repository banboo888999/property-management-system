<template>
  <!-- ... 其他模板内容保持不变 ... -->
  
  <!-- 操作按钮区域 -->
  <el-row :gutter="10" class="mb8">
    <el-col :span="1.5">
      <el-button
        type="primary"
        v-permission="['owner:create']"
        @click="handleAdd"
      >新增</el-button>
    </el-col>
    <el-col :span="1.5">
      <el-button
        type="success"
        v-permission="['owner:import']"
        @click="handleImport"
      >导入</el-button>
    </el-col>
    <el-col :span="1.5">
      <el-button
        type="warning"
        v-permission="['owner:export']"
        @click="handleExport"
      >导出</el-button>
    </el-col>
  </el-row>

  <!-- 导入对话框 -->
  <el-dialog title="导入业主信息" v-model="importOpen" width="400px" append-to-body>
    <el-upload
      class="upload-demo"
      :action="null"
      :auto-upload="false"
      :on-change="handleFileChange"
      :limit="1"
      accept=".xlsx, .xls"
    >
      <template #trigger>
        <el-button type="primary">选择文件</el-button>
      </template>
      <template #tip>
        <div class="el-upload__tip">
          请上传 xlsx 或 xls 格式的Excel文件
        </div>
      </template>
    </el-upload>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="primary" @click="submitImport">确 定</el-button>
        <el-button @click="importOpen = false">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup>
// ... 其他导入保持不变 ...
import { importOwner, exportOwner } from '@/api/owner'

// ... 其他变量定义保持不变 ...

// 是否显示导入对话框
const importOpen = ref(false)
// 导入文件
const importFile = ref(null)

/** 导入按钮操作 */
function handleImport() {
  importOpen.value = true
}

/** 文件选择变化 */
function handleFileChange(file) {
  importFile.value = file.raw
}

/** 提交导入 */
function submitImport() {
  if (!importFile.value) {
    ElMessage.warning('请选择要导入的文件')
    return
  }
  const formData = new FormData()
  formData.append('file', importFile.value)
  importOwner(formData).then(() => {
    ElMessage.success('导入成功')
    importOpen.value = false
    getList()
  })
}

/** 导出按钮操作 */
function handleExport() {
  exportOwner(queryParams).then(response => {
    const blob = new Blob([response.data])
    const link = document.createElement('a')
    link.href = window.URL.createObjectURL(blob)
    link.download = '业主信息.xlsx'
    link.click()
    window.URL.revokeObjectURL(link.href)
  })
}
</script>