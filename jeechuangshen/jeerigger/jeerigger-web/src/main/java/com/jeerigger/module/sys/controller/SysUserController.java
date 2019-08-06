package com.jeerigger.module.sys.controller;

import com.jeerigger.common.annotation.Log;
import com.jeerigger.common.enums.LogTypeEnum;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.page.PageHelper;
import com.jeerigger.frame.support.resolver.annotation.SingleRequestBody;
import com.jeerigger.module.sys.api.ISysUserService;
import com.jeerigger.module.sys.entity.SysUser;
import com.jeerigger.module.sys.vo.AssignUserRoleVo;
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
 * 用户信息表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/sys/user")
@Api(value = "user", tags = "用户信息维护")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService sysUserService;

    @ResponseBody
    @RequestMapping(value = "/orgList", method = RequestMethod.POST)
    @ApiOperation(value = "获取组织机构管理员 可管理的组织机构", notes = "获取组织机构管理员 可管理的组织机构")
    public ResultData orgList() {
        return this.success(sysUserService.selectOrgAdminOrgList());
    }


    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户信息列表", notes = "获取用户信息列表")
    public ResultData selectUserList(@RequestBody PageHelper<SysUser> pageHelper) {
        return this.success(sysUserService.selectPage(pageHelper));
    }


    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户详细信息", notes = "通过主键查询用户信息")
    public ResultData detailUserInfo(@SingleRequestBody(value = "userUuid") String userUuid) {
        return this.success(sysUserService.detailUserInfo(userUuid));
    }

    @ResponseBody
    @RequiresPermissions("sys:user:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增用户信息", notes = "新增用户信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "新增用户信息")
    public ResultData save(@RequestBody SysUser sysUser) {
        if (sysUserService.saveUser(sysUser)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "保存用户信息失败！");
        }
    }


    @ResponseBody
    @RequiresPermissions("sys:user:edit")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "更新用户信息")
    public ResultData updateUser(@RequestBody SysUser sysUser) {
        if (sysUserService.updateUser(sysUser)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "更新用户信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:user:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除用户", notes = "删除用户")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "删除用户")
    public ResultData updateUserByUserCode(@SingleRequestBody(value = "userUuid") String userUuid) {
        if (sysUserService.deleteUser(userUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_DELETE_FAIL, "删除用户失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:user:status")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "更新用户状态", notes = "根据主键更新(必须传主键)")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "更新用户状态")
    public ResultData updateStatus(@RequestBody SysUser sysUser) {
        if (sysUserService.updateUserStatus(sysUser)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "用户状态更新失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:user:resetPwd")
    @RequestMapping(value = "/reset", method = RequestMethod.POST)
    @ApiOperation(value = "重置用户密码", notes = "根据主键重置用户密码")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "重置用户密码")
    public ResultData resetUserPassword(@SingleRequestBody(value = "userUuid") String userUuid) {
        if (sysUserService.resetUserPassword(userUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "重置用户密码失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/roleList", method = RequestMethod.POST)
    @ApiOperation(value = "查看用户已分配和未分配角色列表", notes = "查看用户已分配和未分配角色列表")
    public ResultData selectUserRoleList(@SingleRequestBody(value = "userUuid") String userUuid) {
        return this.success(sysUserService.selectUserRoleList(userUuid));
    }




    @ResponseBody
    @RequestMapping(value = "/authRole", method = RequestMethod.POST)
    @RequiresPermissions("sys:user:authRole")
    @ApiOperation(value = "用户分配角色", notes = "用户分配角色")
    public ResultData authRole(@RequestBody AssignUserRoleVo assignRoleVo) {
        if (sysUserService.assignRole(assignRoleVo)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "用户分配角色失败！");
        }
    }
}
