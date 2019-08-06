package com.jeeadmin.controller;


import com.jeeadmin.api.ISysAreaService;
import com.jeeadmin.entity.SysArea;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * <p>
 * 行政区划表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/sys/area")
@Api(value = "行政区划", tags = "行政区划")
public class SysAreaController extends BaseController {

    @Autowired
    private ISysAreaService sysAreaService;

    @ResponseBody
    @RequestMapping(value = "/selectChild", method = RequestMethod.POST)
    @ApiOperation(value = "获取下级行政区划列表", notes = "获取下级行政区划列表")
    public ResultData selectAllFirstAreas(@SingleRequestBody(value = "areaUuid") String areaUuid) {
        return this.success(sysAreaService.selectChildArea(areaUuid));
    }

    @ResponseBody
    @RequestMapping(value = "/selectAll", method = RequestMethod.POST)
    @ApiOperation(value = "查询行政区划列表", notes = "查询行政区划列表")
    public ResultData selectAreaList(@RequestBody SysArea sysArea) {
        return this.success(sysAreaService.selectAreaList(sysArea));
    }


    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查看行政区划详细信息", notes = "查看行政区划详细信息")
    public ResultData detail(@SingleRequestBody(value = "areaUuid") String areaUuid) {
        return this.success(sysAreaService.getById(areaUuid));
    }

    @ResponseBody
    @RequiresPermissions("sys:area:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增行政区划", notes = "新增行政区划")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "新增行政区划")
    public ResultData add(@RequestBody SysArea sysArea) {
        if (sysAreaService.saveSysArea(sysArea)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "新增行政区划信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:area:edit")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新行政区划信息", notes = "更新行政区划信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "更新行政区划信息")
    public ResultData update(@RequestBody SysArea sysArea) {
        if (sysAreaService.updateSysArea(sysArea)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "更新行政区划信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:area:status")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "更新行政区划状态", notes = "更新行政区划状态")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "更新行政区划状态")
    public ResultData updateStatus(@RequestBody SysArea sysArea) {
        if (sysAreaService.updateStatus(sysArea)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "更新行政区划状态失败！");
        }
    }


    @ResponseBody
    @RequiresPermissions("sys:area:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除行政区划信息", notes = "删除行政区划信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "删除行政区划")
    public ResultData delete(@SingleRequestBody(value = "areaUuid") String areaUuid) {
        if (sysAreaService.deleteSysArea(areaUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "删除行政区划失败！");
        }
    }
}
