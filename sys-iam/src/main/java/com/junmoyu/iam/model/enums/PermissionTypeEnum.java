package com.junmoyu.iam.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 权限类型枚举类
 */
@Getter
@RequiredArgsConstructor
public enum PermissionTypeEnum {

    DIR(1, "目录"),
    MENU(2, "菜单"),
    BUTTON(3, "按钮");

    @EnumValue
    @JsonValue
    private final Integer code;

    private final String desc;
}
