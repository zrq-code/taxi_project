package com.mashibing.constant;

import lombok.Getter;


public enum CommonStatusEnum {
    /**
     * 验证码错误提示：1000-1099
     */
    VERIFICATION_CODE_ERROR(1099, "验证码不正确"),
    /**
     * Token Error: 1100-1199
     */
    TOKEN_ERROR(1199, "token错误"),
    /**
     * 用户提示: 1200-1299
     */
    USER_NOT_EXISTS(1200, "当前用户不存在"),
    /**
     * 计价规则不存在
     */
    PRICE_RULE_EMPTY(1300,"计价规则不存在"),
    SUCCESS(1, "success"),
    FAIL(0, "fail");
    @Getter
    private int code;
    @Getter
    private String value;

    CommonStatusEnum(int code, String value) {
        this.code = code;
        this.value = value;
    }
}
