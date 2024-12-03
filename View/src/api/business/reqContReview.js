import request from '@/utils/request'

//根据rowId查询表单信息
export function getFormInfo(rowId) {
  const params = {rowId: rowId}
  return request({
    url: '/req-cont-review/get-review-info',
    method: 'get',
    params: params
  })
}

//表单保存
export function formSave(data) {
  return request({
    url: '/req-cont-review/form-save',
    method: 'post',
    data
  })
}

//查询已保存的表单列表
export function listReqContReview(query) {
  return request({
    url: '/req-cont-review/get-list',
    method: 'get',
    params: query
  })
}

//删除已保存的表单
export function delReqContReview(rowIds) {
  return request({
    url: '/req-cont-review/del-apply/' + rowIds,
    method: 'delete'
  })
}

// 根据当前合同流水Id 来查看是否缴费
export function checkPaymentIsSuccess(rowId){
  return request({
    url: 'req-cont-review/check-payment-status',
    method: 'get',
    params: {rowId:rowId}
  })
}