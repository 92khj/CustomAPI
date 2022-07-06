package com.khj.customize.utils.response;

import com.khj.customize.openapi.service.vo.ApiInfo042Vo;
import com.khj.customize.wizaiapi.vo.BaseForeCastVO;

import java.text.SimpleDateFormat;
import java.util.Date;

/*파라미터 체크 다시 재점검 필요.*/
public class ResponseCheck {

    //파라미터 체크
    //리턴값 주석 추가할것.
    public static String getParamCheckFile(ApiInfo042Vo apiInfo042Vo) throws Exception{

        if (apiInfo042Vo.getBase_date() == null) {
            return "10";
        }

        if (apiInfo042Vo.getBase_time() == null) {
            return "10";

        }else if (Integer.valueOf(apiInfo042Vo.getBase_time()) > 2400) {
            return "10";
        }

        if (apiInfo042Vo.getBeachNum() == 0) {
            return "10";
        }

        if (apiInfo042Vo.getBeachNum() > 420) {
            return "10";
        }

        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmm");
        Date baseDate = dt.parse(apiInfo042Vo.getBase_date()+ apiInfo042Vo.getBase_time());

        if ( baseDate.getTime() < (new Date()).getTime() - 1000 * 60 * 60 * 24 * 3) {
            return "20";

        }
        return "OK";
    };

    public static String getParamCheckDB(BaseForeCastVO baseForeCastVO, String type) throws Exception{
        //예보
        if (type.equals("fcst")) {
            if (baseForeCastVO.getBase_date() == null || baseForeCastVO.getBase_date().length() != 8) {
                return "10";
            }

            if (baseForeCastVO.getBeachNum() == 0) {
                return "10";
            }
            if (baseForeCastVO.getBeachNum() > 420) {
                return "10";
            }

            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmm");
            Date baseDate = dt.parse(baseForeCastVO.getBase_date() + "0000");

            if (baseDate.getTime() < (new Date()).getTime() - 1000 * 60 * 60 * 24 * 3) {
                return "20";

            }
        }else if (type.equals("predict")){

            if(baseForeCastVO.getSearchTime() == null || baseForeCastVO.getSearchTime().length() != 12) {
                return "10";
            }
            if (Integer.valueOf(baseForeCastVO.getSearchTime().substring(9))> 2399){
                return "10";
            }
            if (baseForeCastVO.getBeachNum() == 0) {
                return "10";
            }
            if (baseForeCastVO.getBeachNum() > 421) {
                return "10";
            }

            SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmm");
            Date baseDate = dt.parse(baseForeCastVO.getSearchTime());

            if (baseDate.getTime() < (new Date()).getTime() - 1000 * 60 * 60 * 24 * 3) {
                return "20";
            }
        }
        return "OK";
    }
}
