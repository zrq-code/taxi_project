package com.mashibing.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author rick zhou
 * @since 2023-11-14
 */
@Data
public class PriceRule implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cityCode;

    private String vehicleType;

    private Double startFare;

    private Integer startMile;

    private Double unitPricePerMile;

    private Double unitPricePerMinute;

    private Integer fareVersion;

    private String fareType;

}
