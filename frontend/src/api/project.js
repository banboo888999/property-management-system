import request from '@/utils/request'

// 获取项目列表
export function getProjectList(params) {
  return request({
    url: '/api/project/list',
    method: 'get',
    params
  })
}

// 获取项目详情
export function getProject(id) {
  return request({
    url: `/api/project/${id}`,
    method: 'get'
  })
}

// 创建项目
export function createProject(data) {
  return request({
    url: '/api/project',
    method: 'post',
    data
  })
}

// 更新项目
export function updateProject(data) {
  return request({
    url: '/api/project',
    method: 'put',
    data
  })
}

// 删除项目
export function deleteProject(id) {
  return request({
    url: `/api/project/${id}`,
    method: 'delete'
  })
}

// 切换项目状态
export function toggleProjectStatus(id, status) {
  return request({
    url: `/api/project/${id}/status/${status}`,
    method: 'put'
  })
}