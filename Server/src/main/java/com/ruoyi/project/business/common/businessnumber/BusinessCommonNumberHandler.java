package com.ruoyi.project.business.common.businessnumber;

import com.ruoyi.common.utils.StringUtils;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-09 16:51 - 星期三
 * @package: com.ruoyi.project.business.common.exception
 * @JDK-Version : 1.8
 */
public class BusinessCommonNumberHandler {
    /**
     *  根据 传入的 设备类型获取 对应的枚举对象
     * @param value 设备类型值
     * @return
     */
    public static BusinessCommonNumber getEnumObjectByStringVal(String value) throws RuntimeException{
        if (StringUtils.isBlank(value)){
            throw new RuntimeException("参数有误!, 系统异常");
        }
        for (BusinessCommonNumber businessCommonNumber : BusinessCommonNumber.values()) {
            if (businessCommonNumber.getValue().equals(value)){
                return businessCommonNumber;
            }
        }
        return null;
    }
}
