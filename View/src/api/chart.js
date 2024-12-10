import request from '@/utils/request'

// 获取柱状图对应的数据信息
export function getBarChartData(){
    return request({
        url: '/process/getBarChartData',
        method: 'get',
    })
}

export function getPieChartData(){
  return request({
    url: '/process/getPieChartData',
    method: 'get',
  })
}
