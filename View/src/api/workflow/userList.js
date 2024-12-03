import request from '@/utils/request'

export function getActUserList(query) {
  return request({
    url: '/engine-act-user/act-user-list',
    method: 'get',
    params: query
  })
}

export function actUsersLookup() {
  return request({
    url: '/engine-act-user/act-line-lookup',
    method: 'get',
  })
}

export function actSaveUserToActs(data) {
  return request({
    url: '/engine-act-user/act-saveUserToActs',
    method: 'post',
    data
  })
}

export function delUser(userIds) {
  return request({
    url: '/engine-act-user/delete/' + userIds,
    method: 'delete'
  })
}
