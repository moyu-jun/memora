package com.junmoyu.iam.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 角色列表响应
 */
@Data
public class RoleListResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "角色名称（如：系统管理员）")
    private String name;

    @Schema(description = "角色编码（如：ADMIN）")
    private String code;
}
