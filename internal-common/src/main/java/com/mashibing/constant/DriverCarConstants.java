package com.mashibing.constant;

public class DriverCarConstants {
    /**
     * 司机车辆关系状态：绑定
     */
    public static final Integer DRIVER_CAR_BIND = 1;

    /**
     * 司机车辆关系状态：解绑
     */
    public static final Integer DRIVER_CAR_UNBIND = 2;
    /**
     * 司机状态：1:有效
     */
    public static final Integer DRIVER_STATE_VALID = 1;
    /**
     * 司机状态：0:有效
     */
    public static final Integer DRIVER_STATE_INVALID = 0;
    /**
     * 司机状态：1:存在
     */
    public static final Integer DRIVER_EXISTS = 1;
    /**
     * 司机状态：0:不存在
     */
    public static final Integer DRIVER_NOT_EXISTS = 0;

    /**
     * 司机工作状态：出车
     */
    public static final Integer DRIVER_WORK_START = 1;
    /**
     * 司机工作状态：收车
     */
    public static final Integer DRIVER_WORK_STOP = 0;
    /**
     * 司机工作状态：暂停
     */
    public static final Integer DRIVER_WORK_SUSPEND = 2;
}
