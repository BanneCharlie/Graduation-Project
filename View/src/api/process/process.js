import request from '@/utils/request'
import { param } from 'jquery'

export function getProcessList(sxId) {
  const params = {sxId: sxId}
  return request({
    url: '/process/go-process',
    method: 'get',
    params: params
  })
}

export function goProcessTemplate(data) {
  return request({
    url: '/template/go-template',
    method: 'post',
    data
  })
}

export function goRunProcessTemplate(data) {
  return request({
    url: '/template/go-template',
    method: 'post',
    data
  })
}

export function startProcess({businessKey, processKey, ssot,reportType}) {
  const params = {
    businessKey: businessKey,
    processKey: processKey,
    ssot: ssot,
    reportType:reportType
  }
  return request({
    url: '/process/start-process',
    method: 'get',
    params: params
  })
}

export function firstRunProcess({processKey, businessKey, chooseFlow, selectedUserIds, procTitleName}) {
  const params = {
    processKey: processKey,
    businessKey: businessKey,
    chooseFlow: chooseFlow,
    selectedUserIds: selectedUserIds,
    procTitleName: procTitleName
  }
  return request({
    url: '/process/first-run-process',
    method: 'get',
    params: params
  })
}

export function goRunProcess( params ) {
  return request({
    url: '/process/go-run-process',
    method: 'get',
    params
  })
}

export function runProcess({processKey, businessKey, chooseFlow, taskId, blfs, title, processInstanceId, addTaskNames, addTaskIds, selectedUserIds, selectedUserName, stepName}) {
  const params = {
    processKey: processKey,
    businessKey: businessKey,
    chooseFlow: chooseFlow,
    taskId: taskId,
    blfs: blfs,
    title: title,
    processInstanceId: processInstanceId,
    addTaskNames: addTaskNames,
    addTaskIds: addTaskIds,
    selectedUserIds: selectedUserIds,
    selectedUserName: selectedUserName,
    stepName: stepName
  }
  return request({
    url: '/process/run-process',
    method: 'get',
    params: params
  })
}

export function getAdviceMsg({tableId, signControlName}) {
  const params = {tableId: tableId, signControlName: signControlName}
  return request({
    url: '/process/form-advice-list',
    method: 'get',
    params: params
  })
}

//查询当前登录用户的待办事项
export function getUserAllProcess(query) {
  return request({
    url: '/process/get-user-all-process',
    method: 'get',
    params: query
  })
}

//查询当前登录用户的已办事项
export function getUserAllHistoryProcess(query) {
  return request({
    url: '/process/get-user-all-history-process',
    method: 'get',
    params: query
  })
}

//撤回已办的流程
export function handleRecall(enId) {
  const params = {enId: enId}
  return request({
    url: '/process/push-back',
    method: 'get',
    params: params
  })
}

export function getPreviousUser(piId, aiId) {
  const params = {piId: piId, aiId: aiId}
  return request({
    url: '/process/get-previous-user',
    method: 'get',
    params: params
  })
}
