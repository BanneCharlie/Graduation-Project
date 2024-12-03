import request from '@/utils/request'

export function getAllFlowInst(query) {
    return request({
        url: '/engine-mgmt-inst/process-inst-mgmt',
        method: 'get',
        params: query
    })
  }

export function editFlowInst(param){
    return request({
        url: '/engine-mgmt-inst/process-inst-input',
        method: 'get',
        params: param
    })
}

export function removeFlowInst(param) {
    return request({
        url: '/engine-mgmt-inst/process-def-remove',
        method: 'get',
        params: param
    })
  }

export function saveFlowInst(param) {

    return request({
        url: '/engine-mgmt-inst/process-inst-save',
        method: 'post',
        params: param
    })
  }

export function flowInstNode(query){

    return request({
        url: '/engine-mgmt-inst/proc-inst-act-list',
        method: 'get',
        params: query
    })
}

export function editInstNode(param) {
    return request({
        url: '/engine-mgmt-inst/process-inst-act-input',
        method: 'get',
        params: param
    })
  }

export function saveInstNode(param) {
    return request({
        url: '/engine-mgmt-inst/process-inst-act-save',
        method: 'post',
        params: param
    })
  }

export function removeInstNode(param) {
    return request({
        url: '/engine-mgmt-inst/process-inst-act-remove',
        method: 'get',
        params: param
    })
  }

export function flowInstNodeActiveUserList(param) {
    return request({
        url: '/engine-mgmt-inst/act-userlist',
        method: 'get',
        params: param
    })
  }

export function currentFlowInstNodeUser(param) {
    return request({
        url: '/engine-mgmt-inst/act-user-edit',
        method: 'get',
        params: param
    })
}

export function saveWorkItem(param) {
    return request({
        url: '/engine-mgmt-inst/saveWorkItem',
        method: 'get',
        params: param
    })
  }
/*
  删除节点关联对象
*/
export function removeFlowInstNodeUser(param) {
    return request({
        url: '/engine-mgmt-inst/act-removeUser',
        method: 'get',
        params: param
    })
  }

  /*
    选择节点关联人员
  */
export function selectActiveUser(param) {
    return request({
        url: '/engine-mgmt-inst/act-userlookup',
        method: 'get',
        params: param
    })

  }
  
//判断流程是否已经结束，1为已结束，0为未结束，-1为无该流程
export function isEndByPiId(piId) {
	return request({
		url: '/engine-mgmt-inst/is-end/' + piId,
		method: 'get',
	})
}
