package com.khj.customize.wizaiapi.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class TideVO {

    private int beachnum; //해수욕장 ID
    private String basedate;
    private String tiTime;
    private String tiType;
    private String tilevel;
}
