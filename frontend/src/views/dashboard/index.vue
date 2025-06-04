<template>
  <div class="dashboard-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in statisticsData" :key="item.title">
        <statistics-card
          :title="item.title"
          :value="item.value"
          :icon="item.icon"
          :color="item.color"
        />
      </el-col>
    </el-row>

    <!-- 收入趋势图 -->
    <el-row :gutter="20" class="mt-4">
      <el-col :span="16">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>收入趋势</span>
              <el-radio-group v-model="timeRange" size="small">
                <el-radio-button label="week">本周</el-radio-button>
                <el-radio-button label="month">本月</el-radio-button>
                <el-radio-button label="year">全年</el-radio-button>
              </el-radio-group>
            </div>
          </template>
          <line-chart :chart-data="lineChartData" />
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>收入构成</span>
            </div>
          </template>
          <pie-chart :chart-data="pieChartData" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 待办事项 -->
    <el-row :gutter="20" class="mt-4">
      <el-col :span="24">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>待办事项</span>
            </div>
          </template>
          <el-table :data="todoList" style="width: 100%">
            <el-table-column prop="type" label="类型" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.tagType">{{ scope.row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="content" label="内容" />
            <el-table-column prop="deadline" label="截止时间" width="180" />
            <el-table-column prop="status" label="状态" width="120">
              <template #default="scope">
                <el-tag :type="scope.row.status === '待处理' ? 'warning' : 'success'">
                  {{ scope.row.status }}
                </el-tag>
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
import StatisticsCard from './components/StatisticsCard.vue'
import LineChart from './components/LineChart.vue'
import PieChart from './components/PieChart.vue'
import { getDashboardData } from '@/api/dashboard'

// 时间范围选择
const timeRange = ref('month')

// 统计数据
const statisticsData = ref([
  { title: '本月收入', value: '¥0', icon: 'Money', color: '#40c9c6' },
  { title: '待缴费用户', value: '0', icon: 'User', color: '#36a3f7' },
  { title: '本月新增业主', value: '0', icon: 'Plus', color: '#f4516c' },
  { title: '投诉工单', value: '0', icon: 'Warning', color: '#f6ab40' }
])

// 折线图数据
const lineChartData = ref({
  xAxis: [],
  propertyFee: [],
  parkingFee: [],
  otherIncome: []
})

// 饼图数据
const pieChartData = ref({
  legend: ['物业费', '车位费', '其他收入'],
  series: []
})

// 待办事项
const todoList = ref([])

// 获取仪表盘数据
const fetchDashboardData = async () => {
  try {
    const response = await getDashboardData({ timeRange: timeRange.value })
    const { statistics, incomeData, incomeProportion, todos } = response.data

    // 更新统计数据
    statisticsData.value[0].value = `¥${statistics.monthlyIncome}`
    statisticsData.value[1].value = statistics.unpaidUsers
    statisticsData.value[2].value = statistics.newOwners
    statisticsData.value[3].value = statistics.complaints

    // 更新折线图数据
    lineChartData.value = incomeData

    // 更新饼图数据
    pieChartData.value.series = incomeProportion

    // 更新待办事项
    todoList.value = todos
  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
  }
}

// 监听时间范围变化
watch(timeRange, () => {
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