package com.jeeadmin.controller;

import com.jeeadmin.api.ISysAdminUserService;
import com.jeeadmin.entity.SysAdminUser;
import com.jeeadmin.vo.user.UpdatePwdVo;
import com.jeeadmin.vo.user.UpdateUserVo;
import com.jeerigger.common.shiro.ShiroUtil;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
    private ISysAdminUserService sysAdminUserService;

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    @ApiOperation(value = "查看个人信息", notes = "查看个人信息")
    public ResultData<SysAdminUser> userInfo() {
        return this.success(sysAdminUserService.getById(ShiroUtil.getUserUuid()));
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
    @ApiOperation(value = "修改个人密码", notes = "修改个人密码")
    public ResultData changePwd(@RequestBody UpdatePwdVo updatePwdVo) {
        if (sysAdminUserService.changePassword(updatePwdVo)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "修改密码失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    @ApiOperation(value = "更新个人信息", notes = "更新个人信息")
    public ResultData updateUserInfo(@RequestBody UpdateUserVo updateUserVo) {
        return this.success(sysAdminUserService.updateUserInfo(updateUserVo));
    }

    @ResponseBody
    @RequiresPermissions("user")
    @RequestMapping(value = "/menu", method = RequestMethod.POST)
    @ApiOperation(value = "获取个人菜单", notes = "获取个人菜单")
    public ResultData userMenu() {
        return this.success(sysAdminUserService.getSysAdminMenu());
    }

}
