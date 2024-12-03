package com.ruoyi.project.business.common.businessnumber;

import com.ruoyi.project.process.utils.DateUtil;
import org.springframework.lang.Nullable;

import java.util.Objects;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-09 16:31 - 星期三
 * @package: com.ruoyi.project.business.common
 * @JDK-Version : 1.8
 */
public enum  BusinessCommonNumber {
    DT("DT"),
    CJ("CJ"),
    QZ("QZ"),
    ZJ("ZJ"),
    KJ("KJ"),
    QT("QT");

    private String value;

    BusinessCommonNumber(String value) {
        this.value = value;
    }
    /**
     * @author xqh, 987072248@qq.com
     * @date 2021/6/11 21:47
     * @param type
     * @param contractNumber
     * @param number
     * @return {@link String}
     */
    public String installCommonNumber(String type,@Nullable String contractNumber,String number,String dataPrefix) throws RuntimeException{
        String prefix = "";
        String suffix = "";
        StringBuilder resultNumber = new StringBuilder("");

        if (Objects.equals("contract",type)){
            prefix = getContractNumberPrefix(dataPrefix);
            suffix = getContractNumberSuffix();
        }else if (Objects.equals("report",type)){
            prefix = "";
        }else {
            throw new RuntimeException("系统异常!!");
        }
        resultNumber.append(prefix).append(handleNumber(number)).append(suffix);

        return resultNumber.toString();
    }


    /**
     *  获取 合同编号前缀信息
     * @return
     */
    private String getContractNumberPrefix(String dataPrefix){
        return dataPrefix + BusinessConstant.NUMBER_TYPE_MIDDLE + BusinessConstant.COMMON_SUFFIX + value + BusinessConstant.COMMON_SUFFIX;
    }
    private String getContractNumberSuffix(){
        String year = DateUtil.formatDate(DateUtil.getDateTime(), "yyyy");
        return BusinessConstant.COMMON_SUFFIX + year;
    }
    /**
     *  对 取出的编号进行 补位处理方法
     * @param number
     * @return
     */
    private String handleNumber(String number){
        int numMaxBit = number.length();    // 1 10 100 1000    0001 0010 100 999
        if (numMaxBit > 3){                 // 999 以上不需要进行处理
            return number;
        }
        String prefix = "";
        int autoCompletionProcess = 3 - numMaxBit;  // 自动补全字符串的个数

        if (autoCompletionProcess == 0){
            return prefix + number;
        }
        else if (autoCompletionProcess == 1){
            prefix = BusinessConstant.FORMAT_NUMBER_S;
        }else if (autoCompletionProcess == 2){
            prefix = BusinessConstant.FORMAT_NUMBER_G;
        }

        return prefix + number;
    }


    public String getValue() {
        return value;
    }
}
