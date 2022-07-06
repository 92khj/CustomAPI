package com.khj.customize.powerweatherapi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PowerWeatherVo {
    private String fcstDate;
    private Object elec_idx;
    private Object sta;
    private Object wbgt;
    private Object tmp;
    private Object reh;
    private Object wsd;
}
