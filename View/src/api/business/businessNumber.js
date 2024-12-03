import request from '@/utils/request'

// 获取合同编号
export function getContractNumber(type) {
    return request({
        url: '/business/common/contract/' + type,
        method: 'post',
    });
}

// 更改设备类型 或者关闭当前页面 需要在后台 处理当前 用户拥有的 number编号
export function handleCurrentNumber(deviceType,ajaxNumber) {
    return request({
        url: '/business/common/handleCurrentNumber/'+ deviceType + '/' + ajaxNumber,
        method: 'post'
    });
  }