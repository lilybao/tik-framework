package com.jeerigger.activiti.service.impl;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/3/4 0004 下午 4:54
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */


import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.jws.soap.SOAPBinding;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jeerigger.activiti.dao.WorkFlowMapper;
import com.jeerigger.activiti.enums.ActivitiEnum;
import com.jeerigger.activiti.enums.BpmsActivityTypeEnum;
import com.jeerigger.activiti.service.IWorkflowService;
import com.jeerigger.activiti.utils.DateUtil;
import com.jeerigger.activiti.utils.UtilMisc;
import com.jeerigger.activiti.vo.CandidateVo;
import com.jeerigger.activiti.vo.OutgoingVo;
import com.jeerigger.activiti.vo.TaskVo;
import com.jeerigger.frame.page.PageHelper;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.EndEvent;
import org.activiti.bpmn.model.ExclusiveGateway;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.ParallelGateway;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.StartEvent;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.util.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName: WorkFlowServiceImpl
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/3/4 0004 下午 4:54
 * @version: V1.0
 */
@Service
public class WorkFlowServiceImpl  implements IWorkflowService {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ProcessEngine processEngine;

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private IdentityService identityService;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private WorkFlowMapper workFlowMapper;

    @Override public boolean deploy(String modelId) {
        Model model = repositoryService.getModel(modelId);
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelId));
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(jsonNode);
            String processName = model.getName();
            if(!StringUtils.endsWith(processName,".bpmn20.xml")){
                processName += ".bpmn20.xml";
            }
            //部署流程
            Deployment deployment = repositoryService.createDeployment().name(model.getName()).addBpmnModel(processName, bpmnModel).deploy();
            //设置流程分类
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
            list.stream().forEach(processDefinition -> repositoryService.setProcessDefinitionCategory(processDefinition.getId(),model.getCategory()));
        } catch (Exception e) {
            logger.error("流程模型部署失败",e);
            return false;
        }
        return true;
    }

    @Override public String deployByInputStream(String fileName, InputStream inputStream) {
        String deployId = "";
        try {
            Deployment deploy = repositoryService.createDeployment().addInputStream(fileName, inputStream).deploy();
            if(null != deploy){
                deployId = deploy.getId();
            }
        } catch (Exception e) {
            logger.error("模型部署失败",e);
            return  "";
        }
        return deployId;
    }

    @Override public boolean delete(String modelId) {
        try {
            repositoryService.deleteModel(modelId);
        } catch (Exception e) {
            logger.error("流程模型删除失败",e);
            return false;
        }
        return true;
    }

    @Override public String start(String processDefinitionKey,String initiator,String businessKey,Map<String,Object> variables) {
        ProcessInstance processInstance;
        try {
            // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
            identityService.setAuthenticatedUserId(initiator);
            processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, businessKey, variables);
        } finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstance.getId();
    }



    @Override public int handleTask(String businessKey, String assignee,String assigneeName, String comments, String outgoing, Map<String, Object> variables) {
        try {
            Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
            if(null == task){
                return 0;
            }
            String taskId=task.getId();
            //保存审批意见和流程走向
            taskService.setVariableLocal(taskId,ActivitiEnum.COMMENTS.getVariableType(),comments);
            taskService.setVariableLocal(taskId,ActivitiEnum.PASS.getVariableType(),outgoing);
            //保存其他的流程变量
            variables.forEach((k, v) -> taskService.setVariableLocal(taskId,k,v));
            variables.put(ActivitiEnum.PASS.getVariableType(),outgoing);
            taskService.complete(task.getId(),variables);
            //完成审批后设置下一个节点的审批人
            if(StringUtils.isNotEmpty(assignee)){
                task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
                taskService.setVariableLocal(task.getId(),ActivitiEnum.ASSIGNEE.getVariableType(),assignee);
                taskService.setVariableLocal(task.getId(),ActivitiEnum.ASSIGNEENAME.getVariableType(),assigneeName);
                taskService.claim(task.getId(),assignee);
            }
        } catch (Exception e) {
            logger.error("处理审批失败",e);
            return 0;
        }
        if(null == runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult()){
            return -1;
        }
        return 1;
    }

    @Override public String getWorkflowDesc(String workflowName) {
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionName(workflowName).latestVersion().singleResult();
        return processDefinition.getDescription();
    }

    @Override public boolean isComplete(String businessKey) {
        return null == runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
    }

    @Override public List<TaskVo> searchHistoryTaskByBusinessKey(String businessKey) {
        //获取流程发起人
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();
        User user = identityService.createUserQuery().userId(historicProcessInstance.getStartUserId()).singleResult();
        List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).orderByTaskCreateTime().asc().list();
        if(CollectionUtils.isEmpty(list)){
            return  null;
        }
        ArrayList<TaskVo> taskVos = new ArrayList<>();
        for (HistoricTaskInstance task:list) {
            TaskVo taskVo = new TaskVo();
            taskVo.setStartTime(task.getStartTime());
            taskVo.setEndTime(task.getEndTime());
            if(ActivitiEnum.INITIATOR.getVariableType().equals(task.getAssignee())){
                taskVo.setAssignee(user.getId());
                taskVo.setAssigneeName(user.getFirstName());
            }else {
                taskVo.setAssignee(task.getAssignee());
            }
            taskVo.setTaskName(task.getName());
            List<HistoricVariableInstance> variableInstances = historyService.createHistoricVariableInstanceQuery().taskId(task.getId()).list();
            if(!CollectionUtils.isEmpty(variableInstances)){
                //从历史变量中获取审批意见，流程走向，审批人名字
                for (HistoricVariableInstance varibale:variableInstances) {
                    if(ActivitiEnum.COMMENTS.getVariableType().equals(varibale.getVariableName())&&null !=varibale.getValue()){
                        taskVo.setComments(varibale.getValue().toString());
                    }
                    if(ActivitiEnum.PASS.getVariableType().equals(varibale.getVariableName())&&null !=varibale.getValue()){
                        taskVo.setOutgoingName(varibale.getValue().toString());
                    }
                    if(ActivitiEnum.ASSIGNEENAME.getVariableType().equals(varibale.getVariableName())&&null !=varibale.getValue()){
                        taskVo.setAssigneeName(varibale.getValue().toString());
                    }
                }
            }
            taskVos.add(taskVo);
        }
        return taskVos;
    }

    @Override public int getToDoTaskNum(String assignee) {
        return taskService.createTaskQuery().taskAssignee(assignee).list().size();
    }

    @Override public List<OutgoingVo> getStartNodeByBusinessKey(String modelKey,String businessKey,String startUserId){
        //流程走向集合
        ArrayList<OutgoingVo> outgoingVos = new ArrayList<>();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey(modelKey).latestVersion().singleResult();
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        //获取process下的所有流程元件
        Collection<FlowElement> flowElements1 = bpmnModel.getMainProcess().getFlowElements();
        //获取开始节点
        String startEventId = "";
        for (FlowElement flowElement:flowElements1) {
            if(flowElement instanceof StartEvent ){
                startEventId = flowElement.getId();
            }
        }
        //获取经办人userTaskId
        String userTaskId = "";
        for (FlowElement flowElement:flowElements1) {
            if(flowElement instanceof SequenceFlow && ((SequenceFlow)flowElement).getSourceRef().equals(startEventId)){
                userTaskId = ((SequenceFlow)flowElement).getTargetRef();
            }
        }
        //获取经办人后的流程走向
        FlowElement flowElement = null;
        for (FlowElement flowElement1:flowElements1) {
            if(flowElement1 instanceof SequenceFlow && ((SequenceFlow)flowElement1).getSourceRef().equals(userTaskId)){
                flowElement = bpmnModel.getMainProcess().getFlowElement(((SequenceFlow) flowElement1).getTargetRef());
            }
        }
        if(null != flowElement && flowElement instanceof UserTask){//下个节点是用户
            List<CandidateVo> candidateVos = new ArrayList<>();
            List<String> candidateGroups = ((UserTask) flowElement).getCandidateGroups();
            String doc = ((UserTask) flowElement).getDocumentation();
            if (!CollectionUtils.isEmpty(candidateGroups)){ //用户组
                List<User> users = new ArrayList<>();
                if(null !=doc && doc.startsWith("{") && doc.endsWith("}") ){
                    if(doc.contains(ActivitiEnum.BUSINESSKEY.getVariableType())){
                        doc.replace(ActivitiEnum.BUSINESSKEY.getVariableType(),"'"+businessKey+"'");
                    }
                    if(doc.contains(ActivitiEnum.STARTUSERID.getVariableType())){
                        doc = doc.replace(ActivitiEnum.STARTUSERID.getVariableType(), "'" + startUserId + "'");
                    }
                    doc = doc.replace("{", "").replace("}", "");
                    List<Map<String, Object>> maps = workFlowMapper.executeSql1(doc);
                    if(!CollectionUtils.isEmpty(maps)){
                        for (Map<String,Object> map:maps) {
                            CandidateVo candidateVo = new CandidateVo();
                            candidateVo.setUserId(map.get("userId").toString());
                            candidateVo.setUserName(map.get("userName").toString());
                            candidateVos.add(candidateVo);
                        }
                    }
                }else {
                    users = identityService.createUserQuery().memberOfGroup(candidateGroups.get(0)).list();
                }
                if(!CollectionUtils.isEmpty(users)){
                    for (User user1:users) {
                        CandidateVo candidateVo = new CandidateVo();
                        candidateVo.setUserId(user1.getId());
                        candidateVo.setUserName(user1.getFirstName());
                        candidateVos.add(candidateVo);
                    }
                }
            }
            OutgoingVo outgoingVo = new OutgoingVo();
            outgoingVo.setCandidateVos(candidateVos);
            outgoingVos.add(outgoingVo);
        }else if(flowElement instanceof  ExclusiveGateway || flowElement instanceof  ParallelGateway){//下个节点是网关
            for (FlowElement flowElement2:flowElements1) {
                if(flowElement2 instanceof SequenceFlow && ((SequenceFlow) flowElement2).getSourceRef().equals(flowElement.getId())){
                    OutgoingVo outgoingVo = new OutgoingVo();
                    outgoingVo.setOutgoingName(flowElement2.getName());
                    outgoingVo.setConditionExpression(((SequenceFlow) flowElement2).getConditionExpression());
                    //封装流程走向的下一个节点信息
                    ArrayList<CandidateVo> candidateVos = new ArrayList<>();
                    String targetRef = ((SequenceFlow) flowElement2).getTargetRef();
                    for (FlowElement flowElement1:flowElements1) {
                        //结束节点
                        if(flowElement1 instanceof EndEvent && flowElement1.getId().equals(targetRef)){
                            CandidateVo candidateVo = new CandidateVo();
                            candidateVo.setUserId("end");
                            candidateVo.setUserName("流程结束");
                            candidateVos.add(candidateVo);
                        }
                        //user节点
                        if(flowElement1 instanceof UserTask && flowElement1.getId().equals(targetRef)){
                            //用户
                            String assignee = ((UserTask) flowElement1).getAssignee();
                            String doc = ((UserTask) flowElement1).getDocumentation();
                            //用户组
                            List<String> candidateGroups = ((UserTask) flowElement1).getCandidateGroups();
                           if (!CollectionUtils.isEmpty(candidateGroups)){//用户组
                                List<User> users = new ArrayList<>();
                                if(null !=doc && doc.startsWith("{") && doc.endsWith("}") ){
                                    if(doc.contains(ActivitiEnum.BUSINESSKEY.getVariableType())){
                                        doc.replace(ActivitiEnum.BUSINESSKEY.getVariableType(),"'"+businessKey+"'");
                                    }
                                    if(doc.contains(ActivitiEnum.STARTUSERID.getVariableType())){
                                        doc = doc.replace(ActivitiEnum.STARTUSERID.getVariableType(), "'" + startUserId + "'");
                                    }
                                    doc = doc.replace("{", "").replace("}", "");
                                    List<Map<String, Object>> maps = workFlowMapper.executeSql1(doc);
                                    if(!CollectionUtils.isEmpty(maps)){
                                        for (Map<String,Object> map:maps) {
                                            CandidateVo candidateVo = new CandidateVo();
                                            candidateVo.setUserId(map.get("userId").toString());
                                            candidateVo.setUserName(map.get("userName").toString());
                                            candidateVos.add(candidateVo);
                                        }
                                    }
                                }else {
                                    users = identityService.createUserQuery().memberOfGroup(candidateGroups.get(0)).list();
                                }
                                if(!CollectionUtils.isEmpty(users)){
                                    for (User user1:users) {
                                        CandidateVo candidateVo = new CandidateVo();
                                        candidateVo.setUserId(user1.getId());
                                        candidateVo.setUserName(user1.getFirstName());
                                        candidateVos.add(candidateVo);
                                    }
                                }

                            }
                        }
                    }
                    outgoingVo.setCandidateVos(candidateVos);
                    outgoingVos.add(outgoingVo);
                }
            }
        }
        return outgoingVos;
    }

    @Override public Page<Model> getModelByPage(PageHelper<Model> pageHelper, String modelName) {
        Page<Model> page = new Page<>(pageHelper.getCurrent(), pageHelper.getSize());
        ModelQuery modelQuery = repositoryService.createModelQuery().latestVersion().orderByLastUpdateTime().desc();
        if(StringUtils.isNotEmpty(modelName)){
            modelQuery.modelNameLike("%"+modelName+"%");
        }
        page.setTotal(modelQuery.count());
        page.setRecords(modelQuery.listPage(((Long)page.getCurrent()).intValue(),((Long)page.getSize()).intValue()));
        return page;
    }

    @Override public List<OutgoingVo> getOutgoingsByBusinessKey(String businessKey) {
        //流程走向集合
        ArrayList<OutgoingVo> outgoingVos = new ArrayList<>();
        Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
        if(null == task){
            return null;
        }
        //根据流程定义id获取模型
        BpmnModel bpmnModel = repositoryService.getBpmnModel(task.getProcessDefinitionId());
        //获取当前节点
        String currentActivityId = task.getTaskDefinitionKey();
        FlowNode currentFlowNode = (FlowNode)bpmnModel.getMainProcess().getFlowElement(currentActivityId);
        //获取process
        Process process = bpmnModel.getProcesses().get(0);
        //获取process下的所有流程元件
        Collection<FlowElement> flowElements1 = process.getFlowElements();
        //先获取当前节点的连线，根据连线获取关口的id
        String gateWayId ="";
        for (FlowElement flowElement:flowElements1) {
            if(flowElement instanceof SequenceFlow && ((SequenceFlow) flowElement).getSourceRef().equals(currentActivityId)){
                gateWayId= ((SequenceFlow) flowElement).getTargetRef();
            }
        }
        //获取流程发起人
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceBusinessKey(businessKey).singleResult();

        User user = identityService.createUserQuery().userId(processInstance.getStartUserId()).singleResult();
        for (FlowElement flowElement:flowElements1) {
            //获取到流程走向
            if(flowElement instanceof SequenceFlow && ((SequenceFlow) flowElement).getSourceRef().equals(gateWayId)){
                OutgoingVo outgoingVo = new OutgoingVo();
                outgoingVo.setOutgoingName(flowElement.getName());
                outgoingVo.setConditionExpression(((SequenceFlow) flowElement).getConditionExpression());
                //封装流程走向的下一个节点信息
                ArrayList<CandidateVo> candidateVos = new ArrayList<>();
                String targetRef = ((SequenceFlow) flowElement).getTargetRef();
                for (FlowElement flowElement1:flowElements1) {
                    //结束节点
                    if(flowElement1 instanceof EndEvent && flowElement1.getId().equals(targetRef)){
                        CandidateVo candidateVo = new CandidateVo();
                        candidateVo.setUserId("end");
                        candidateVo.setUserName("流程结束");
                        candidateVos.add(candidateVo);
                    }
                    //user节点
                    if(flowElement1 instanceof UserTask && flowElement1.getId().equals(targetRef)){
                        //用户
                        String assignee = ((UserTask) flowElement1).getAssignee();
                        String doc = ((UserTask) flowElement1).getDocumentation();
                        //用户组
                        List<String> candidateGroups = ((UserTask) flowElement1).getCandidateGroups();
                        if(null != assignee && (ActivitiEnum.INITIATOR.getVariableType()).equals(assignee)){
                            if(null != user){
                                CandidateVo candidateVo = new CandidateVo();
                                candidateVo.setUserId(user.getId());
                                candidateVo.setUserName(user.getFirstName());
                                candidateVos.add(candidateVo);
                            }
                        }else if (!CollectionUtils.isEmpty(candidateGroups)){//用户组
                            List<User> users = new ArrayList<>();
                            if(null !=doc && doc.startsWith("{") && doc.endsWith("}") ){
                                if(doc.contains(ActivitiEnum.BUSINESSKEY.getVariableType())){
                                    doc.replace(ActivitiEnum.BUSINESSKEY.getVariableType(),"'"+businessKey+"'");
                                }
                                if(doc.contains(ActivitiEnum.STARTUSERID.getVariableType())){
                                     doc = doc.replace(ActivitiEnum.STARTUSERID.getVariableType(), "'" + user.getId() + "'");
                                }
                                doc = doc.replace("{", "").replace("}", "");
                                List<Map<String, Object>> maps = workFlowMapper.executeSql1(doc);
                                if(!CollectionUtils.isEmpty(maps)){
                                    for (Map<String,Object> map:maps) {
                                        CandidateVo candidateVo = new CandidateVo();
                                        candidateVo.setUserId(map.get("userId").toString());
                                        candidateVo.setUserName(map.get("userName").toString());
                                        candidateVos.add(candidateVo);
                                    }
                                }
                            }else {
                                users = identityService.createUserQuery().memberOfGroup(candidateGroups.get(0)).list();
                            }
                            if(!CollectionUtils.isEmpty(users)){
                                for (User user1:users) {
                                    CandidateVo candidateVo = new CandidateVo();
                                    candidateVo.setUserId(user1.getId());
                                    candidateVo.setUserName(user1.getFirstName());
                                    candidateVos.add(candidateVo);
                                }
                            }

                        }
                    }
                }
                outgoingVo.setCandidateVos(candidateVos);
                outgoingVos.add(outgoingVo);
            }
        }
        return outgoingVos;
    }

    @Override public InputStream getResourceDiagramInputStream(String businessKey) {
        InputStream inputStream = null;
        try {
            List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey).list();
            String rocessInstanceId = list.get(0).getProcessInstanceId();
            String processDefinitionId = list.get(0).getProcessDefinitionId();
//            Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
            //获取流程中已经执行的节点，按照执行的先后顺序
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(rocessInstanceId).orderByHistoricActivityInstanceId().asc().list();
            //已经执行的节点ID的集合
            ArrayList<String> executedActivityIdList = new ArrayList<>();
            historicActivityInstances.forEach(historicActivityInstance -> executedActivityIdList.add(historicActivityInstance.getActivityId()));
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
            //获取流程已发生流转的线的ID集合
            List<String> flowIds = this.getHightLightFlows(bpmnModel, historicActivityInstances);
            ProcessDiagramGenerator processDiagramGenerator = processEngine.getProcessEngineConfiguration().getProcessDiagramGenerator();
            inputStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList, flowIds, "宋体", "微软雅黑", "黑体", null, 2.0);
        } catch (Exception e) {
            logger.error("生成流程图片失败",e);
            return null;
        }
        return inputStream;
    }

    @Override public boolean claim(String businessKey, String userId) {
        try {
            Task task = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
            taskService.claim(task.getId(),userId);
        } catch (Exception e) {
            logger.error("签收失败",e);
            return  false;
        }
        return true;
    }

    @Override public FileInputStream downloadModelByName(String modelName) {
        Model model = repositoryService.getModel("");
        byte[] modelEditorSource = repositoryService.getModelEditorSource(model.getId());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(modelEditorSource);

        return null;
    }
    private List<String> getHightLightFlows(BpmnModel bpmnModel,List<HistoricActivityInstance> historicActivityInstances){
        ArrayList<String> flowIdList = new ArrayList<>();//需要高亮的线ID集合
        Map<String, HistoricActivityInstance> map = new HashMap<>();
        for (HistoricActivityInstance historicActivityInstance:historicActivityInstances) {
            map.put(historicActivityInstance.getActivityId(),historicActivityInstance);
        }
        for (int i=0;i<historicActivityInstances.size();i++){
            FlowNode flowNode =(FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstances.get(i).getActivityId(), true);
            List<SequenceFlow> outgoingFlows = flowNode.getOutgoingFlows();
            for (SequenceFlow sequenceFlow:outgoingFlows){//如果当前线的开始节点结束时间和结束节点的开始时间相同，则该线已经流转过
                String targetRef = sequenceFlow.getTargetRef();
                String sourceRef = sequenceFlow.getSourceRef();
                if(null !=map.get(targetRef)&&null != map.get(sourceRef)&&DateUtil.getSdfTimesByDate(map.get(sourceRef).getEndTime()).equals(DateUtil.getSdfTimesByDate(map.get(targetRef).getStartTime()))){
                    flowIdList.add(sequenceFlow.getId());
                }
            }
        }
        return flowIdList;
    }



    private List<String> getExecutedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        ArrayList<String> flowIdList = new ArrayList<>();    //流转线ID集合
        LinkedList<FlowNode> historicFlowNodeList = new LinkedList<>();//全部活动实例
        List<HistoricActivityInstance> finishedActivityInstanceList = new LinkedList<HistoricActivityInstance>();	//已完成的历史活动节点
        for(HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            historicFlowNodeList.add((FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true));
            if(historicActivityInstance.getEndTime() != null) {
                finishedActivityInstanceList.add(historicActivityInstance);
            }
        }
        /**遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的*/
        FlowNode currentFlowNode = null;
        for(HistoricActivityInstance currentActivityInstance : finishedActivityInstanceList) {
            /**获得当前活动对应的节点信息及outgoingFlows信息*/
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlowList = currentFlowNode.getOutgoingFlows();
            /**
             * 遍历outgoingFlows并找到已流转的
             * 满足如下条件任务已流转：
             * 1.当前节点是并行网关或互斥网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最近的流转节点视为有效流转
             */
            FlowNode targetFlowNode = null;
            if(BpmsActivityTypeEnum.PARALLEL_GATEWAY.getType().equals(currentActivityInstance.getActivityType())
                    || BpmsActivityTypeEnum.EXCLUSIVE_GATEWAY.getType().equals(currentActivityInstance.getActivityType())) {
                for(SequenceFlow sequenceFlow : sequenceFlowList) { //遍历历史活动节点，找到匹配Flow目标节点的
                    String targetRef = sequenceFlow.getTargetRef();
                    String sourceRef = sequenceFlow.getSourceRef();

                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    //根据前一个节点的结束时间和下一个节点的开始时间俩判断当前线是否已经结束。
                    if(historicFlowNodeList.contains(targetFlowNode)) {
                        flowIdList.add(sequenceFlow.getId());
                    }
                }
            }else{
                List<Map<String, String>> tempMapList = new LinkedList<Map<String,String>>();
                for(SequenceFlow sequenceFlow : sequenceFlowList) {	 //遍历历史活动节点，找到匹配Flow目标节点的
                    for(HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                        if(historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
                            tempMapList.add(UtilMisc.toMap("flowId", sequenceFlow.getId(), "activityStartTime", String.valueOf(historicActivityInstance.getStartTime().getTime())));
                        }
                    }
                }
                String flowId = null;
                for (Map<String, String> map : tempMapList) {
                    flowId = map.get("flowId");
                    flowIdList.add(flowId);
                }
            }
        }
        return flowIdList;
    }
}
