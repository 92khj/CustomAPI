package com.khj.customize.wizaiapi.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MidForeCastVo {
    private String category;
    private String[] foreValues;
}
