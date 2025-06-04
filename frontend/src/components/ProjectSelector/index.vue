<template>
  <el-dropdown trigger="click" @command="handleCommand">
    <span class="project-selector">
      {{ currentProject?.name || '请选择项目' }}
      <el-icon class="el-icon--right"><arrow-down /></el-icon>
    </span>
    <template #dropdown>
      <el-dropdown-menu>
        <el-dropdown-item
          v-for="project in projectList"
          :key="project.id"
          :command="project"
          :disabled="project.status === 0"
        >
          {{ project.name }}
          <el-tag
            v-if="project.status === 0"
            size="small"
            type="danger"
            effect="dark"
          >已禁用</el-tag>
        </el-dropdown-item>
      </el-dropdown-menu>
    </template>
  </el-dropdown>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ArrowDown } from '@element-plus/icons-vue'
import { useStore } from 'vuex'
import { getProjectList } from '@/api/project'

const store = useStore()
const projectList = ref([])
const currentProject = ref(null)

// 获取项目列表
const getProjects = async () => {
  try {
    const { data } = await getProjectList({
      pageSize: 100,
      status: 1
    })
    projectList.value = data.records
    
    // 如果有存储的项目ID，设置为当前项目
    const storedProjectId = store.state.project.currentProjectId
    if (storedProjectId) {
      const project = projectList.value.find(p => p.id === storedProjectId)
      if (project) {
        currentProject.value = project
      }
    }
  } catch (error) {
    console.error('获取项目列表失败:', error)
  }
}

// 切换项目
const handleCommand = (project) => {
  currentProject.value = project
  store.dispatch('project/setCurrentProject', project)
}

onMounted(() => {
  getProjects()
})
</script>

<style scoped>
.project-selector {
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 0 10px;
  height: 50px;
  color: var(--el-text-color-primary);
}

.project-selector:hover {
  background: var(--el-color-primary-light-9);
}
</style>