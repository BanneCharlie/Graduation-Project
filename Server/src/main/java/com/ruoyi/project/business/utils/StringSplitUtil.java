package com.ruoyi.project.business.utils;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_06_02 _ 11:36__星期三
 * @package: com.ruoyi.project.business.utils
 * @JDK-Version : 1.8
 */
public class StringSplitUtil {

    public static String trimSplitAppend(String src){
        if (src.endsWith(",")){
            return src.substring(0,src.length()-1);
        }
        return src;
    }
}
