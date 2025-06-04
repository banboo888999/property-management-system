import request from '@/utils/request'

export function getDashboardData(params) {
  return request({
    url: '/api/dashboard/data',
    method: 'get',
    params
  })
}