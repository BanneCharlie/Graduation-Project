package com.ruoyi.project.template.util;

import java.lang.reflect.InvocationHandler;
import java.util.Map;
import java.util.Set;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_24 _ 16:30__星期一
 * @package: com.ruoyi.project.template.util
 * @JDK-Version : 1.8
 */
public class RuoYiRequestParam {
    
    public static void stdoutRequestBodyParam(Map<String,Object> paramMap){
        Set<Map.Entry<String, Object>> entries = paramMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            System.out.println(entry.getKey() + "==>" + entry.getValue());
        }
    }
}
