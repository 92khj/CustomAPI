package com.khj.customize.wizaiapi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseForeCastVO {


    private int beachNum; //해수욕장 ID
    private String searchTime;
    private String base_date;
    private String baseTime;
}
