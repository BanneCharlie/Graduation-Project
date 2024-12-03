package com.ruoyi.project.workflow.vo;

import com.ruoyi.project.workflow.domain.EngineProcUserorg;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class NextStepVo {  //映射EngineProcActDefRelation
    private String enId;   //唯一代码eId
    private String currentActDefId;  //当前步骤节点IDactDefRelId
    private String nodeLineName;     // 节点间的连线名称
    private String nextActName;         //actName 下一步节点名称
    private String nextActDefId;        //actDefId 下一步节点ID
    private Date createTime;            //创建时间
    private String enStatus;             //状态
    private String enType;               //类别
    private String procDefId;           //流程定义ID
    private Long enOrder;                //排序
    private List<EngineProcUserorg> step = new ArrayList(); //待选择人员列表Users
}
