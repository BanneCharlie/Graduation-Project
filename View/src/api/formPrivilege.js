import request from '@/utils/request'

export function getFormPrivilege({taskId}) {
  const params = {
    taskId: taskId
  }
  return request({
    url: '/form-privilege/list',
    method: 'get',
    params
  })
}
