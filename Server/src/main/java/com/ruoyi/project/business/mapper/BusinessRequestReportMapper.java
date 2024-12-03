package com.ruoyi.project.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.business.domain.BusinessRequestReport;
import com.ruoyi.project.business.vo.ReportVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-08-05 14:57 - 星期四
 * @package: com.ruoyi.project.business.mapper
 * @JDK-Version : 1.8
 */
public interface BusinessRequestReportMapper extends BaseMapper<BusinessRequestReport> {

    List<ReportVo> getReportVoList(@Param("userId") Long userId , @Param("queryParams")Map<String, String> queryParams , @Param("templateCategory")String templateCategory);
}
