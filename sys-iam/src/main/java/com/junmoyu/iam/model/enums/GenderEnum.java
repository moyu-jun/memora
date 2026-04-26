package com.junmoyu.iam.model.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 用户性别枚举类
 */
@Getter
@RequiredArgsConstructor
public enum GenderEnum {

    SECRET(0, "保密"),
    MALE(1, "男"),
    FEMALE(2, "女");

    @EnumValue
    private final Integer code;

    @JsonValue
    private final String desc;
}
