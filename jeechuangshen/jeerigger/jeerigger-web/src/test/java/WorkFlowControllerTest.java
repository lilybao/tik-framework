import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.jeerigger.JeeRiggerApplication;
import com.jeerigger.activiti.enums.ActivitiEnum;
import com.jeerigger.activiti.service.IWorkflowService;
import com.jeerigger.activiti.utils.FileUpload;
import com.jeerigger.activiti.vo.CandidateVo;
import com.jeerigger.activiti.vo.OutgoingVo;
import com.jeerigger.activiti.vo.TaskVo;
import com.jeerigger.module.sys.api.ISysUserService;
import com.jeerigger.module.sys.entity.SysUser;
import org.activiti.bpmn.model.Association;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.Process;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.bpmn.model.UserTask;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.shiro.util.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName: WorkFlowControllerTest
 * @Description: TODO
 * @author: libaojian
 * @date: 2019/3/12 0012 上午 10:51
 * @version: V1.0
 */
@Component
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JeeRiggerApplication.class)
public class WorkFlowControllerTest {

    @Autowired
    private RepositoryService repositoryService;

    @Autowired
    private IWorkflowService workflowService;

    @Autowired
    private ISysUserService sysUserService;

    /**
     * 部署模型
     */
    @Test
    public void deploy(){
        File file = new File("H://合同申请.bpmn20.xml");
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String deployId = workflowService.deployByInputStream(file.getName(), fileInputStream);
        System.out.println(deployId);
    }

    /**
     * 发起流程
     */
    @Test
    public void start(){
        HashMap<String, Object> map = new HashMap<>();
//        map.put("pass","提交信息耗材管理岗");
        String businessKey = "htsq@110";
        String processInstanceId = workflowService.start("HTSQ", "licuiping", businessKey, map);
//        int b = workflowService.handleTask(businessKey, "jwc_kjsh", "教务处会计审核", "发起人提交", "", map);
//        System.out.println(b);
    }

    /**
     * 获取流程发起时下一环节的审批人员信息
     */
    @Test
    public void getStartNodeByBusinessKey(){
        List<OutgoingVo> outgoingVos = workflowService.getStartNodeByBusinessKey("HTSQ", "", "");
        outgoingVos.stream().forEach(outgoingVo -> System.out.println(outgoingVo.toString()));
    }

    /**
     * 查询待办数量根据UserId
     */
    @Test
    public void getToDoTaskNum(){
//        boolean claim = workflowService.claim("123", "lili");
//        int i = workflowService.getToDoTaskNum("jwc_kjsh");
        int i = workflowService.getToDoTaskNum("licuiping");
        System.out.println(i);
    }

    /**
     * 完成审批
     */
    @Test
    public void completeTask(){
        HashMap<String, Object> map = new HashMap<>();
//        map.put(ActivitiEnum.MONEY.getVariableType(),20000);
        int b = workflowService.handleTask("clfbx7", "", "", "校长完成审批", "通过", map);
        System.out.println(b);
    }

    /**
     * 测试流程审批记录
     */
    @Test
    public void searchHistoryTaskByBusinessKey(){
        List<TaskVo> taskVos = workflowService.searchHistoryTaskByBusinessKey("htsq7");
        taskVos.stream().forEach(taskVo -> System.out.println(taskVo.toString()));
    }

    /**
     * 获取当前任务的png图片
     * @throws Exception
     */
    @Test
    public void trackProcess() throws Exception{
        InputStream in = workflowService.getResourceDiagramInputStream("htsq7");
        FileUpload.copyFile(in,"F://","123.png");//把文件上传到文件目录里面
        in.close();
    }
    /**
     * 测试流程是否结束
     */
    @Test
    public void isComplete(){
        boolean flag = workflowService.isComplete("clfbx7");
        System.out.println(flag);
    }
    /**
     * 测试流程退回
     */
    @Test
    public void back(){
        HashMap<String, Object> map = new HashMap<>();
        int b = workflowService.handleTask("clfbx6", null, null, "退回到发起人", "退回", map);
        System.out.println(b);
    }

    /**
     * 获取流程说明
     */
    @Test
    public void getDesc(){
        String workflowDesc = workflowService.getWorkflowDesc("差旅费报销");
        System.out.println(workflowDesc);
    }


    /**
     * 获取流程走向和走向下的节点信息
     */
    @Test
    public void getFlowAndNodes(){

        List<OutgoingVo> outgoingsByBusinessKey = workflowService.getOutgoingsByBusinessKey("clfbx13");
        if(CollectionUtils.isEmpty(outgoingsByBusinessKey)){
            System.out.println("null");
        }else {
            outgoingsByBusinessKey.stream().forEach(outgoingVo -> System.out.println(outgoingVo.toString()));
        }
    }

    /**
     * 根据模型Id生成流程图xml和图片
     */
    @Test
    public void createXmlAndPng() throws  Exception{
        String DEPLOYMENT_ID_ = "47501";
        List<String> names = repositoryService.getDeploymentResourceNames(DEPLOYMENT_ID_);
        for (String name : names) {
            if(name.indexOf("zip")!=-1)continue;
            InputStream in = repositoryService.getResourceAsStream(DEPLOYMENT_ID_, name);
            FileUpload.copyFile(in,"F://",name); 		//把文件上传到文件目录里面
            in.close();
        }
    }
    @Test
    public void get(){

        List<SysUser> list = sysUserService.list();

    }

}
