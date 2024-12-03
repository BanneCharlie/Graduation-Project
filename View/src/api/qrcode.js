import request from '@/utils/request'

// 手机扫描二维码获取数据信息
export function showTemplateReportData(params){
    return request({
        url: '/showReportData',
        method: 'get',
        params: params
    })
}