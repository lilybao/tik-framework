package com.jeerigger.module.sys.controller;

import java.util.List;

import com.jeerigger.activiti.service.IWorkflowService;
import com.jeerigger.activiti.vo.OutgoingVo;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.module.sys.api.ISysUserService;
import com.jeerigger.module.sys.vo.UpdateUserInfoVo;
import com.jeerigger.module.sys.vo.UpdateUserPwdVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/person")
@Api(value = "个人中心", tags = "个人中心")
public class PersonController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

//    @Autowired
//    private IWorkflowService workflowService;

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ApiOperation(value = "查看个人信息", notes = "查看个人信息")
    public ResultData userInfo() {
        return this.success(sysUserService.getById(ShiroUtil.getUserUuid()));
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ApiOperation(value = "修改个人密码", notes = "修改个人密码")
    public ResultData changePwd(@RequestBody UpdateUserPwdVo changePwdVo) {
        boolean changeFlag = sysUserService.changePassword(changePwdVo);
        if (changeFlag) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "修改密码失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    @ApiOperation(value = "更新个人信息", notes = "更新个人信息")
    public ResultData updateUserInfo(@RequestBody UpdateUserInfoVo updateUserInfoVo) {
        return this.success(sysUserService.updateUserInfo(updateUserInfoVo));
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    @ApiOperation(value = "获取个人菜单", notes = "获取个人菜单")
    public ResultData userMenu() {
        return this.success(sysUserService.getUserMenu(ShiroUtil.getUserUuid()));
    }





}
