package com.ruoyi.project.process.domian;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-07-22 17:22 - 星期四
 * @package: com.ruoyi.project.process.domian
 * @JDK-Version : 1.8
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_report_author")
public class SysUserReportAuthor {
    private String userId;
    private String userDept;
    private String dt;
    private String cj;
    private String qz;
    private String zj;
    private String kj;
    private String qt;
}
