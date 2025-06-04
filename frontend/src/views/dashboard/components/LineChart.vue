<template>
  <div ref="chartRef" :style="{ height, width: '100%' }" />
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps, watch } from 'vue'
import * as echarts from 'echarts'
import { useResizeHandler } from '../hooks/useResizeHandler'

const props = defineProps({
  xAxis: {
    type: Array,
    default: () => []
  },
  propertyFee: {
    type: Array,
    default: () => []
  },
  parkingFee: {
    type: Array,
    default: () => []
  },
  otherIncome: {
    type: Array,
    default: () => []
  },
  height: {
    type: String,
    default: '350px'
  }
})

const chartRef = ref(null)
let chart = null

// 初始化图表
const initChart = () => {
  if (!chartRef.value) return

  chart = echarts.init(chartRef.value)
  const option = {
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'shadow'
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
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: props.xAxis
    },
    yAxis: {
      type: 'value',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [
      {
        name: '物业费',
        type: 'line',
        stack: 'Total',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: props.propertyFee
      },
      {
        name: '车位费',
        type: 'line',
        stack: 'Total',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: props.parkingFee
      },
      {
        name: '其他收入',
        type: 'line',
        stack: 'Total',
        areaStyle: {},
        emphasis: {
          focus: 'series'
        },
        data: props.otherIncome
      }
    ]
  }

  chart.setOption(option)
}

// 监听数据变化
watch(
  () => [props.xAxis, props.propertyFee, props.parkingFee, props.otherIncome],
  () => {
    initChart()
  },
  { deep: true }
)

// 处理窗口大小变化
const { addResizeListener, removeResizeListener } = useResizeHandler(chart)

onMounted(() => {
  initChart()
  addResizeListener()
})

onBeforeUnmount(() => {
  if (chart) {
    chart.dispose()
    chart = null
  }
  removeResizeListener()
})
</script>
