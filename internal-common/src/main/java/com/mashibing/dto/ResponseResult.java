package com.mashibing.dto;

import com.mashibing.constant.CommonStatusEnum;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResponseResult<T> {
    private int code;
    private String message;
    private T data;

    /**
     * 成功响应的方法
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResponseResult success(T data) {
        return new ResponseResult().setCode(CommonStatusEnum.SUCCESS.getCode())
                .setMessage(CommonStatusEnum.SUCCESS.getValue())
                .setData(data);
    }

    /**统一的失败
     *
     * @param data
     * @return
     * @param <T>
     */
    public static <T> ResponseResult fail(T data){
        return new ResponseResult().setData(data);
    }

    /**
     * 自定义失败错误码和提示信息
     *
     * @param code
     * @param message
     * @return
     */
    public static ResponseResult fail(int code, String message) {
        return new ResponseResult().setCode(code)
                .setMessage(message);
    }

    /**
     * 自定义失败、错误码、提示信息、具体错误
     * @param code
     * @param message
     * @param data
     * @return
     */
    public static ResponseResult fail(int code, String message, String data){
        return new ResponseResult()
                .setCode(code)
                .setMessage(message)
                .setData(data);
    }


}
