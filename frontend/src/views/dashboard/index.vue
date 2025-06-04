<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6">
        <statistics-card
          title="本月收入"
          :value="formatMoney(statistics.monthlyIncome)"
          icon="Money"
          color="#409EFF"
        />
      </el-col>
      <el-col :span="6">
        <statistics-card
          title="待缴费用户"
          :value="statistics.unpaidUsers"
          icon="User"
          color="#F56C6C"
        />
      </el-col>
      <el-col :span="6">
        <statistics-card
          title="本月新增业主"
          :value="statistics.newOwners"
          icon="UserFilled"
          color="#67C23A"
        />
      </el-col>
      <el-col :span="6">
        <statistics-card
          title="待处理投诉"
          :value="statistics.complaints"
          icon="Warning"
          color="#E6A23C"
        />
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" class="mt-4">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>收入趋势</span>
              <el-radio-group v-model="timeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">本年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <line-chart
            :x-axis="incomeData.xAxis"
            :property-fee="incomeData.propertyFee"
            :parking-fee="incomeData.parkingFee"
            :other-income="incomeData.otherIncome"
            height="350px"
          />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>收入占比</span>
            </div>
          </template>
          <pie-chart
            :data="incomeProportion"
            height="350px"
          />
        </el-card>
      </el-col>
    </el-row>

    <!-- 待办事项 -->
    <el-row class="mt-4">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待办事项</span>
            </div>
          </template>
          <el-table :data="todoList" style="width: 100%">
            <el-table-column prop="type" label="类型" width="120">
              <template #default="{ row }">
                <el-tag :type="row.tagType">{{ row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="content" label="内容" />
            <el-table-column prop="time" label="时间" width="180" />
            <el-table-column label="操作" width="120" align="center">
              <template #default>
                <el-button type="primary" link>处理</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useStore } from 'vuex'
import { getDashboardData } from '@/api/dashboard'
import StatisticsCard from './components/StatisticsCard.vue'
import LineChart from './components/LineChart.vue'
import PieChart from './components/PieChart.vue'

const store = useStore()
const projectId = computed(() => store.state.project.currentProjectId)

// 时间范围选择
const timeRange = ref('month')

// 统计数据
const statistics = ref({
  monthlyIncome: 0,
  unpaidUsers: 0,
  newOwners: 0,
  complaints: 0
})

// 收入数据
const incomeData = ref({
  xAxis: [],
  propertyFee: [],
  parkingFee: [],
  otherIncome: []
})

// 收入占比
const incomeProportion = ref([])

// 待办事项
const todoList = ref([
  {
    type: '待缴费',
    tagType: 'danger',
    content: '3栋2单元1001业主物业费待缴',
    time: '2024-01-15 10:00:00'
  },
  {
    type: '投诉',
    tagType: 'warning',
    content: '5栋电梯故障投诉',
    time: '2024-01-15 09:30:00'
  },
  {
    type: '维修',
    tagType: 'info',
    content: '地下车库B区照明设备维修申请',
    time: '2024-01-15 09:00:00'
  }
])

// 格式化金额
const formatMoney = (amount) => {
  return `¥${amount?.toFixed(2)}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')
}

// 获取仪表盘数据
const fetchDashboardData = async () => {
  try {
    const { data } = await getDashboardData({
      projectId: projectId.value,
      timeRange: timeRange.value
    })
    statistics.value = data.statistics
    incomeData.value = data.incomeData
    incomeProportion.value = data.incomeProportion
  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
  }
}

// 监听时间范围变化
watch(timeRange, () => {
  fetchDashboardData()
})

// 监听项目变化
watch(() => projectId.value, () => {
  fetchDashboardData()
})

onMounted(() => {
  fetchDashboardData()
})
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;

  .mt-4 {
    margin-top: 20px;
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
