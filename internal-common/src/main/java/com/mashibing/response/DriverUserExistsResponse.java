package com.mashibing.response;

import lombok.Data;

@Data
public class DriverUserExistsResponse {
    private String driverPhone;
    private int ifExists;
}
