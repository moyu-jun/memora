package com.junmoyu.iam.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增/修改角色请求
 */
@Data
@Schema(description = "新增/修改角色请求")
public class RoleCreateUpdateRequest {

    @Schema(description = "角色名称（如：系统管理员）")
    @NotBlank(message = "角色名称不能为空")
    private String name;

    @Schema(description = "角色编码（如：ADMIN）")
    @NotBlank(message = "角色编码不能为空")
    private String code;

    @Schema(description = "排序编号")
    private Integer sort;

    @Schema(description = "角色描述")
    private String remark;

    @Schema(description="禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;
}
