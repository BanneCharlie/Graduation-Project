package com.ruoyi.project.business.controller.common;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.business.common.businessnumber.BusinessCommonNumber;
import com.ruoyi.project.business.common.businessnumber.BusinessCommonNumberHandler;
import com.ruoyi.project.business.common.businessnumber.BusinessConstant;
import com.ruoyi.project.business.service.BusinessNumTempService;
import com.ruoyi.project.system.domain.SysDictData;
import com.ruoyi.project.system.service.ISysDictDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-09 16:28 - 星期三
 * @package: com.ruoyi.project.business.controller.common
 * @JDK-Version : 1.8
 */
@RestController
@RequestMapping("/business/common")
public class BusinessCommonController {

    private BusinessNumTempService businessNumTempService;
    private ISysDictDataService iSysDictDataService;
    private static Logger logger = LoggerFactory.getLogger(BusinessCommonController.class);

    @Autowired
    public BusinessCommonController(BusinessNumTempService businessNumTempService,
                                    ISysDictDataService iSysDictDataService) {
        this.businessNumTempService = businessNumTempService;
        this.iSysDictDataService = iSysDictDataService;
    }

    /**
     * 从数据库中 对比 并获取 合同编号
     * @param deviceType 设备类型
     * @return
     */
    @PostMapping("/contract/{deviceType}")
    public AjaxResult getRequestCommonNumber(
            @PathVariable String deviceType
    ){
        BusinessCommonNumber enumObjectByStringVal = BusinessCommonNumberHandler.getEnumObjectByStringVal(deviceType);
        if (enumObjectByStringVal == null){
            return AjaxResult.error("系统异常,找不到对应枚举键值对");
        }
        Map<String,Object> resultMap = new HashMap<>(2);
        // 生成 数字编号
        Integer number = businessNumTempService.saveCurrentOperationNumberToRedis(deviceType);
        // 组装 合同编号
        String dataPrefix = "";
        SysDictData dictData = new SysDictData();
        dictData.setDictType(BusinessConstant.BUSINESSTYPE_DICT_DATA_KEY);
        List<SysDictData> sysDictData = iSysDictDataService.selectDictDataList(dictData);
        for (SysDictData sysDictDatum : sysDictData) {
            if (sysDictDatum.getDictValue().contains(deviceType)){
                dataPrefix = sysDictDatum.getDictValue();
                break;
            }
        }
        dataPrefix = dataPrefix.split("/")[0];
        String contractNumber = enumObjectByStringVal.installCommonNumber(BusinessConstant.NUMBER_TYPE_CONTRACT, null, number+"",dataPrefix);
        resultMap.put("number",number);
        resultMap.put("contractNumber",contractNumber);
        logger.info("--redis取号成功--" + contractNumber);
        return AjaxResult.success("redis取号成功",resultMap);
    }
    /**
     * --->
     *  处理当前 用户已经拥有的编号
     * @author xqh, 987072248@qq.com
     * @date 2021/6/16 10:16
     * @param deviceType 接受参数 ， 当前设备类型
     * @param ajaxNumber 接收参数 ， 当前用户已持有的编号
     * @return {@link AjaxResult}
     */
    @PostMapping("/handleCurrentNumber/{deviceType}/{ajaxNumber}")
    public AjaxResult handleCurrentNumberMethod(
            @PathVariable String deviceType,
            @PathVariable String ajaxNumber
    ){
        if (businessNumTempService.handleCurrentCancelNumber(deviceType,ajaxNumber)){
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }
}
