package com.khj.customize.powerweatherapi.vo;

import com.khj.customize.openapi.CmmnVo;

import java.io.Serializable;

public class PowerWeatherInVo extends CmmnVo implements Serializable {

    // 조회
    private String base_date;	//파일생성일시
    private String base_time;	//파일생성일시

    private Long x;	//예보지점 X 좌표
    private Long y;	//예보지점 Y 좌표




    /*************************************/

    public String getBase_date() {
        return base_date;
    }

    public void setBase_date(String base_date) {
        this.base_date = base_date;
    }

    public String getBase_time() {
        return base_time;
    }

    public void setBase_time(String base_time) {
        this.base_time = base_time;
    }

    public Long getX() {
        return x;
    }

    public void setX(Long x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y) {
        this.y = y;
    }

   /*************************************/
}
