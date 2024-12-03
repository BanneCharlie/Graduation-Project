package com.ruoyi.project.business.service;

import com.ruoyi.project.business.domain.BusinessNumTemp;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-10 11:28 - 星期四
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */
public interface BusinessNumTempService extends IBaseService<BusinessNumTemp> {
    /**
     * --->
     *  Client Select Update --> Redis --> Client --(Commit Start Process)--> Save Database
     *  这个方法是 经过一系列逻辑 获得往redis中存储的临时编号的数值
     * @author xqh, 987072248@qq.com
     * @date 2021/6/11 15:48
     * @param deviceType 只需要知道当前操作的是哪个设备类别即可 ， 其他参数不需要
     * @return {@link Integer}
     */
    Integer saveCurrentOperationNumberToRedis(String deviceType);
    /**
     * --->
     *  处理当前 表单将要抛弃的 设备类型的编号 , 将其从 redis中剔除
     * @author xqh, 987072248@qq.com
     * @date 2021/6/17 9:37
     * @param deviceType
     * @param ajaxNumber
     * @return {@link boolean}
     */
    boolean handleCurrentCancelNumber(String deviceType,String ajaxNumber);

    List<Object> selectBusinessRowIds();
}
