package com.junmoyu.iam.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * 用户列表响应
 */
@Data
public class UserListResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "系统用户名/登录名")
    private String username;

    @Schema(description = "用户头像URL")
    private String avatar;
}
