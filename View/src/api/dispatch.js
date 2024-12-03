import request from '@/utils/request'

//表单保存
export function getAdvice(piid) {
  const params = {piid: piid}
  return request({
    url: '/process/dispatch-advice-strategy-list',
    method: 'get',
    params: params
  })
}
