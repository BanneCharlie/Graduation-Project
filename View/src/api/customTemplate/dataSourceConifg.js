import request from '@/utils/request'

/**
 * 获取分页数据
 */
export function list(query) {
  return request({
    url: '/data-source/list',
    method: 'get',
    params: query
  })
}

export function getAll() {
  return request({
    url: '/data-source/getAll',
    method: 'get'
  })
}

export function saveDataSource(data) {
  return request({
    url: '/data-source/save',
    method: 'post',
    data: data
  })
}

export function delDataSource(rowIds) {
  return request({
    url: '/data-source/delete/' + rowIds,
    method: 'delete'
  })
}

export function getDataSourceById(rowIds) {
  return request({
    url: '/data-source/getOne/' + rowIds,
    method: 'get'
  })
}

export function updatePassword(data) {
  return request({
    url: '/data-source/update-password',
    method: 'post',
    data: data
  })
}

// export function getSchema(dataSourceId) {
//   return request({
//     url: '/data-source/get-schema/' + dataSourceId,
//     method: 'get'
//   })
// }

export function getField() {
  return request({
    url: '/data-source/getField',
    method: 'get',
  })
}
