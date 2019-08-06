package com.jeeadmin.controller;


import com.jeeadmin.api.ISysAdminUserService;
import com.jeeadmin.entity.SysAdminUser;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.resolver.annotation.SingleRequestBody;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 系统管理员信息表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/sys/adminUser")
@Api(value = "系统管理员", tags = "系统管理员")
public class SysAdminUserController extends BaseController {
    @Autowired
    private ISysAdminUserService sysAdminUserService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取系统管理员列表", notes = "获取系统管理员列表")
    public ResultData list(@RequestBody PageHelper<SysAdminUser> pageHelper) {
        return this.success(sysAdminUserService.selectPage(pageHelper));
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查看系统管理员详细信息", notes = "查看系统管理员详细信息")
    public ResultData detail(@SingleRequestBody(value = "userUuid") String userUuid) {
        return this.success(sysAdminUserService.getById(userUuid));
    }

    @ResponseBody
    @RequiresPermissions("sys:sysAdmin:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增系统管理员信息", notes = "新增系统管理员信息")
    public ResultData add(@RequestBody SysAdminUser sysAdminUser) {
        if (sysAdminUserService.saveAdminUser(sysAdminUser)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "新增系统管理员信息！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:sysAdmin:edit")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新系统管理员信息", notes = "更新系统管理员信息")
    public ResultData update(@RequestBody SysAdminUser sysAdminUser) {
        if (sysAdminUserService.updateAdminUser(sysAdminUser)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "更新系统管理员信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:sysAdmin:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除系统管理员", notes = "删除系统管理员")
    public ResultData delete(@SingleRequestBody(value = "userUuid") String userUuid) {
        if (sysAdminUserService.removeById(userUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_DELETE_FAIL, "删除系统管理员失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:sysAdmin:status")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "修改系统管理员状态", notes = "修改系统管理员状态")
    public ResultData updateStatus(@RequestBody SysAdminUser sysAdminUser) {
        if (sysAdminUserService.updateAdminUserStatus(sysAdminUser)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "修改系统管理员状态失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:sysAdmin:resetPwd")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码", notes = "重置密码")
    public ResultData resetPwd(@SingleRequestBody(value = "userUuid") String userUuid) {
        if (sysAdminUserService.resetPwd(userUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "重置密码失败！");
        }
    }

}
