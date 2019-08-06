package com.jeeadmin.controller;


import com.jeeadmin.api.ISysPostService;
import com.jeeadmin.entity.SysPost;
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
 * 岗位表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2019-01-22
 */
@Controller
@RequestMapping("/sys/post")
@Api(value = "岗位管理", tags = "岗位管理")
public class SysPostController extends BaseController {

    @Autowired
    private ISysPostService sysPostService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取岗位列表", notes = "获取岗位列表")
    public ResultData list(@RequestBody PageHelper<SysPost> pageHelper) {
        return this.success(sysPostService.selectPage(pageHelper));
    }

    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.POST)
    @ApiOperation(value = "查看岗位详细信息", notes = "查看岗位详细信息")
    public ResultData detail(@SingleRequestBody(value = "postUuid") String postUuid) {
        return this.success(sysPostService.getById(postUuid));
    }

    @ResponseBody
    @RequiresPermissions("sys:post:status")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST)
    @ApiOperation(value = "修改岗位状态", notes = "修改岗位状态")
    public ResultData updateStatus(@RequestBody SysPost sysPost) {
        if (sysPostService.updateStatus(sysPost.getUuid(), sysPost.getPostStatus())) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "修改岗位状态失败！");
        }
    }


    @ResponseBody
    @RequiresPermissions("sys:post:add")
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ApiOperation(value = "新增岗位信息", notes = "新增岗位信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "新增岗位信息")
    public ResultData save(@RequestBody SysPost sysPost) {
        if (sysPostService.saveSysPost(sysPost)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_SAVE_FAIL, "新增岗位信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:post:edit")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ApiOperation(value = "更新岗位信息", notes = "更新岗位信息")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "更新岗位信息")
    public ResultData update(@RequestBody SysPost sysPost) {
        if (sysPostService.updateSysPost(sysPost)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_UPDATE_FAIL, "更新岗位信息失败！");
        }
    }

    @ResponseBody
    @RequiresPermissions("sys:post:delete")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ApiOperation(value = "删除岗位", notes = "删除岗位")
    @Log(logType = LogTypeEnum.SYSTEM, logTitle = "删除角色")
    public ResultData delete(@SingleRequestBody(value = "postUuid") String postUuid) {
        if (sysPostService.deleteSysPost(postUuid)) {
            return this.success();
        } else {
            return this.failed(ResultCodeEnum.ERROR_DELETE_FAIL, "删除岗位信息失败！");
        }
    }
}
