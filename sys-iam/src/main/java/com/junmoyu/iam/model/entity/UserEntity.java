package com.junmoyu.iam.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.junmoyu.basic.model.BaseEntity;
import com.junmoyu.iam.model.enums.GenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户基础信息表
 */
@Data
@EqualsAndHashCode(callSuper=true)
@TableName(value = "sys_user")
public class UserEntity extends BaseEntity implements Serializable {

    @TableField(value = "username")
    @Schema(description="系统用户名/登录名（要求唯一）")
    private String username;

    @TableField(value = "`password`")
    @Schema(description="密码")
    private String password;

    @TableField(value = "nickname")
    @Schema(description="昵称")
    private String nickname;

    @TableField(value = "gender")
    @Schema(description="性别：0-保密，1-男，2-女")
    private GenderEnum gender;

    @TableField(value = "avatar")
    @Schema(description="用户头像URL")
    private String avatar;

    @TableField(value = "mobile")
    @Schema(description="手机号码（要求唯一）")
    private String mobile;

    @TableField(value = "email")
    @Schema(description="邮箱地址（要求唯一）")
    private String email;

    @TableField(value = "`disable`")
    @Schema(description="禁用状态：0-未禁用，1-已禁用")
    private Boolean disable;
}