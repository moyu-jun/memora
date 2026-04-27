package com.junmoyu.iam.model.request;

import com.junmoyu.iam.model.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/**
 * 更新用户信息请求
 */
@Data
@Schema(description = "更新用户信息请求")
public class UserUpdateRequest {

    @Schema(description = "昵称")
    private String nickname;

    @Schema(description = "性别：0-保密，1-男，2-女")
    private GenderEnum gender;

    @Schema(description = "用户头像URL")
    private String avatar;

    @Schema(description = "手机号码")
    private String mobile;

    @Schema(description = "邮箱地址")
    private String email;

    @Schema(description = "禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;

    @Schema(description = "角色ID列表")
    @NotEmpty(message = "角色不能为空")
    private List<Long> roleIds;
}
