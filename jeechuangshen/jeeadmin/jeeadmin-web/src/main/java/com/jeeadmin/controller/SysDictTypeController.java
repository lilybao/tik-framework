package com.jeeadmin.controller;


import com.jeeadmin.api.ISysDictTypeService;
import com.jeeadmin.entity.SysDictType;
import com.jeerigger.common.annotation.Log;
import com.jeerigger.common.enums.LogTypeEnum;
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
 * 字典类型表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/sys/dictType")
@Api(value = "字典类型管理", tags = "字典类型管理")
public class SysDictTypeController extends BaseController {
    @Autowired
    ISysDictTypeService sysDictTypeService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取字典类型列表", notes = "获取字典类型列表")
    public ResultData list(@RequestBody PageHelper<SysDictType> pageHelper) {
        return this.success(sysDictTypeService.selectPage(pageHelper));
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查看字典类型详细信息", notes = "查看字典类型详细信息")
    public ResultData detail(@SingleRequestBody(value = "dictUuid") String dictUuid) {
        return this.success(sysDictTypeService.getById(dictUuid));
    }


    @ResponseBody
    @RequiresPermissions("sys:dict:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增字典类型数据", notes = "新增字典类型数据")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "新增字典类型数据")
    public ResultData save(@RequestBody SysDictType sysDictType) {
        if (sysDictTypeService.saveDictType(sysDictType)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "新增字典类型数据失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:dict:status")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "修改字典类型状态", notes = "修改字典类型状态")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "修改字典类型状态")
    public ResultData updateStatus(@RequestBody SysDictType sysDictType) {
        if (sysDictTypeService.updateStatus(sysDictType)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "修改字典类型状态失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:dict:edit")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "修改字典类型数据", notes = "修改字典类型数据")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "修改字典类型数据")
    public ResultData update(@RequestBody SysDictType sysDictType) {
        if (sysDictTypeService.updateDictType(sysDictType)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "修改字典类型数据失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:dict:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除字典类型", notes = "删除字典类型")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "删除字典类型")
    public ResultData delete(@SingleRequestBody(value = "dictUuid") String dictUuid) {
        if (sysDictTypeService.deleteDictType(dictUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_DELETE_FAIL, "删除字典类型失败");
        }
    }
}
