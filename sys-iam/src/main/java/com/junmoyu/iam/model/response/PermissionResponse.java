package com.junmoyu.iam.model.response;

import com.baomidou.mybatisplus.annotation.TableName;
import com.junmoyu.basic.model.BaseEntity;
import com.junmoyu.iam.model.enums.PermissionTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 权限资源表
 */
@Data
public class PermissionResponse implements Serializable {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "父级权限ID，顶级为0")
    private Long parentId;

    @Schema(description = "父级权限名称，顶级为0")
    private String parentName;

    @Schema(description = "权限/菜单名称")
    private String name;

    @Schema(description = "权限标识（如：user:add, menu:sys）")
    private String code;

    @Schema(description = "权限类型：1-目录，2-菜单，3-按钮")
    private PermissionTypeEnum type;

    @Schema(description = "路由地址或API路径")
    private String path;

    @Schema(description = "前端图标")
    private String icon;

    @Schema(description = "排序编号")
    private Integer sort;

    @Schema(description = "禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;

    @Schema(description = "可见状态：0-不可见，1-可见")
    private Boolean visible;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}