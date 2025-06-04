import request from '@/utils/request'

export function getOwnerPage(params) {
  return request({
    url: '/api/owner/page',
    method: 'get',
    params
  })
}

export function getOwnerDetail(id) {
  return request({
    url: `/api/owner/${id}`,
    method: 'get'
  })
}

export function createOwner(data) {
  return request({
    url: '/api/owner',
    method: 'post',
    data
  })
}

export function updateOwner(data) {
  return request({
    url: '/api/owner',
    method: 'put',
    data
  })
}

export function deleteOwner(id) {
  return request({
    url: `/api/owner/${id}`,
    method: 'delete'
  })
}