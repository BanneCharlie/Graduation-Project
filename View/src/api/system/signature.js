import request from '@/utils/request'

export function getCurrentUserSignature() {
    return request({
        url: '/system/signature/getCurrentUserSignature',
        method: 'post'
    })
}

export function deleteSignatureById(deleteId){
    return request({
        url:'/system/signature/deleteSignature/' + deleteId,
        method:'delete'
    });
}

