package com.mashibing.util;

public class RedisPrefixUtils {
    //乘客验证码前缀
    public static final String verificationCodePrefix = "passenger-verification-code-";
    public static final String tokenPrefix = "token-";

    /**
     * 根据手机号生成验证码的key
     *
     * @param passengerPhone
     * @return
     */
    public static String generateKeyByPhone(String passengerPhone) {
        return verificationCodePrefix + passengerPhone;
    }

    /**
     * 根据手机号和身份生成token key
     *
     * @param phone
     * @param identity
     * @param tokenType
     * @return
     */
    public static String generateTokenKey(String phone, String identity, String tokenType) {
        return tokenPrefix + phone + "-" + identity + "-" + tokenType;
    }
}
