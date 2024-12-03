package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.project.business.temp.AttachmentInstanceTemp;
import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 15:00__星期五
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("business_attachment_instance")
public class AttachmentInstance {
    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String attachmentName;          // 文件名
    private String filePath;            // 上传路径
    private String fileIdentifyName;    // 文件名编码
    private String fileType;            // 文件类型
    //    private Date createTime;            // 上传日期
//    private Date updateTime;            // 更改日期
    private Integer fileAttribute;
    // 1.委托检验文件 0
    // 2.委托报告     1
    // 3.产品检验文件 2
    // 4.产品报告     3
    private Integer isDelete; // 0 存在 1 删除
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    private String createUserId;
    private String createUserName;

    private String ptid;
    private String piid;
    private String paid;


}
