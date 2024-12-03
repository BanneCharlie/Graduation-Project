import request from '@/utils/request'

//根据rowId查询表单信息
export function getFormInfo(rowId) {
  const params = {rowId: rowId}
  return request({
    url: '/pro-rep-manage/get-info',
    method: 'get',
    params: params
  })
}

//表单保存
export function formSave(data) {
  return request({
    url: '/pro-rep-manage/form-save',
    method: 'post',
    data
  })
}

//查询已保存的表单列表
export function listReqRepManage(query) {
  return request({
    url: '/pro-rep-manage/get-list',
    method: 'get',
    params: query
  })
}

//删除已保存的表单
export function delReqRepManage(rowIds) {
  return request({
    url: '/pro-rep-manage/del-apply/' + rowIds,
    method: 'delete'
  })
}