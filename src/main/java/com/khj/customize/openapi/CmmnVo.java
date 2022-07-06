package com.khj.customize.openapi;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class CmmnVo implements Serializable {

    // serialVersion UID
    private static final long serialVersionUID = -858838578081269359L;

    // 페이징 관련
    @ApiModelProperty(value = "페이지 번호")
    private int pageNo = 1;
    @ApiModelProperty(value = "페이지당 item 수")
    private int numOfRows = 10;

    //API 서비스 공통
    @ApiModelProperty(hidden = true)
    private String dataType = "xml";

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(Object pageNo) {

        try {

            if (pageNo == null || Integer.valueOf(pageNo.toString()) < 0) {
                this.pageNo = 1;
            } else {
                this.pageNo = Integer.valueOf(pageNo.toString());
            }

        } catch (NumberFormatException e) {
            this.pageNo = 1;
        }
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(Object numOfRows) {

        try {

            if (numOfRows == null || Integer.valueOf(numOfRows.toString()) < 0) {
                this.numOfRows = 10;
            } else {
                this.numOfRows = Integer.valueOf(numOfRows.toString());
            }
        } catch (NumberFormatException e) {
            this.numOfRows = 10;
        }
    }
}