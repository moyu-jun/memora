package com.junmoyu.iam.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junmoyu.basic.model.BaseEntity;
import com.junmoyu.iam.model.enums.PermissionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 权限资源表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_permission")
public class PermissionEntity extends BaseEntity implements Serializable {

    @TableField(value = "parent_id")
    @Schema(description = "父级权限ID，顶级为0")
    private Long parentId;

    @TableField(value = "`name`")
    @Schema(description = "权限/菜单名称")
    private String name;

    @TableField(value = "`code`")
    @Schema(description = "权限标识（如：user:add, menu:sys）")
    private String code;

    @TableField(value = "`type`")
    @Schema(description = "权限类型：1-目录，2-菜单，3-按钮")
    private PermissionTypeEnum type;

    @TableField(value = "`path`")
    @Schema(description = "路由地址或API路径")
    private String path;

    @TableField(value = "icon")
    @Schema(description = "前端图标")
    private String icon;

    @TableField(value = "`sort`")
    @Schema(description = "排序编号")
    private Integer sort;

    @TableField(value = "`disable`")
    @Schema(description = "禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;

    @TableField(value = "visible")
    @Schema(description = "可见状态：0-不可见，1-可见")
    private Boolean visible;
}