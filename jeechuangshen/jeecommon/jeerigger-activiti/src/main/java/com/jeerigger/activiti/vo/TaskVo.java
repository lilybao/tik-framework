package com.jeerigger.activiti.vo;

import java.util.Date;

import lombok.Data;

/**
 * 流程环节
 * @author zzh
 *
 */
@Data
public class TaskVo {
	/**
	 * 环节名称
	 */
	private String taskName;
	/**
	 * 办理人员
	 */
	private String assignee;
	/**
	 * 办理人员名称
	 */
	private String assigneeName;
	/**
	 * 环节到达时间
	 */
	private Date startTime;
	/**
	 * 办理完成时间
	 */
	private Date endTime;
	/**
	 * 选择走向名称
	 */
	private String outgoingName;
	/**
	 * 审批意见
	 */
	private String comments;
}
