import store from '@/store'

function checkPermission(el, binding) {
  const { value } = binding
  const permissions = store.getters && store.getters.permissions

  if (value && value instanceof Array) {
    if (value.length > 0) {
      const hasPermission = permissions.some(permission => {
        return value.includes(permission)
      })

      if (!hasPermission) {
        el.parentNode && el.parentNode.removeChild(el)
      }
    }
  } else {
    if (!permissions.includes(value)) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  }
}

export default {
  mounted(el, binding) {
    checkPermission(el, binding)
  },
  updated(el, binding) {
    checkPermission(el, binding)
  }
}