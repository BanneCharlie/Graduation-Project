package com.ruoyi.project.business.temp;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.project.business.domain.AttachmentInstance;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@TableName("business_attachment_instance_temp")
public class AttachmentInstanceTemp extends AttachmentInstance {
    @TableId("attachment_id")
    private String attachmentId;
    private String tableName;
    private String tableId;
    private String attachmentName;
    private String uploaderName;
    private Date uploadTime;
    private String userId;
    private String flowId;
    private String nodeId;
    private String fileSize;
    private String fileMime;
    private String filePc;
    private String filePath;
    private String extendName;
    private String orderId;
    private String state;
    private Integer isDelete;

    private String piid;
    private String paid;
    private Date createTime;
    private String createUserId;
    private String createUserName;
    private String attachmentType;
    private String attachmentSerialNumber;
}
