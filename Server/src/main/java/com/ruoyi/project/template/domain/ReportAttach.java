package com.ruoyi.project.template.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * --->
 *
 * @author xqh , 987072248@qq.com
 * @data 2023-06-12 09:48:46
 */
@Data
@Builder
@TableName("report_attach")
@NoArgsConstructor
@AllArgsConstructor
public class ReportAttach {

    @TableId(value = "row_id" , type = IdType.ID_WORKER_STR)
    private String rowId;
    /** 数据分类 类型标识
     * @see com.ruoyi.project.template.commons.enums.AttachDataType
     * */
    private String dataType;
    /** 上传名称 */
    private String uploadName;
    /** 文件类型 */
    private String fileType;
    /** 文件路径 */
    private String filePath;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String createUserId;

}
