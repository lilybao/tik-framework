package com.jeeadmin.controller;


import com.jeeadmin.api.ISysParamService;
import com.jeeadmin.entity.SysParam;
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
 * 系统参数配置表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/sys/param")
@Api(value = "系统参数管理", tags = "系统参数管理")
public class SysParamController extends BaseController {
    @Autowired
    private ISysParamService sysParamService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取系统参数列表", notes = "获取系统参数列表")
    public ResultData list(@RequestBody PageHelper<SysParam> pageHelper) {
        return this.success(sysParamService.selectPage(pageHelper));
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "获取系统参数详细信息", notes = "获取系统参数详细信息")
    public ResultData detail(@SingleRequestBody(value = "paramUuid") String paramUuid) {
        return this.success(sysParamService.getById(paramUuid));
    }

    @ResponseBody
    @RequiresPermissions("sys:param:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增系统参数信息", notes = "新增系统参数信息")
    public ResultData save(@RequestBody SysParam param) {
        if (sysParamService.saveSysParam(param)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "新增系统参数信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:param:edit")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新系统参数信息", notes = "更新系统参数信息")
    public ResultData update(@RequestBody SysParam param) {
        if (sysParamService.updateSysParam(param)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "更新系统参数信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:param:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除系统参数", notes = "删除系统参数")
    public ResultData delete(@SingleRequestBody(value = "paramUuid") String paramUuid) {
        if (sysParamService.deleteSysParam(paramUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_DELETE_FAIL, "删除系统参数失败！");
        }
    }
}
