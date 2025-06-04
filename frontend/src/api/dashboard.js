import request from '@/utils/request'

/**
 * 获取仪表盘数据
 * @param {Object} params
 * @param {number} params.projectId - 项目ID
 * @param {string} params.timeRange - 时间范围（week/month/year）
 * @returns {Promise}
 */
export function getDashboardData(params) {
  return request({
    url: '/api/dashboard/data',
    method: 'get',
    params
  })
}
