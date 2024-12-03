import request from '@/utils/request'

//获取委托检验合同申请信息及对应的检验报告申请
export function getContractAndReportInfo(query) {
  return request({
    url: '/req-statistics/get-list',
    method: 'get',
    params: query
  })
}