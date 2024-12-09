package com.ruoyi.project.business.service.impl;

import com.ruoyi.project.business.domain.FileUploadInstance;
import com.ruoyi.project.business.mapper.FileUploadMapper;
import com.ruoyi.project.business.service.IFileUploadService;
import com.ruoyi.project.template.commons.mybatis.base.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/***
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021_05_28 _ 15:22__星期五
 * @package: com.ruoyi.project.business.service.impl
 * @JDK-Version : 1.8
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IFileUploadServiceImpl extends BaseServiceImpl<FileUploadMapper, FileUploadInstance> implements IFileUploadService {
}
