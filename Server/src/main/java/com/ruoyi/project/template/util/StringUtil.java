package com.ruoyi.project.template.util;

import cn.hutool.core.lang.UUID;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    /**
     * 验证字符串是否为空(true 不为空；false表示为空)
     *
     * @param str
     * @return
     */
    public static boolean validateStringNotNull(String str) {
        str = StringUtils.trimToNull(str);
        return StringUtils.isNotEmpty(str) && StringUtils.isNotBlank(str);
    }

    /**
     * 某字符串结尾是否含有第二个字符串 where t.rowId = b.roId and
     *
     * @param sqlStr
     * @param chr    ("and ")
     * @return 返回true说明该字符串结尾含有and ，否则不含
     */
    public static boolean endHasAnd(String sqlStr, String chr) {
        return sqlStr.length() - sqlStr.lastIndexOf(chr) <= chr.length();
    }

    /**
     * 截取从A字符串到B字符串之间所有字符
     *
     * @param strBegin 开始A字符串
     * @param strEnd   截止B字符串
     * @param strDest  目标字符串
     * @return
     */
    public static String substringMiddle(String strBegin, String strEnd, String strDest) {
        String results = "";
        Pattern p = Pattern.compile(strBegin + "(.*)" + strEnd);
        Matcher m = p.matcher(strDest);
        while (m.find()) {
            results = m.group(1);
        }
        return results;
    }

    /*	public static String substringMiddle(String strBegin, String strEnd, String strDest) {
            String results = "";
            Pattern p = Pattern.compile(strBegin + "(.*)" + strEnd);
            Matcher m = p.matcher(strDest);
            while (m.find()) {
                results = m.group(1);
            }
            return results;
        }*/

    /**
     * 生成uuid 唯一标识
     */

    public static String fastSimpleUUID() {
        return UUID.fastUUID().toString(true);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString
     *            原始字符串
     * @param length
     *            指定长度
     * @return
     */
    public static List<String> getStrList(String inputString, int length) {
        int size = inputString.length() / length;
        if (inputString.length() % length != 0) {
            size += 1;
        }
        return getStrList(inputString, length, size);
    }

    /**
     * 把原始字符串分割成指定长度的字符串列表
     *
     * @param inputString
     *            原始字符串
     * @param length
     *            指定长度
     * @param size
     *            指定列表大小
     * @return
     */
    public static List<String> getStrList(String inputString, int length,
                                          int size) {
        List<String> list = new ArrayList<String>();
        for (int index = 0; index < size; index++) {
            String childStr = substring(inputString, index * length,
                    (index + 1) * length);
            list.add(childStr);
        }
        return list;
    }

    /**
     * 分割字符串，如果开始位置大于字符串长度，返回空
     *
     * @param str
     *            原始字符串
     * @param f
     *            开始位置
     * @param t
     *            结束位置
     * @return
     */
    public static String substring(String str, int f, int t) {
        if (f > str.length())
            return null;
        if (t > str.length()) {
            return str.substring(f, str.length());
        } else {
            return str.substring(f, t);
        }
    }
}
