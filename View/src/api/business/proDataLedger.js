import request from '@/utils/request'

//根据rowId查询表单信息
export function getDataList(params) {
  return request({
    url: '/prod-data-ledger/get-data-list',
    method: 'get',
    params: params
  })
}

export function downloadTemplate() {
  return request({
    url: '/prod-data-ledger/importTemplate',
    method: 'get'
  })
}

export function downloadReport(reportName) {
  return request({
    url: '/prod-data-ledger/download-report',
    method: 'get',
    params: { reportName: reportName }
  })
}