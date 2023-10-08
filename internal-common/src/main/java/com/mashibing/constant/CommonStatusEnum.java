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
    /**
     * 地图信息：1400-1499
     */
    MAP_DISTRICT_ERROR(1400,"请求地图错误"),
    /**
     * 司机和车辆：1500-1599
     */
    DRIVER_CAR_BIND_EXIST(1500,"司机和车辆绑定关系已存在，请勿重复绑定"),
    DRIVER_CAR_BIND_NOT_EXIST(1501,"司机和车辆绑定关系不存在"),
    DRIVER_NOT_EXIST(1502,"司机不存在"),
    DRIVER_BIND_EXIST(1503, "司机已经被绑定，请勿重复绑定"),
    CAR_BIND_EXIST(1504, "车辆已经被绑定，请勿重复绑定"),

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
