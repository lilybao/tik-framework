package com.jeeadmin.controller;


import com.jeeadmin.api.ISysLogService;
import com.jeeadmin.entity.SysLog;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.page.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author wangcy
 * @since 2018-11-12
 */
@Controller
@RequestMapping("/sys/log")
@Api(value = "系统日志", tags = "系统日志")
public class SysLogController extends BaseController {
    @Autowired
    ISysLogService sysLogService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ApiOperation(value = "获取日志列表", notes = "获取日志列表")
    public ResultData list(@RequestBody PageHelper<SysLog> pageHelper) {
        return this.success(sysLogService.selectPage(pageHelper));
    }
}
