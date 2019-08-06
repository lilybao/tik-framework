package com.jeeadmin.controller;

import com.jeeadmin.api.ISysMonitorService;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sys/monitor")
@Api(value = "系统监控", tags = "系统监控")
public class SysMonitorController extends BaseController {
    @Autowired
    private ISysMonitorService monitorService;

    @ResponseBody
    @RequestMapping(value = "/msaOrgUser", method = RequestMethod.POST)
    @ApiOperation(value = "部门人员占比分析", notes = "部门人员占比分析")
    public ResultData msaOrgUser() {
        return this.success(monitorService.selectMsaOrgUser());
    }

    @ResponseBody
    @RequestMapping(value = "/msaRoleUser", method = RequestMethod.POST)
    @ApiOperation(value = "角色人员占比分析", notes = "角色人员占比分析")
    public ResultData msaRoleUser() {
        return this.success(monitorService.selectMsaRoleUser());
    }

    @ResponseBody
    @RequestMapping(value = "/systemInfo", method = RequestMethod.POST)
    @ApiOperation(value = "系统信息", notes = "系统信息")
    public ResultData systemInfo() {
        return this.success(monitorService.getSystemInfo());
    }

    @ResponseBody
    @RequestMapping(value = "/diskInfo", method = RequestMethod.POST)
    @ApiOperation(value = "磁盘信息", notes = "磁盘信息")
    public ResultData diskInfo() {
        return this.success(monitorService.getDiskListInfo());
    }

    @ResponseBody
    @RequestMapping(value = "/serverInfo", method = RequestMethod.POST)
    @ApiOperation(value = "系统服务状态", notes = "系统服务状态")
    public ResultData serverInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("cpu", monitorService.getCpuInfo());
        map.put("jvm", monitorService.getJvmInfo());
        map.put("mem", monitorService.getMemInfo());
        return this.success(map);
    }

    @ResponseBody
    @RequestMapping(value = "/usedPerc", method = RequestMethod.POST)
    @ApiOperation(value = "系统服务状态 首页使用", notes = "系统服务状态 首页使用")
    public ResultData cpuUsedPerc() {
        Map<String, Object> map = new HashMap<>();
        map.put("cpu", monitorService.getCpuInfo().get("usedPerc"));
        map.put("mem", monitorService.getMemInfo().get("usedPerc"));
        map.put("dateTime", new Date());
        return this.success(map);
    }

}
