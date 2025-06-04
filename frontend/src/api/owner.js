import request from '@/utils/request'

// ... 其他方法保持不变 ...

export function importOwner(data) {
  return request({
    url: '/api/owner/import',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function exportOwner(params) {
  return request({
    url: '/api/owner/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}