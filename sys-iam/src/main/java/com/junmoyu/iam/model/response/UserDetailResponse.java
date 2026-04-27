package com.junmoyu.iam.model.response;

import com.junmoyu.iam.model.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户详情响应
 */
@Data
public class UserDetailResponse {

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "系统用户名/登录名")
    private String username;

    @Schema(description = "真实姓名/昵称")
    private String nickname;

    @Schema(description="性别：0-保密，1-男，2-女")
    private GenderEnum gender;

    @Schema(description = "用户头像URL")
    private String avatar;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;

    @Schema(description = "已分配角色列表")
    private List<RoleListResponse> roles;
}
