package com.ruoyi.project.business.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-25 09:34 - 星期三
 * @package: com.ruoyi.project.business.vo
 * @JDK-Version : 1.8
 */
@Data
@NoArgsConstructor
public class ReportMessageVo {
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastLoginTime;
    private List<String> reportNumbers;

    public static ReportMessageVo createVoidInstance(){
        return new ReportMessageVo();
    }
}
