import request from '@/utils/request'

/**
 * 获取分页数据
 */
export function treeList(query) {
  return request({
    url: '/template-row/tree-list',
    method: 'get',
    params: query
  })
}

export function listRow() {
  return request({
    url: '/template-row/list-row',
    method: 'get'
  })
}

export function saveReportRow(data) {
  return request({
    url: '/template-row/save',
    method: 'post',
    data: data
  })
}

export function getInfoByRowId(rowId) {
  return request({
    url: '/template-row/get-info/' + rowId,
    method: 'get',
  })
}

export function delReportRow(rowIds) {
  return request({
    url: '/template-row/delete/' + rowIds,
    method: 'delete'
  })
}
