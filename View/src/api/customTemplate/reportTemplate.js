import request from '@/utils/request'

/**
 * 获取分页数据
 */
export function list(query) {
  return request({
    url: '/report-template/list',
    method: 'get',
    params: query
  })
}

export function getAllTemplate() {
  return request({
    url: '/report-template/getAll',
    method: 'get',
  })
}

export function saveReport(data) {
  // console.log(data);
  return request({
    url: '/report-template/save',
    method: 'post',
    data: data
  })
}

export function delReport(rowIds) {
  return request({
    url: '/report-template/delete/' + rowIds,
    method: 'delete'
  })
}

export function getTemplateIdResolve({templateId, reportId , contractId}) {
  const params = {
    templateId: templateId,
    reportId: reportId,
    contractId: contractId
  }
  return request({
    url: '/report-template/getTemplateIdResolve',
    method: 'get',
    params
  })
}

export function getById(templateId) {
  return request({
    url: '/report-template/getById/' + templateId,
    method: 'get'
  })
}

export function saveContent(data) {
  return request({
    url: '/report-template/save-content',
    method: 'post',
    data
  })
}

export function saveGenericContent(data) {
  return request({
    url: '/report-template/save-generic-content',
    method: 'post',
    data
  })
}

export function changeNotice(changeFlag) {
  return request({
    url:'/report-template/changeNotice/' + changeFlag,
    method:'put'
  })
}

export function currentUserNoticeStatus() {
  return request({
    url:'/report-template/currentUserNotice',
    method:'get'
  })
}