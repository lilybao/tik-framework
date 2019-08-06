package com.jeerigger.activiti.service;/*
 * @项目名称: jeechuangshen
 * @日期: 2019/3/4 0004 下午 4:16
 * @版权: 2019 河南中审科技有限公司 Inc. All rights reserved.
 * @开发公司或单位：河南中审科技有限公司研发中心
 */

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jeerigger.activiti.vo.CandidateVo;
import com.jeerigger.activiti.vo.OutgoingVo;
import com.jeerigger.activiti.vo.TaskVo;
import com.jeerigger.frame.page.PageHelper;
import org.activiti.engine.repository.Model;

/**
 * @ClassName: IWorkflowService
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/3/4 0004 下午 4:16
 * @version: V1.0
 */
public interface IWorkflowService {

    /**
     * 根据流程模型id部署流程模型
     * @param id
     * @return
     */
   boolean deploy(String id);

    /**
     * 根据文件流部署模型
     * @param fileName
     * @param inputStream
     * @return
     */
   String deployByInputStream(String fileName,InputStream inputStream);

    /**
     *根据流程模型id删除流程模型
     * @param id
     * @return
     */
   boolean delete(String id);

    /**
     *根据流程定义key发起流程
     * @param processDefinitionKey 流程定义key
     * @param initiator 发起人
     * @param businessKey 业务主键
     * @param variables 工作流变量
     * @return 流程实例Id
     */
   String start(String processDefinitionKey,String initiator,String businessKey,Map<String,Object> variables);

    /**
     *办理审批
     * @param businessKey  业务主键
     * @param assignee 下个审批人
     * @param assigneeName 下个审批人名字
     * @param comments 审批意见
     * @param outgoing 流程走向
     * @param variables 工作流变量
     * @return 1 办理成功 0 办理失败 -1 流程结束
     */
   int handleTask(String businessKey,String assignee,String assigneeName,String comments,String outgoing,Map<String,Object> variables);

    /**
     * 获取流程说明
     * @param workflowName 流程名称
     * @return
     */
   String getWorkflowDesc(String workflowName);
    /**
     * 判断当前流程是否结束
     * @param businessKey 业务主键
     * @return true 结束 false 未结束
     */
   boolean isComplete(String businessKey);

    /**
     *根据业务主键查询流程审批历史记录
     * @param businessKey 业务主键
     * @return
     */
   List<TaskVo> searchHistoryTaskByBusinessKey(String businessKey);

    /**
     * 获取待办任务的数量
     * @param assignee 审批人
     * @return
     */
   int getToDoTaskNum(String assignee);

    /**
     * 根据业务主键获取流程走向及走向的下个节点信息
     * @param businessKey 业务主键
     * @return
     */
    List<OutgoingVo> getOutgoingsByBusinessKey(String businessKey);

    /**
     * 根据业务主键获取流程图
     * @param businessKey 业务主键
     * @return
     */
    InputStream getResourceDiagramInputStream(String businessKey);

    /**
     *
     * @param businessKey
     * @param userId
     * @return
     */
    boolean claim(String businessKey,String userId);

    FileInputStream  downloadModelByName(String modelName);

    /**
     * 流程发起时获取第一环节待审批人员
     * @param modelKey 模型key
     * @param businessKey 业务key
     * @param startUserId 发起人Id
     * @return
     */
    List<OutgoingVo> getStartNodeByBusinessKey(String modelKey,String businessKey,String startUserId);

    /**
     * 查询流程定义列表
     * @param page
     * @param modelName
     * @return
     */
    Page<Model> getModelByPage(PageHelper<Model> page,String modelName);
}
