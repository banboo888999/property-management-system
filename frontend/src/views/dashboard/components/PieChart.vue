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
      trigger: 'item',
      formatter: '{a} <br/>{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 10,
      data: data.legend
    },
    series: [
      {
        name: '收入构成',
        type: 'pie',
        radius: ['50%', '70%'],
        avoidLabelOverlap: false,
        label: {
          show: false,
          position: 'center'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: '30',
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: data.series
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