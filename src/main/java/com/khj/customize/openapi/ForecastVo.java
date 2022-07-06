package com.khj.customize.openapi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForecastVo {
    private String baseDate;
    private String bastTime;
    private String category;
    private String fcstBase;
    private String fcstTime;
    private String fcstValue;
    private int nx;
    private int ny;
}
