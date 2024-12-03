import request from '@/utils/request';

export function listNextMgt(query) {
	return request({
		url: '/engine-act/act-def-next',
		method: 'get',
		params: query
	});
}

export function actLineLookup(procDefId) {
	const params = { procDefId: procDefId };
	return request({
		url: '/engine-act/act-line-lookup',
		method: 'get',
		params
	});
}

export function actSaveLineToActs(data) {
	return request({
		url: '/engine-act/act-saveLineToActs',
		method: 'post',
		data
	});
}

export function saveNext(data) {
	return request({
		url: '/engine-act/act-def-next-save',
		method: 'post',
		data: data
	});
}

export function delNext(enIds) {
	return request({
		url: '/engine-act/delete/next/' + enIds,
		method: 'delete'
	});
}

export function updateNodeLineName(data) {
	return request({
		url: '/engine-act/update-node-line-name',
		method: 'put',
		data: data
	});
}
