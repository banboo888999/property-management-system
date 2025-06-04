<template>
  <div ref="chartRef" :style="{ height: height, width: width }" />
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue'
import * as echarts from 'echarts'
import { useResizeHandler } from '@/hooks/useResizeHandler'

const props = defineProps({
  width: {
    type: String,
    default: '100%'
  },
  height: {
    type: String,
    default: '350px'
  },
  chartData: {
    type: Object,
    required: true
  }
})

const chartRef = ref(null)
let chart = null

// 初始化图表
const initChart = () => {
  if (!chartRef.value) return
  
  chart = echarts.init(chartRef.value)
  setOptions(props.chartData)
}

// 设置图表配置
const setOptions = (data) => {
  if (!chart) return
  
  chart.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        label: {
          backgroundColor: '#6a7985'
        }
      }
    },
    legend: {
      data: ['物业费', '车位费', '其他收入']
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: [
      {
        type: 'category',
        boundaryGap: false,
        data: data.xAxis
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: '金额（元）',
        axisLabel: {
          formatter: '{value}'
        }
      }
    ],
    series: [
      {
        name: '物业费',
        type: 'line',
        stack: '总量',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: data.propertyFee
      },
      {
        name: '车位费',
        type: 'line',
        stack: '总量',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: data.parkingFee
      },
      {
        name: '其他收入',
        type: 'line',
        stack: '总量',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: data.otherIncome
      }
    ]
  })
}

// 监听数据变化
watch(
  () => props.chartData,
  (val) => {
    setOptions(val)
  },
  { deep: true }
)

// 处理窗口大小变化
const { addResizeListener, removeResizeListener } = useResizeHandler()

onMounted(() => {
  initChart()
  addResizeListener(chartRef.value, () => {
    if (chart) {
      chart.resize()
    }
  })
})

onBeforeUnmount(() => {
  if (chart) {
    chart.dispose()
    chart = null
  }
  removeResizeListener(chartRef.value)
})
</script>