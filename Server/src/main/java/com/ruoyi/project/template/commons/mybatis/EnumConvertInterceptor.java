package com.ruoyi.project.template.commons.mybatis;

public interface EnumConvertInterceptor {
    boolean convert(EntityMap map, String key, Object v);
}
