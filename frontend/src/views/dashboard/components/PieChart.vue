<template>
  <div ref="chartRef" :style="{ height, width: '100%' }" />
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, defineProps, watch } from 'vue'
import * as echarts from 'echarts'
import { useResizeHandler } from '../hooks/useResizeHandler'

const props = defineProps({
  data: {
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
      trigger: 'item',
      formatter: '{b}: ¥{c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '收入占比',
        type: 'pie',
        radius: '70%',
        center: ['60%', '50%'],
        data: props.data.map(item => ({
          name: item.name,
          value: item.value
        })),
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }

  chart.setOption(option)
}

// 监听数据变化
watch(
  () => props.data,
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
