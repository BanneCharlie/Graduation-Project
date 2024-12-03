package com.ruoyi.project.business.controller;

import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.business.service.IRequestStatisticsService;
import com.ruoyi.project.business.vo.ReqStatisticsVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 委托业务查询Controller
 * @author niminui
 * @date 2021/6/30 10:12
 */
@RestController
@RequestMapping("req-statistics")
public class RequestStatisticsController extends BaseController {

    @Resource
    private IRequestStatisticsService iRequestStatisticsService;

    /**
     * 获取委托检验合同申请信息及对应的检验报告申请
     * @param reqStatisticsVo 查询参数
     * @return
     */
    @GetMapping("get-list")
    public TableDataInfo getContractAndReportInfo(ReqStatisticsVo reqStatisticsVo) {
        List<ReqStatisticsVo> list = iRequestStatisticsService.getList(reqStatisticsVo);
        for (ReqStatisticsVo vo : list) {
            String checkUserName = vo.getCheckUserName();
            vo.setCheckUserNameMore(vo.getCheckUserName());
            if (StringUtils.isNotEmpty(checkUserName) && checkUserName.split(",").length > 3) {
                String[] split = checkUserName.split(",");
                vo.setCheckUserName(split[0] + "," + split[1] + "," + split[2] + "...");
            }
        }
        return getDataTable(list);
    }

}
