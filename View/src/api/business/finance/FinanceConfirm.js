import request from '@/utils/request'

// 获取年终缴费 并且流程执行结束的合同列表
export function getFinanceConfirmList( params ) {
  return request({
    url: '/contract-review-finance/get-finance-confirm-list',
    method: 'get',
    params: params
  });
}

/**
 * 确认某个合同评审可以进行打印
 * @param {*} contractReviewRowId 具体某个合同rowId
 * @returns 
 */
export function confirmPrintByContractRowId( contractReviewRowId ){
  return request({
    url: '/contract-review-finance/confirm-print/' + contractReviewRowId,
    method: 'put'
  });
}
