package com.ruoyi.project.system.controller;

import com.ruoyi.framework.security.service.TokenService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.business.utils.FileUtil;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserSignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/***
 * ---->
 *
 * @author xqh, 987072248@qq.com
 * @version 1.0
 * @date: 2021-06-28 10:26 - 星期一
 * @package: com.ruoyi.project.system.controller
 * @JDK-Version : 1.8
 */
@RestController
@RequestMapping("/system/signature/")
public class SysUserSignatureController extends BaseController {
    private FileUtil fileUtil = new FileUtil();
    private TokenService tokenService;
    private ISysUserSignatureService iSysUserSignatureService;
    @Autowired
    public SysUserSignatureController(TokenService tokenService, ISysUserSignatureService iSysUserSignatureService) {
        this.tokenService = tokenService;
        this.iSysUserSignatureService = iSysUserSignatureService;
    }

    /**
     * ---> 获取当前用的电子签名 图片对象 , 分为 使用过和正在使用的图片
     * @author xqh, 987072248@qq.com
     * @date 2021/6/28 10:12
     * @return {@link AjaxResult}
     */
    @PostMapping("getCurrentUserSignature")
    public AjaxResult getCurrentUserSignature(HttpServletRequest request){
        final SysUser currentLoginUser = tokenService.getLoginUser(request).getUser();
        Map<String,Object> resultMap;
        if (currentLoginUser.isAdmin()){        // all signature
            resultMap = iSysUserSignatureService.getCurrentUserSignature(true);
        }else {                                 // currentUserSignture
            final String currentUserName = currentLoginUser.getUserName();
            resultMap = iSysUserSignatureService.getCurrentUserSignature(false,currentUserName);
        }
        return AjaxResult.success(resultMap);
    }

    @DeleteMapping("deleteSignature/{deleteId}")
    public AjaxResult deleteSignature(@PathVariable("deleteId") String deleteId){
        return AjaxResult.success(iSysUserSignatureService.removeById(deleteId) ? "删除成功" : "系统异常");
    }
}
