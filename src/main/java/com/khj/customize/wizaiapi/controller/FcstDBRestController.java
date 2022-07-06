package com.khj.customize.wizaiapi.controller;

import com.google.gson.Gson;
import com.khj.controller.MainController;
import com.khj.customize.openapi.service.vo.ApiInfo042Vo;

import com.khj.customize.utils.response.ApiResponse;
import com.khj.customize.utils.response.ResponseCheck;
import com.khj.customize.wizaiapi.service.BeachService;
import com.khj.customize.wizaiapi.vo.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.applet.Main;

import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class FcstDBRestController {

    @Autowired
    private BeachService beachService;

    Gson gson = new Gson();

    //파고정보.
    @RequestMapping(value = {"/getWhBuoy" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getFargoVOL(BaseForeCastVO baseForeCastVO ,Locale locale) throws Exception {
        String paramChk = ResponseCheck.getParamCheckDB(baseForeCastVO,"predict");

		if (paramChk.equals("10")) {
			ApiResponse api = ApiResponse.error(400, "파라미터가 잘못되엇습니다.");
			String jsonByGson = gson.toJson(api);
			return jsonByGson;

		} else if (paramChk.equals("20")) {
			ApiResponse api = ApiResponse.error(400, "최근3일간의 데이터만 제공합니다.");
			String jsonByGson = gson.toJson(api);
			return jsonByGson;
		};

        List<String> beach_info = MainController.getBeach_info(baseForeCastVO.getBeachNum());
        List<Map<String,Object>> list = beachService.getFargoVOL(baseForeCastVO.getSearchTime().substring(0,10),Integer.valueOf(beach_info.get(3)));
        List<FargoVO> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            FargoVO vo = new FargoVO( baseForeCastVO.getBeachNum(),
                    baseForeCastVO.getSearchTime(),
                    list.get(i).get("WH").toString()
            );
            result.add(vo);
        };

        return gson.toJson(result);
    };

    //조석정보
    @RequestMapping(value = {"/getTideinfo" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getTideVOL(BaseForeCastVO baseForeCastVO ,Locale locale) throws Exception {
        String paramChk = ResponseCheck.getParamCheckDB(baseForeCastVO,"fcst");

        if (paramChk.equals("10")) {
            ApiResponse api = ApiResponse.error(400, "파라미터가 잘못되엇습니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;

        } else if (paramChk.equals("20")) {
            ApiResponse api = ApiResponse.error(400, "최근3일간의 데이터만 제공합니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;
        };
        List<String> beach_info = MainController.getBeach_info(baseForeCastVO.getBeachNum());
        List<Map<String,Object>> list = beachService.getTideVOL(baseForeCastVO.getBase_date(),Integer.valueOf(beach_info.get(3)));
        List<TideVO> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            TideVO vo = new TideVO( baseForeCastVO.getBeachNum(),
                    baseForeCastVO.getBase_date(),
                    list.get(i).get("TITIME").toString(),
                    list.get(i).get("TITYPE").toString(),
                    list.get(i).get("TILEVEL").toString()
            );
            result.add(vo);
        };
        return gson.toJson(result);
    };

    //일출일몰
    @RequestMapping(value = {"/getSunInfo" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getSunVOL(BaseForeCastVO baseForeCastVO ,Locale locale) throws Exception {
        String paramChk = ResponseCheck.getParamCheckDB(baseForeCastVO,"fcst");

        if (paramChk.equals("10")) {
            ApiResponse api = ApiResponse.error(400, "파라미터가 잘못되엇습니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;

        } else if (paramChk.equals("20")) {
            ApiResponse api = ApiResponse.error(400, "최근3일간의 데이터만 제공합니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;
        };

        List<String> beach_info = MainController.getBeach_info(baseForeCastVO.getBeachNum());
        List<Map<String,Object>> list = beachService.getSunVOL(baseForeCastVO.getBase_date(), Integer.valueOf(beach_info.get(3)));

        List<SunVO> result = new ArrayList<>();
        System.out.println(baseForeCastVO.getBase_date());

            SunVO vo = new SunVO( baseForeCastVO.getBeachNum(),
                    baseForeCastVO.getBase_date(),
                    list.get(0).get("SUNRISE").toString(),
                    list.get(0).get("SUNSET").toString()
            );
            result.add(vo);

        return gson.toJson(result);
    };

    //수온.
    @RequestMapping(value = {"/getTwBuoy" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getWaterVOL(BaseForeCastVO baseForeCastVO ,Locale locale) throws Exception {
        String paramChk = ResponseCheck.getParamCheckDB(baseForeCastVO,"predict");

        if (paramChk.equals("10")) {
            ApiResponse api = ApiResponse.error(400, "파라미터가 잘못되엇습니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;

        } else if (paramChk.equals("20")) {
            ApiResponse api = ApiResponse.error(400, "최근3일간의 데이터만 제공합니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;
        };

        List<String> beach_info = MainController.getBeach_info(baseForeCastVO.getBeachNum());

        //여기부터 데이터베이스 정보
        List<Map<String,Object>> list = beachService.getWaterVOL(baseForeCastVO.getSearchTime().substring(0,10),Integer.valueOf(beach_info.get(3)));
        List<WaterVO> result = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            WaterVO vo = new WaterVO( baseForeCastVO.getBeachNum(),
                    baseForeCastVO.getSearchTime(),
                    list.get(i).get("TW").toString()
            );
            result.add(vo);
        };

        return gson.toJson(result);
    };

//    @RequestMapping(value = {"/getObTideInfo" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
//    @ResponseBody
//    public Object test(BaseForeCastVO baseForeCastVO, Locale locale) throws Exception{
//        List<Object> result = new ArrayList<>();
//
//        List<String> beach_info = MainController.getBeach_info(baseForeCastVO.getBeachNum());
//        List<Map<String,Object>> list = beachService.getTest(baseForeCastVO.getBeachNum());
//        return gson.toJson(list);
//    }


}