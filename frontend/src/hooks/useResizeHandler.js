import { onBeforeUnmount } from 'vue'

export function useResizeHandler() {
  const resizeHandler = new Map()

  const addResizeListener = (element, handler) => {
    if (!element || !handler) return

    const resizeObserver = new ResizeObserver(handler)
    resizeObserver.observe(element)
    resizeHandler.set(element, resizeObserver)
  }

  const removeResizeListener = (element) => {
    if (!element) return

    const observer = resizeHandler.get(element)
    if (observer) {
      observer.disconnect()
      resizeHandler.delete(element)
    }
  }

  onBeforeUnmount(() => {
    resizeHandler.forEach((observer) => {
      observer.disconnect()
    })
    resizeHandler.clear()
  })

  return {
    addResizeListener,
    removeResizeListener
  }
}