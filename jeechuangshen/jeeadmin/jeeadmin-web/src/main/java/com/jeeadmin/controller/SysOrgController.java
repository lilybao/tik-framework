package com.jeeadmin.controller;


import com.jeeadmin.api.ISysOrgService;
import com.jeeadmin.entity.SysOrg;
import com.jeerigger.common.annotation.Log;
import com.jeerigger.common.enums.LogTypeEnum;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
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
 * 组织机构表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/sys/org")
@Api(value = "组织机构管理", tags = "组织机构管理")
public class SysOrgController extends BaseController {

    @Autowired
    private ISysOrgService sysOrgService;

    @ResponseBody
    @RequestMapping(value = "/selectChild", method = RequestMethod.POST)
    @ApiOperation(value = "查询下级组织机构列表", notes = "查询下级组织机构列表")
    public ResultData selectChild(@SingleRequestBody(value = "orgUuid") String orgUuid) {
        return this.success(sysOrgService.selectChildOrg(orgUuid));
    }

    @ResponseBody
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    @ApiOperation(value = "查询组织机构列表", notes = "查询组织机构列表")
    public ResultData selectAll(@RequestBody SysOrg sysOrg) {
        return this.success(sysOrgService.selectOrgList(sysOrg));
    }


    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查看组织机构详细信息", notes = "查看组织机构详细信息")
    public ResultData detail(@SingleRequestBody(value = "orgUuid") String orgUuid) {
        return this.success(sysOrgService.detailOrg(orgUuid));
    }

    @ResponseBody
    @RequiresPermissions("sys:org:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增组织机构信息", notes = "新增组织机构信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "新增组织机构")
    public ResultData save(@RequestBody SysOrg sysOrg) {
        if (sysOrgService.saveSysOrg(sysOrg)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "新增组织机构信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:org:edit")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新组织机构信息", notes = "更新组织机构信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "更新组织机构信息")
    public ResultData update(@RequestBody SysOrg sysOrg) {
        if (sysOrgService.updateSysOrg(sysOrg)) {
            return this.success();
        } else {
            return this.success("error", "更新组织机构信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:org:status")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "更新组织机构状态", notes = "更新组织机构状态")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "更新组织机构状态")
    public ResultData updateStatus(@RequestBody SysOrg sysOrg) {
        if (sysOrgService.updateStatus(sysOrg)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "更新组织机构状态失败！");
        }
    }


    @ResponseBody
    @RequiresPermissions("sys:org:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除组织机构", notes = "删除组织机构")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "删除组织机构")
    public ResultData delete(@SingleRequestBody(value = "orgUuid") String orgUuid) {
        if (sysOrgService.deleteSysOrg(orgUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_DELETE_FAIL, "删除组织机构失败！");
        }
    }

}
