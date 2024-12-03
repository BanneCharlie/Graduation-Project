package com.ruoyi.project.template.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import com.ruoyi.project.template.commons.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author niminui
 * @date 2020/11/20 15:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@TableName("DATA_SOURCE_CONFIG")
public class DataSourceConfig extends AbstractEntity {

    @TableId(value = "DATA_SOURCE_ID", type = IdType.UUID)
    private String dataSourceId;

    private String dataSourceName;

    private String jdbcUrl;

    private String jdbcPwd;

    private String jdbcUserName;

    private String jdbcDriverType;

    private String schemaName;

    private String status;

    private Integer orderNum;

}
