package com.junmoyu.iam.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junmoyu.basic.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统角色表
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName(value = "sys_role")
public class RoleEntity extends BaseEntity implements Serializable {

    @TableField(value = "`name`")
    @Schema(description="角色名称（如：系统管理员）")
    private String name;

    @TableField(value = "`code`")
    @Schema(description="角色编码（如：ADMIN）")
    private String code;

    @TableField(value = "`sort`")
    @Schema(description="排序编号")
    private Integer sort;

    @TableField(value = "`disable`")
    @Schema(description="禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;

    @TableField(value = "remark")
    @Schema(description="角色描述")
    private String remark;
}