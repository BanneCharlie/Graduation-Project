import request from '@/utils/request'



// 部门负责人 对已经完成流程的任务进行任务派发
export function getAllFinishProcess(query){
  return request({
    url: '/device-task/contract-send',
    method: 'get',
    params: query
  })
}
export function getSendTaskUserList(query){
  return request({
    url: '/device-task/getSendTaskUser',
    method: 'get',
    params: query
  })
}

export function sendTaskRunning(rowIds,userIds){
  return request({
    url: '/device-task/sendTaskRunning/'+rowIds+'/' + userIds,
    method: 'post',
  })
}

// ------------------------关联其他人员模块
export function getDeviceRelationList(data) {
  return request({
    url:'/device-task/receive-relation-list',
    method:'get',
    params: data,
  })
}

export function relationUsers(deviceId , userIds) {
  return request({
    url:'/device-task/receive-relation-user/' + deviceId + '/' + userIds,
    method:'post',
  })
}

export function relationDeleteUsers(rowIds) {
  return request({
    url:'/device-task/receive-relation-delete/' + rowIds,
    method:'delete',
  })
}

// ------------ 报告模块
export function getMineReportList(data) {
  return request({
    url:'/device-task/mime-report-list',
    method:'get',
    params:data
  })
}


export function getReportInstace(reportId) {
  return request({
    url:'/device-task/reportInstance/' + reportId,
    method:'get',
  })
}
// 替换意见和内容数据
export function replaceReportContext(data) {
  return request({
    url:'/device-task/replaceReportContext',
    method:'post',
    data
  })
}
/**
 *
 *  操作报告 ， 
 *    1. 选择完报告之后 先调用当前方法 生成临时报告 标志位 generic 为 0
 *    2. 点击生成报告之后会改版该标志位来 用来显示在我的报告里
 * @export
 * @param {*} data
 * @return {*} 
 */
export function operateReport(data) {
  return request({
    url:'/device-task/operate-report',
    method:'post',
    data:data
  })
}

export function saveReportTemplate(data) {
  return request({
    url:'/device-task/save-report-template',
    method:'post',
    data:data
  })
}


// -------------
export function delRelationUser(rowId,userIds){
  return request({
    url: '/device-task/deleteRelationUser/' + rowId + '/' + userIds,
    method: 'delete'
  });
}
export function getWaitSendTaskCount(){
  return request({
    url: '/device-task/getWaitSendTaskCount',
    method: 'get'
  });
}

//获取任务池所有任务任务
export function getTaskReceive(query) {
  return request({
    url: '/device-task/task-receive',
    method: 'get',
    params: query
  })
}

//获取待领取任务总数
export function getTaskReceiveCount() {
  return request({
    url: '/device-task/task-receive-count',
    method: 'get'
  })
}

//领取任务
export function receiveTask(rowIds) {
  return request({
    url: '/device-task/receive-task/' + rowIds,
    method: 'post'
  })
}

//归还任务
export function revertTask(rowIds) {
  return request({
    url: '/device-task/revert-task/' + rowIds,
    method: 'post'
  })
}

//查询所有当前用户的待处理任务（任务管理菜单下）
export function getTaskPending(query) {
  return request({
    url: '/device-task/task-pending',
    method: 'get',
    params: query
  })
}

//查询所有当前用户的已处理任务（任务管理菜单下）
export function getTasksProcessed(query) {
  return request({
    url: '/device-task/task-processed',
    method: 'get',
    params: query
  })
}