import request from '@/utils/request'


//查询已保存的表单列表
export function list(query) {
    return request({
      url: '/report-examine/get-list',
      method: 'get',
      params: query
    })
  }
  
//表单保存
export function formSave(data) {
return request({
    url: '/report-examine/form-save',
    method: 'post',
    data
})
}
  
//根据rowId查询表单信息
export function getFormInfo(rowId) {
    const params = {rowId: rowId}
    return request({
      url: '/report-examine/get-info',
      method: 'get',
      params: params
    })
}
//根据rowId查询表单信息
export function getFormInfoByReportId(reportId) {
    const params = {reportId: reportId}
    return request({
      url: '/report-examine/get-info-by-reportId',
      method: 'get',
      params: params
    })
}

export function getEndLoginInfo() {
  return request({
    url: '/report-examine/get-end-login-info',
    method: 'get',
  })
}
export function setRedisCacheNotice() {
  return request({
    url: '/report-examine/set-notice-cache',
    method: 'put',
  })
}
