import request from '@/utils/request'

// 查询流程模板列表
export function listEngineMgt(query) {
  return request({
    url: '/engine-mgt/process-def-mgt',
    method: 'get',
    params: query
  })
}

// 导出流程模板
export function exportEngineProc(enIds) {
  return request({
    url: '/engine-mgt/export/' + enIds,
    method: 'get'
  })
}

// 查询流程模板详细
export function getEngineProc(enId) {
  return request({
    url: '/engine-mgt/get-engine-proc/' + enId,
    method: 'get'
  })
}

// 新增用户
export function saveEngineProc(data) {
  return request({
    url: '/engine-mgt/process-def-save',
    method: 'post',
    data: data
  })
}

// 删除流程模板
export function delEngineProc(enIds) {
  return request({
    url: '/engine-mgt/delete/' + enIds,
    method: 'delete'
  })
}
