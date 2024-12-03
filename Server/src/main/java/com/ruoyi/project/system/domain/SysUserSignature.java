package com.ruoyi.project.system.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-28 09:49 - 星期一
 * @package: com.ruoyi.project.system.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_images_signature")
public class SysUserSignature {
    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String userId;                  // 用户唯一 id
    private String userName;                // 用户真实姓名
    private String signaturePath;           // 文件全径名称 ， 包含文件名
    private String signatureFileSize;       // 文件大小 KB单位
    private String signatureUploadName;     // 上传时的文件名
    private String signatureServerName;     // 存在服务器的文件名
    private String fileType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private int isDelete;                   // 是否删除 0 未删除 ， 1 删除

}
