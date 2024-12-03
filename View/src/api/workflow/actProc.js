import request from '@/utils/request'

// 查询流程模板列表
export function listActMgt(query) {
  return request({
    url: '/engine-act/proc-def-act-list',
    method: 'get',
    params: query
  })
}

// 导出流程模板
export function exportActProc(enIds) {
  return request({
    url: '/engine-act/export/' + enIds,
    method: 'get'
  })
}

// 查询流程模板详细
export function getActProc(enId) {
  return request({
    url: '/engine-act/get-proc-def-act/' + enId,
    method: 'get'
  })
}

// 新增用户
export function saveActProc(data) {
  return request({
    url: '/engine-act/act-def-save',
    method: 'post',
    data: data
  })
}

// 删除流程模板
export function delActProc(enIds) {
  return request({
    url: '/engine-act/delete/' + enIds,
    method: 'delete'
  })
}
