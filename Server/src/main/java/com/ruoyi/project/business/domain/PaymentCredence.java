package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-07-02 15:54 - 星期五
 * @package: com.ruoyi.project.business.domain
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@TableName("business_payment_credence")
public class PaymentCredence {
    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String businessRowId;
    private String businessCategory;
    private BigDecimal realMoney;
    private int paymentType;
    private String confirmUserName;
    private String confirmUserNickname;
    private String credenceUploadFileName;
    private String credenceFileType;
    private String credenceFilePath;
    private String credenceFileSize;
    private int isDefault;          // 0 不默认 ， 1 默认
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
