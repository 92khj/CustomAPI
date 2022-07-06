package com.khj.customize.openapi.service.vo;

import com.khj.customize.openapi.CmmnVo;

import java.io.Serializable;

public class ApiInfo042Vo extends CmmnVo implements Serializable {

    // serialVersion UID
    private static final long serialVersionUID = -858838578081269359L;

    // 조회
    private String ftype;
    private String base_date;	//발표일자
    private String base_time;	//발표시각
    private String baseDate;	//발표일자
    private String baseTime;	//발표시각
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;

    private int nx;	//예보지점 X 좌표
    private int ny;	//예보지점 Y 좌표
    private int beachNum;
    private String obsrValue;

    private String basedatetime;
    private String filetype;
    private String version;


    // 일기도정보조회서비스
    // 공통.
    private String file_url;	//파일정보.


    /*************************************/

    public String getBaseDate() {
        return baseDate;
    }

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

    public String getFtype() {
        return ftype;
    }

    public void setFtype(String ftype) {
        this.ftype = ftype;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getNy() {
        return ny;
    }

    public void setNy(int ny) {
        this.ny = ny;
    }

    public String getObsrValue() {
        return obsrValue;
    }

    public void setObsrValue(String obsrValue) {
        this.obsrValue = obsrValue;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFcstDate() {
        return fcstDate;
    }

    public void setFcstDate(String fcstDate) {
        this.fcstDate = fcstDate;
    }

    public String getFcstTime() {
        return fcstTime;
    }

    public void setFcstTime(String fcstTime) {
        this.fcstTime = fcstTime;
    }

    public String getFcstValue() {
        return fcstValue;
    }

    public void setFcstValue(String fcstValue) {
        this.fcstValue = fcstValue;
    }

    public String getBasedatetime() {
        return basedatetime;
    }

    public void setBasedatetime(String basedatetime) {
        this.basedatetime = basedatetime;
    }

    public String getFiletype() {
        return filetype;
    }

    public void setFiletype(String filetype) {
        this.filetype = filetype;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getBeachNum() {
        return beachNum;
    }

    public void setBeachNum(int beachNum) {
        this.beachNum = beachNum;
    }

/*************************************/
}
