import request from '@/utils/request'

// 查询作业指导书下拉树结构
export function treeselectFile() {
  return request({
    url: '/file-work/tree',
    method: 'get',
  })
}

