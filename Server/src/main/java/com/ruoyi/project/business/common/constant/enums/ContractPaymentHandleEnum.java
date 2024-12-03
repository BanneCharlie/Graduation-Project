package com.ruoyi.project.business.common.constant.enums;

import java.util.Arrays;

/**
 * ---> 合同付费处理枚举
 *
 * @author xqh , 987072248@qq.com
 * @data 2022-04-22 11:56:12
 * @package com.ruoyi.project.business.common.constant.enums
 */
public enum ContractPaymentHandleEnum {
    /** 年终缴费 */
    YEAR_END_PAYMENT("年终缴费" , "0"){
        @Override
        public boolean checkPaymentAccept(boolean paymentFlag , boolean confirmPrint) {
            return confirmPrint;
        }
    },
    /** 取报告缴费 */
    TAKE_REPORT_PAYMENT("取报告缴费" , "1"){
        @Override
        public boolean checkPaymentAccept(boolean paymentFlag, boolean confirmPrint) {
            return paymentFlag;
        }
    },
    /** 签合同缴费 */
    CONTRACT_PAYMENT("签合同缴费" , "2"){
        @Override
        public boolean checkPaymentAccept(boolean paymentFlag, boolean confirmPrint) {
            return paymentFlag;
        }
    }


    ;


    private final String paymentCategoryLabel;
    private final String paymentCode;

    ContractPaymentHandleEnum(String paymentCategoryLabel, String paymentCode) {
        this.paymentCategoryLabel = paymentCategoryLabel;
        this.paymentCode = paymentCode;
    }


    public String getPaymentCategoryLabel() {
        return paymentCategoryLabel;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public boolean checkPaymentAccept(boolean paymentFlag , boolean confirmPrint){
        throw new AbstractMethodError();
    }

    public static boolean resolveHandlePayment(String paymentType , int isPaymentSuccess , int confirmPrint){

        ContractPaymentHandleEnum paymentHandleEnum = Arrays.stream(values())
                .filter(v -> v.getPaymentCode().equals(paymentType))
                .findFirst()
                .orElseThrow(RuntimeException::new);

        return paymentHandleEnum.checkPaymentAccept(isPaymentSuccess == 1 , confirmPrint == 1);
    }
}
