package com.mashibing.request;

import lombok.Data;

@Data
public class ApiDriverPointRequest {
    private Long carId;
    private PointDTO[] points;

}
