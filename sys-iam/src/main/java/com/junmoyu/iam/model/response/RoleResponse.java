package com.junmoyu.iam.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

/**
 * 角色列表响应
 */
@Data
public class RoleResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "角色名称（如：系统管理员）")
    private String name;

    @Schema(description = "角色编码（如：ADMIN）")
    private String code;

    @Schema(description = "排序编号")
    private Integer sort;

    @Schema(description = "角色描述")
    private String remark;

    @Schema(description = "禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}
