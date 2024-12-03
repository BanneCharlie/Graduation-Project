import request from '@/utils/request'

export function getCategoryCredenceFileList(businessRowId){
    return request({
        url:'/paymentCredence/getAllCategoryCredenceFile/' + businessRowId,
        method:'get'
    })
}

export function deleteCurrentOperationCredenceFile(rowId){
    return request({
        url:'/paymentCredence/deleteCurrentOperationFile/' + rowId,
        method:'delete'
    })
}

export function setCurrentFileIsDefault(rowId){
    return request({
        url:'/paymentCredence/setCurrentFileDefault/' + rowId,
        method:'put'
    })
}

export function updateRealMoneyApply(data){
    return request({
        url: '/paymentCredence/updateRealMoneyApply',
        method: 'post',
        data: data
    })
}

export function confirmCurrentBusinessIsPaymentSuccess(businessRowId) {
    return request({
        url: '/paymentCredence/confirmBusinessPaymentIsSuccess/' + businessRowId,
        method: 'put'
    })
}