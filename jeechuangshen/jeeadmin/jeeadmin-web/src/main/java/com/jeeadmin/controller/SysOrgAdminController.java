package com.jeeadmin.controller;

import com.jeeadmin.api.ISysOrgAdminService;
import com.jeeadmin.vo.orgadmin.AssignOrgRoleVo;
import com.jeeadmin.vo.user.QueryUserVo;
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

@Controller
@RequestMapping("/sys/orgAdmin")
@Api(value = "机构管理员", tags = "机构管理员")
public class SysOrgAdminController extends BaseController {
    @Autowired
    private ISysOrgAdminService sysOrgAdminService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "查询机构管理员列表", notes = "查询机构管理员列表")
    public ResultData list(@RequestBody PageHelper<QueryUserVo> pageHelper) {
        return this.success(sysOrgAdminService.selectOrgAdminPage(pageHelper));
    }

    @ResponseBody
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    @ApiOperation(value = "获取用户列表", notes = "获取用户列表")
    public ResultData userList(@RequestBody PageHelper<QueryUserVo> pageHelper) {
        return this.success(sysOrgAdminService.selectUserPage(pageHelper));
    }

    @ResponseBody
    @RequiresPermissions("sys:orgAdmin:add")
    @RequestMapping(value = "/addOrgAdmin", method = RequestMethod.POST)
    @ApiOperation(value = "新增机构管理员", notes = "新增机构管理员")
    public ResultData addOrgAdmin(@SingleRequestBody(value = "userUuid") String userUuid) {
        if (sysOrgAdminService.saveOrgAdmin(userUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "分配组织机构管理员失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:orgAdmin:cancel")
    @RequestMapping(value = "/cancelOrgAdmin", method = RequestMethod.POST)
    @ApiOperation(value = "取消组织机构管理员", notes = "取消组织机构管理员")
    public ResultData cancelOrgAdmin(@SingleRequestBody(value = "userUuid") String userUuid) {
        if (sysOrgAdminService.cancelOrgAdmin(userUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "取消组织机构管理员失败！");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/detailOrgRole", method = RequestMethod.POST)
    @ApiOperation(value = "查看机构管理员 已分配组织机构和已分配的角色", notes = "查看机构管理员 已分配组织机构和已分配的角色")
    public ResultData detailOrgRole(@SingleRequestBody(value = "userUuid") String userUuid) {
        return this.success(sysOrgAdminService.detailOrgRole(userUuid));
    }

    @ResponseBody
    @RequiresPermissions("sys:orgAdmin:assign")
    @RequestMapping(value = "/assignOrgRole", method = RequestMethod.POST)
    @ApiOperation(value = "机构管理员 分配可管理的组织机构和可给用户分配的角色", notes = "机构管理员 分配可管理的组织机构和可给用户分配的角色")
    public ResultData assignOrgRole(@RequestBody AssignOrgRoleVo assignOrgRoleVo) {
        if (sysOrgAdminService.assignOrgRole(assignOrgRoleVo)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "机构管理员 分配可管理的组织机构和可给用户分配的角色失败！");
        }
    }
}
