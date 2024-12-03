package com.ruoyi.project.business.common.businessnumber;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_31 _ 14:14__星期一
 * @package: com.ruoyi.project.business.common
 * @JDK-Version : 1.8
 */
public interface BusinessConstant {

    int FILE_SIZE_MAX = 1024 * 10;

    String NUMBER_TYPE_CONTRACT = "contract";

    String NUMBER_TYPE_REPORT = "report";

    String CONTRACT_PREFIX = "NSEl/HT";

    String COMMON_SUFFIX = "-";

    String NUMBER_TYPE_MIDDLE = "/HT";

    String FORMAT_NUMBER_G = "00";

    String FORMAT_NUMBER_S = "0";

//    --------------- Redis Cache Number Pool Key
    String REDIS_NUMBER_POOL_KEY = "business_number_pool";

//    --------------- SysDictData Key
    String BUSINESSTYPE_DICT_DATA_KEY = "business_contract_number";
}
