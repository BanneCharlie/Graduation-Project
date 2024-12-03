package com.ruoyi.project.template.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import com.ruoyi.project.template.domain.DataSourceConfig;
import com.ruoyi.project.template.mapper.DataSourceConfigMapper;
import com.ruoyi.project.template.service.DataSourceConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author niminui
 * @date 2020/11/20 15:56
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataSourceConfigServiceImpl extends BaseServiceImpl<DataSourceConfigMapper, DataSourceConfig> implements DataSourceConfigService {

    @Resource
    private DataSourceConfigMapper dataSourceConfigMapper;

    private static final String[] SHOW_ARRAY = new String[]
            {
                    // reqConReview
                    "entrustUnit", "typeUnit", "useUnit",
                    "liaisons", "phone", "zipCode",
                    "contractNumber",
                    // device
                    "deviceNumber", "deviceName", "checkDept",
                    "orderTime"

            };
    private static final String[] SHOW_ARRAY_VAL = new String[]
            {
                    // reqConReview
                    "委托单位", "单位类型", "使用单位",
                    "联系人", "联系电话", "邮编",
                    "合同编号",
                    // device
                    "报告编号", "设备名称", "检修单位",
                    "检修日期"
            };

    static {
        if (SHOW_ARRAY.length != SHOW_ARRAY_VAL.length) {
            try {
                throw new Exception("参数初始化异常!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<DataSourceConfig> getList(DataSourceConfig config) {
        QueryWrapper<DataSourceConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .like(StringUtils.isNotEmpty(config.getDataSourceName()), DataSourceConfig::getDataSourceName, config.getDataSourceName());
        return dataSourceConfigMapper.selectList(queryWrapper);
    }

    @Override
    public int deleteConfigByIds(String[] rowIds) {
        int res = 1;
        try {
            for (String id : rowIds) {
                dataSourceConfigMapper.deleteById(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            res = 0;
        }
        return res;
    }

    @Override
    public List<Map<String, String>> getReqRepFields() throws Exception {
        List<Map<String, String>> resultFieldList = new ArrayList<>();
        Map<String, String> resultFieldMap;
        for (int i = 0, maxLength = SHOW_ARRAY.length; i < maxLength; i++) {
            resultFieldMap = new HashMap<>();
            resultFieldMap.put("keyLabel", SHOW_ARRAY_VAL[i]);
            resultFieldMap.put("keyValue", SHOW_ARRAY[i]);
            resultFieldList.add(resultFieldMap);
        }

        return resultFieldList;


//        List<Class<?>> operateClassList = new ArrayList<>();
//        operateClassList.add(ReqContReview.class);
//        operateClassList.add(BusinessDevice.class);
//
////        -----------   init -------
//
//        List<String> resultFields = new ArrayList<>();
//        for (Class<?> itemClass : operateClassList) {
//            Field[] declaredFields = itemClass.getDeclaredFields();
//            for (Field declaredField : declaredFields) {
//                declaredField.setAccessible(true);
//                final String fieldName = declaredField.getName();
//                if (!declaredField.isAnnotationPresent(TableField.class) && StringUtils.checkTargetValueContainsArrays(fieldName,valArray)){
//                    resultFields.add(fieldName);
//                }
//            }
//        }

    }
}
