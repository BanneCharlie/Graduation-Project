package com.ruoyi.project.process.vo;

import com.ruoyi.project.process.domian.ProcSxpz;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author niminui
 * @date 2021/5/26 19:29
 */
@Data
@NoArgsConstructor
public class ProcessVo {

    private List<ProcSxpz> sxpzList;
    private String type;
    private String fileRelationBeanId;
    private String flag;
    private String swtaskId;
    private String swId;
    private String sxId;
    private String swtofw;

}
