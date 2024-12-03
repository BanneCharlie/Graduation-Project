package com.ruoyi.project.business.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 作业指导书
 * @author niminui
 * @date 2021/6/11 10:20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@TableName("file_work")
public class FileWork extends AbstractEntity {

    @TableId(value = "row_id",type = IdType.UUID)
    private String rowId;
    private String fileName;
    private String filePath;
    private String parentId;
    private String parentName;
    private String fileExtension;
    private Integer dataType; //1为目录，2为文件
    private Integer orderNum;
    private String status;

    @TableField(exist = false)
    private List<FileWork> children;
}
