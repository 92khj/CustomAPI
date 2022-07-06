package com.khj.customize.openapi.service.impl;


import com.khj.customize.openapi.forecast.*;
import com.khj.customize.openapi.service.ApiInfo042Service;
import com.khj.customize.openapi.service.vo.ApiInfo042Vo;
import com.khj.customize.openapi.util.StringUtils;

import org.springframework.stereotype.Service;

@Service("apiInfo042Service")
public class ApiInfo042ServiceImpl implements ApiInfo042Service {


    //2. 초단기예보조회
    public DFSPointVsrtData getForecastTimeData(ApiInfo042Vo apiInfo042Vo ) throws Exception{
        int x = apiInfo042Vo.getNx();
        int y = apiInfo042Vo.getNy();

        java.util.Date baseDate = StringUtils.parseDate(apiInfo042Vo.getBaseDate() + apiInfo042Vo.getBaseTime(),"yyyyMMddHHmm",null);
        String date = StringUtils.getFormattedDate(baseDate, "yyyyMMddHH");
        //데이터 불러오기
        DFSPointVsrtData re = DFSVsrtCacher.getInstance().getPastPoint(x, y, date);
        return re;
    }

    //3. 동네예보조회
    public DFSPointShrnData getForecastSpaceData(ApiInfo042Vo apiInfo042Vo) throws Exception{
        int x = apiInfo042Vo.getNx();
        int y = apiInfo042Vo.getNy();

        java.util.Date baseDate = StringUtils.parseDate(apiInfo042Vo.getBaseDate() + apiInfo042Vo.getBaseTime(),"yyyyMMddHHmm",null);
        String date = StringUtils.getFormattedDate(baseDate, "yyyyMMddHH");

        //데이터 불러오기
        DFSPointShrnData re = DFSShrnCacher.getInstance().getPastPoint(x, y, date);

        return re;
    }

}
