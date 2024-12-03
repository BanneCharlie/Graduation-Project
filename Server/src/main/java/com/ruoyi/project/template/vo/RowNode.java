package com.ruoyi.project.template.vo;

import com.ruoyi.project.template.domain.ReportRow;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author niminui
 * @date 2021/6/9 10:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowNode extends ReportRow {

    private List<RowNode> children;

}
