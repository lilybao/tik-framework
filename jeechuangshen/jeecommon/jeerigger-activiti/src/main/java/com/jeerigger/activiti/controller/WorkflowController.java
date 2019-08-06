package com.jeerigger.activiti.controller;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/3/26 0026 下午 2:09
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.jeerigger.activiti.enums.ActivitiEnum;
import com.jeerigger.activiti.service.IWorkflowService;
import com.jeerigger.activiti.vo.OutgoingVo;
import com.jeerigger.activiti.vo.TaskVo;
import com.jeerigger.frame.base.controller.BaseController;
import com.jeerigger.frame.base.controller.ResultCodeEnum;
import com.jeerigger.frame.base.controller.ResultData;
import com.jeerigger.frame.page.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: WorkflowController
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/3/26 0026 下午 2:09
 * @version: V1.0
 */
@Controller
@RequestMapping("/workflow")
@Api(value = "工作流API",tags = "工作流API")
public class WorkflowController extends BaseController {

        @Autowired
        private IWorkflowService workflowService;


        @ResponseBody
        @RequestMapping(value = "/deployModel", method = RequestMethod.POST)
        @ApiOperation(value = "部署模型", notes = "部署模型")
        public ResultData getOutgoingsByBusinessKey(MultipartFile file) {
            if(file.isEmpty()){
                return this.failed(ResultCodeEnum.ERROR_PARSE_EXCEPTION,"");
            }
            InputStream inputStream = null;
            try {
                inputStream = file.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String originalFilename = file.getOriginalFilename();
            String  deployId = workflowService.deployByInputStream(file.getOriginalFilename(), inputStream);
            if(StringUtils.isNotEmpty(deployId)){
                return this.success("部署模型成功",deployId);
            }else {
                return  this.failed(ResultCodeEnum.ERROR_WORKFLOW_DEPLOY_EXCEPTION);
            }
        }


        @ResponseBody
        @RequestMapping(value = "/getOutgoingsByBusinessKey", method = RequestMethod.POST)
        @ApiOperation(value = "获取流程走向及走向下的节点信息", notes = "获取流程走向及走向下的节点信息")
        public ResultData getOutgoingsByBusinessKey(String businessKey) {
            if(StringUtils.isEmpty(businessKey)){
                return this.failed(ResultCodeEnum.ERROR_PARSE_EXCEPTION,"业务主键不能为空！");
            }
            List<OutgoingVo> outgoingsByBusinessKey = workflowService.getOutgoingsByBusinessKey(businessKey);
            return this.success(outgoingsByBusinessKey);
        }

        @ResponseBody
        @RequestMapping(value = "/start", method = RequestMethod.POST)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "processDefinitionKey",value = "流程定义key",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "initiator",value = "流程发起人",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "businessKey",value = "业务主键",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "assignee",value = "下个环节审批人ID",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "assigneeName",value = "下个环节审批人名字",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "comments",value = "审批意见",required = false,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "outgoing",value = "流程走向",required = false,dataType = "string",paramType ="query")
        })
        @ApiOperation(value = "流程发起", notes = "流程发起")
        public ResultData getOutgoingsByBusinessKey(String processDefinitionKey,String initiator,String businessKey,String assignee,String assigneeName,String comments,String outgoing) {
            HashMap<String, Object> map = new HashMap<>();
            String processInstanceId = workflowService.start(processDefinitionKey, initiator, businessKey, map);
            int b = workflowService.handleTask(businessKey, assignee, assigneeName, comments, outgoing, map);
            if(1 == b){
                return this.success("流程发起成功");
            }else if(0 == b){
                return this.success("流程发起失败");
            }else {
                return this.success("流程结束");
            }
        }
        @ResponseBody
        @RequestMapping(value = "/completeTask", method = RequestMethod.POST)
        @ApiImplicitParams({
                @ApiImplicitParam(name = "businessKey",value = "业务主键",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "assignee",value = "下个环节审批人ID",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "assigneeName",value = "下个环节审批人名字",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "comments",value = "审批意见",required = false,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "outgoing",value = "流程走向",required = true,dataType = "string",paramType ="query"),
                @ApiImplicitParam(name = "money",value = "金额",required = false,dataType = "string",paramType ="query")
        })
        @ApiOperation(value = "完成审批", notes = "完成审批")
        public ResultData completeTask(String businessKey,String assignee,String assigneeName,String comments,String outgoing,String money) {
            HashMap<String, Object> map = new HashMap<>();
            if(StringUtils.isNotEmpty(money)){
                map.put(ActivitiEnum.MONEY.getVariableType(),money);
            }
            int b = workflowService.handleTask(businessKey, assignee, assigneeName, comments, outgoing, map);
            if(1 == b){
                return this.success("流程发起成功");
            }else if(0 == b){
                return this.success("流程发起失败");
            }else {
                return this.success("流程结束");
            }
        }

        @ResponseBody
        @RequestMapping(value = "/getToDoTaskNum", method = RequestMethod.POST)
        @ApiImplicitParam(name = "assignee",value = "当前用户登录名",required = true,dataType = "string",paramType ="query")
        @ApiOperation(value = "获取当前用户的待办数量", notes = "获取当前用户的待办数量")
        public ResultData getToDoTaskNum(String assignee) {
            int i = workflowService.getToDoTaskNum(assignee);
            return this.success(i);
        }

        @ResponseBody
        @RequestMapping(value = "/searchHistoryTaskByBusinessKey", method = RequestMethod.POST)
        @ApiImplicitParam(name = "businessKey",value = "业务主键",required = true,dataType = "string",paramType ="query")
        @ApiOperation(value = "获取流程审批记录", notes = "获取流程审批记录")
        public ResultData searchHistoryTaskByBusinessKey(String businessKey) {
            List<TaskVo> taskVos = workflowService.searchHistoryTaskByBusinessKey(businessKey);
            return this.success(taskVos);
        }

        @ResponseBody
        @RequestMapping(value = "/getModelByPage", method = RequestMethod.POST)
        @ApiImplicitParam(name = "modelName",value = "模型名称",required = false,dataType = "string",paramType ="query")
        @ApiOperation(value = "获取流程定义列表", notes = "获取流程定义列表")
        public ResultData getModelByPage(PageHelper pageHelper,String modelName) {
            return this.success(workflowService.getModelByPage(pageHelper,modelName));
        }

        @ResponseBody
        @RequestMapping(value = "/isComplete", method = RequestMethod.POST)
        @ApiImplicitParam(name = "businessKey",value = "业务主键",required = true,dataType = "string",paramType ="query")
        @ApiOperation(value = "判断当前流程是否结束", notes = "判断当前流程是否结束")
        public ResultData isComplete(String businessKey) {
            if(workflowService.isComplete(businessKey)){
                return this.success("流程结束",true);
            }else {
                return this.success("流程未结束",false);
            }
        }
        @ResponseBody
        @RequestMapping(value = "/getWorkflowDesc", method = RequestMethod.POST)
        @ApiImplicitParam(name = "workflowName",value = "流程名称",required = true,dataType = "string",paramType ="query")
        @ApiOperation(value = "获取流程说明", notes = "获取流程说明")
        public ResultData getWorkflowDesc(String workflowName) {
            String workflowDesc = workflowService.getWorkflowDesc(workflowName);
            return this.success(workflowDesc);
        }
        @ResponseBody
        @RequestMapping(value = "/getStartFlowAndNodes", method = RequestMethod.POST)
        @ApiImplicitParam(name = "modelKey",value = "模型key",required = true,dataType = "string",paramType ="query")
        @ApiOperation(value = "获取流程发起时下一环节的审批人员信息", notes = "获取流程发起时下一环节的审批人员信息")
        public ResultData getFlowAndNodes(String modelKey) {
            List<OutgoingVo> outgoingsByBusinessKey = workflowService.getStartNodeByBusinessKey(modelKey,"","");
            return this.success(outgoingsByBusinessKey);
        }

        @ResponseBody
        @RequestMapping(value = "/trackProcess", method = RequestMethod.POST)
        @ApiImplicitParam(name = "businessKey",value = "业务主键",required = true,dataType = "string",paramType ="query")
        @ApiOperation(value = "获取流程跟踪图片", notes = "获取流程跟踪图片")
        public ResultData trackProcess(String businessKey,HttpServletResponse response) {
            InputStream imageStream = workflowService.getResourceDiagramInputStream(businessKey);
            response.setContentType("image/png;chartset=utf-8");
            response.setCharacterEncoding("utf-8");
            byte[] b = new byte[1024];
            int len;
            try {
                while((len=imageStream.read(b,0,1024))!=-1){
                    response.getOutputStream().write(b,0,len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

    @ResponseBody
    @RequestMapping(value = "/downloadModel", method = RequestMethod.POST)
    @ApiImplicitParam(name = "businessKey",value = "业务主键",required = true,dataType = "string",paramType ="query")
    @ApiOperation(value = "下载模型", notes = "下载模型")
    public ResultData downloadModel(String businessKey,HttpServletResponse response) {
        InputStream imageStream = workflowService.getResourceDiagramInputStream(businessKey);
        response.setContentType("image/png;chartset=utf-8");
        response.setCharacterEncoding("utf-8");
        byte[] b = new byte[1024];
        int len;
        try {
            while((len=imageStream.read(b,0,1024))!=-1){
                response.getOutputStream().write(b,0,len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
