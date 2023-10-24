package com.mashibing.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PointRequestDTO {
    private String tid;
    private String trid;
    private PointDTO[] points;
}
