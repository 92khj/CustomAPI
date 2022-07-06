package com.khj.customize.wizaiapi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class SunVO{

    private int beachnum; //해수욕장 ID
    private String basedate;
    private String sunrise;
    private String sunset;

}

