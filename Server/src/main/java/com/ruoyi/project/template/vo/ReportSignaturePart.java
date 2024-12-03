package com.ruoyi.project.template.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-08-02 11:12:28
 */
@Data
@Builder
public class ReportSignaturePart {

    private String userName;

    private String signatureId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date executeTime;

}
