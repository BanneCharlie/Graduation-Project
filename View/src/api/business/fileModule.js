import request from '@/utils/request'

// 流程中附件上传
export function businessFileUpload(data) {
    return request({
      url: '/fileModule/upload',
      method: 'post',
      data: data
    })
}
// 电子签名文件上传
export function signatureFileUpload(data) {
  return request({
    url: '/fileModule/uploadSignature',
    method: 'post',
    data: data
  })
}
// 缴费凭证文件上传
export function paymentCredenceFileUpload(data) {
  return request({
    url:'/fileModule/uploadPaymentCredence',
    method:'post',
    data:data
  })
}
// 检验文件存在
export function checkFile(params){
    return request({
      url:'/fileModule/checkFile',
      method:'get',
      params:params
    })
}

export function getFileListByIds(params){
  return request({
    url: 'fileModule/fileList',
    method:'get',
    params:params
  })
}