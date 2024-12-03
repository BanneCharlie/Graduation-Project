package com.ruoyi.project.workflow.vo;

import com.ruoyi.project.workflow.domain.EngineProcDef;

public class ProcRunVo {

    private EngineProcDef engineProcDef;
    private String procInstId;
    private Boolean re;

    public EngineProcDef getEngineProcDef() {
        return engineProcDef;
    }

    public void setEngineProcDef(EngineProcDef engineProcDef) {
        this.engineProcDef = engineProcDef;
    }

    public String getProcInstId() {
        return procInstId;
    }

    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId;
    }

    public Boolean getRe() {
        return re;
    }

    public void setRe(Boolean re) {
        this.re = re;
    }
}
