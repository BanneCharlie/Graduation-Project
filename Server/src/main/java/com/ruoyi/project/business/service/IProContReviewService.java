package com.ruoyi.project.business.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruoyi.project.business.domain.ProContReview;
import com.ruoyi.project.template.commons.mybatis.base.service.IBaseService;

import java.util.List;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 13:55__星期五
 * @package: com.ruoyi.project.business.service
 * @JDK-Version : 1.8
 */
public interface IProContReviewService extends IBaseService<ProContReview> {
    public boolean deleteBeanByIds(String[] rowIds);

    public List<ProContReview> getList(ProContReview proContReview);
}
