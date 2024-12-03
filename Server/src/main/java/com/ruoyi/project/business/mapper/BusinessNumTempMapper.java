package com.ruoyi.project.business.mapper;

import com.ruoyi.project.business.domain.BusinessNumTemp;
import com.ruoyi.project.template.commons.mybatis.base.mapper.SuperMapper;

import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-10 11:27 - 星期四
 * @package: com.ruoyi.project.business.mapper
 * @JDK-Version : 1.8
 */
public interface BusinessNumTempMapper extends SuperMapper<BusinessNumTemp> {
    /**
     *  获取 该类型 所有的临时数据
     * @param type
     * @return
     */
    Integer selectMaxBusinessNumByType(String type);

    List<Integer> selectTableListByType(String type);
}
