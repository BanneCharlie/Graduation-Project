package com.ruoyi.project.common.vo;
/**
 * 工作流连线存储VO
 * @author optimus
 *
 */
public class ProcessResultVo {
	
	String processInstId;//流程实例ID
	String selectedUserIds;//已选择用户登录名
	String selectedUserNames;//已选择用户名	
	String currentActId;//当前节点ID
	String currentActName;//当前节点名称
	String userActionType; //用户选择动作  0=XXX默认提交给selectedUserNames  1=XXX新增任务给selectedUserNames
	String processDefId;//流程模版ID
	String processKey;//流程KEY
	String businessKey;//流程表单键
	String blfs;  //1代表默认提交   2代表完成任务  3代表新增任务

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	String  result;
	public String getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	String isEnd;
	public String getProcessInstId() {
		return processInstId;
	}
	public void setProcessInstId(String processInstId) {
		this.processInstId = processInstId;
	}
	public String getSelectedUserIds() {
		return selectedUserIds;
	}
	public void setSelectedUserIds(String selectedUserIds) {
		this.selectedUserIds = selectedUserIds;
	}
	public String getSelectedUserNames() {
		return selectedUserNames;
	}
	public void setSelectedUserNames(String selectedUserNames) {
		this.selectedUserNames = selectedUserNames;
	}
	public String getCurrentActId() {
		return currentActId;
	}
	public void setCurrentActId(String currentActId) {
		this.currentActId = currentActId;
	}
	public String getCurrentActName() {
		return currentActName;
	}
	public void setCurrentActName(String currentActName) {
		this.currentActName = currentActName;
	}
	public String getUserActionType() {
		return userActionType;
	}
	public void setUserActionType(String userActionType) {
		this.userActionType = userActionType;
	}
	public String getProcessDefId() {
		return processDefId;
	}
	public void setProcessDefId(String processDefId) {
		this.processDefId = processDefId;
	}
	public String getProcessKey() {
		return processKey;
	}
	public void setProcessKey(String processKey) {
		this.processKey = processKey;
	}
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	public String getBlfs() {
		return blfs;
	}
	public void setBlfs(String blfs) {
		this.blfs = blfs;
	}

}
