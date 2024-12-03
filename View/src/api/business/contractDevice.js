import request from '@/utils/request'

export function getList(contractId) {
  return request({
    url: '/contract-device/list/' + contractId,
    method: 'get'
  })
}

export function getDeviceById(rowId) {
  return request({
    url: '/contract-device/get-device/' + rowId,
    method: 'get'
  })
}

export function saveDeviceList(data) {
  return request({
    url: '/contract-device/save-list',
    method: 'post',
    data
  })
}

export function getAllDeviceList(contractId) {
  return request({
    url: '/contract-device/all-device/' + contractId,
    method: 'get'
  })
}

export function setDeviceHandle(deviceRowId, reqCheckId) {
  return request({
    url: '/contract-device/set-handle/' + deviceRowId + '/' + reqCheckId,
    method: 'post'
  })
}
