package com.ruoyi.project.business.temp;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 15:09__星期五
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("business_file_upload_temp")
public class FileUploadInstanceTemp{
    @TableId("row_id")
    private String rowId;               // 该文件唯一id
    private String uploadName;          // 文件名
    private String filePath;            // 上传路径
    private String fileIdentifyName;    // 文件名编码
    private String fileType;            // 文件类型
    private Date uploadTime;            // 上传日期
    private String uploadUserId; // 上传附件人主键Id
    private String uploadUserName;
    private Integer fileAttribute; // 1.委托检验文件 2.委托报告 3.产品检验文件 4.产品报告
    private Integer isDelete; // 0 存在 1 删除

    private String ptid;
    private String piid;
    private String paid;

}
