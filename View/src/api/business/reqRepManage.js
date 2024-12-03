import request from '@/utils/request'

//根据rowId查询表单信息
export function getFormInfoReqRep(rowId) {
  const params = {rowId: rowId}
  return request({
    url: '/req-rep-manage/get-info',
    method: 'get',
    params: params
  })
}

//表单保存
export function formSave(data) {
  return request({
    url: '/req-rep-manage/form-save',
    method: 'post',
    data
  })
}

//查询已保存的表单列表
export function listReqRepManage(query) {
  return request({
    url: '/req-rep-manage/get-list',
    method: 'get',
    params: query
  })
}

//删除已保存的表单
export function delReqRepManage(rowIds) {
  return request({
    url: '/req-rep-manage/del-apply/' + rowIds,
    method: 'delete'
  })
}

//修改检验报告的模板ID
export function updateTemplateById(rowId, templateId) {
  const params = {rowId: rowId, templateId: templateId}
  return request({
    url: '/req-rep-manage/update-templateId',
    method: 'post',
    params: params
  })
}
