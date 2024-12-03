package com.ruoyi.project.business.common.constant.enums;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-26 10:50 - 星期四
 * @package: com.ruoyi.project.business.common.constant
 * @JDK-Version : 1.8
 */
public enum RedisConstantEnum {
    CURRENT_LOGIN_USER_REDIS_NOTICE("currentLoginUserRedisNotice--");
    ;

    private String redisKey;

    RedisConstantEnum(String redisKey) {
        this.redisKey = redisKey;
    }

    public String getRedisKey() {
        return redisKey;
    }
}
