package com.ruoyi.project.api.pojo;

import lombok.Data;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-06-08 14:17:54
 */
@Data
public class ResolveReportFormVo {

    /** 隶属于哪个合同评审流程Id */
    private String contractReviewId;
    /** 当前报告rowId */
    private String reportId;

}
