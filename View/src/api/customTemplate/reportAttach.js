import request from "@/utils/request";

/**
 * 获取当前用户附件数据列表
 */
export function getSelfAttachData(params) {
  return request({
    url: "/report-attach/get-self-data",
    method: "get",
    params: params,
  });
}
// 上传附件信息
export function uploadAttach(data) {
  return request({
    url: "/report-attach/upload-attach",
    method: "post",
    data: data,
  });
}
// 删除自己上传的某条附件信息
export function removeSelfAttachData(attachId) {
  return request({
    url: "/report-attach/remove-self-attach/" + attachId,
    method: "delete",
  });
}
