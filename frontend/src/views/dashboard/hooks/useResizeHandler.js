import { onMounted, onBeforeUnmount } from 'vue'

export function useResizeHandler(chart) {
  const resizeHandler = () => {
    if (chart) {
      chart.resize()
    }
  }

  const addResizeListener = () => {
    window.addEventListener('resize', resizeHandler)
  }

  const removeResizeListener = () => {
    window.removeEventListener('resize', resizeHandler)
  }

  return {
    addResizeListener,
    removeResizeListener
  }
}
