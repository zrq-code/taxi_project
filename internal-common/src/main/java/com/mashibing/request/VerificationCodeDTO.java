package com.mashibing.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerificationCodeDTO {
    private String passengerPhone;
    private String driverPhone;
    private String verificationCode;
}
