//package com.khj.customize.wizaiapi.controller;
//
//import com.khj.customize.openapi.ForecastVo;
//import com.khj.customize.openapi.forecast.DFSPointShrnData;
//import com.khj.customize.openapi.forecast.DFSPointVsrtData;
//import com.khj.customize.openapi.forecast.DFSShrnCacher;
//import com.khj.customize.openapi.forecast.DFSVsrtCacher;
//import com.khj.customize.openapi.service.ApiInfo042Service;
//import com.khj.customize.openapi.service.vo.ApiInfo042Vo;
//import com.khj.customize.openapi.util.CommonFunction;
//import com.khj.customize.utils.response.ApiResponse;
//import com.khj.customize.utils.response.ResponseCheck;
//import com.khj.customize.wizaiapi.midforecast.MidForeCastNcReader;
//import com.khj.customize.wizaiapi.other.BeachInfo;
//import com.khj.customize.wizaiapi.vo.GpsVO;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.text.SimpleDateFormat;
//import java.util.*;
///*
//
//*/
//@Controller
//public class FcstFileRestController {
//
//
//    static Map<Integer,String[]> beachMap = BeachInfo.readCSV();
//    private String[] midCode = {"POP","PTY","REH","RN3","SKY","SN3","T3H","TMN","TMX","UUU","VVV","WAV"};
//    GpsVO gpsVO = new GpsVO();
//    String strDate;
//    String strTime;
//
//    @Value("${path.shrn}")
//    String pathShrn;
//
//    @Value("${path.vsrt}")
//    String pathVsrt;
//
//    @Value("${path.mid}")
//    String filePath;
//
//    @Autowired
//    CommonFunction commonFunction;
//
//
//    Map<String,String> mapParamInfo = new HashMap<>();
//
//    public void main() {
//        DFSVsrtCacher.init(pathVsrt, false);
//        DFSShrnCacher.init(pathShrn, false);
//    };
//
//
//    @Resource(name = "apiInfo042Service")
//    private ApiInfo042Service apiInfo042Service;
//
//    List<ApiInfo042Vo> result = new ArrayList<>();
//
//
//
//
//    @ApiOperation(value = "?????????????????????", notes="?????????????????????")
//    @ApiImplicitParams({
//            @ApiImplicitParam(required= true,name="baseDate", value="???????????????", paramType="query", defaultValue="20210520"),
//            @ApiImplicitParam(required= true,name="baseTime", value="?????????", paramType="query", defaultValue="2330"),
//            @ApiImplicitParam(required= true,name="nx", value="???????????? X ??????", paramType="query", defaultValue="55"),
//            @ApiImplicitParam(required= true,name="ny", value="???????????? Y ??????", paramType="query", defaultValue="127")
//    })
//    @RequestMapping(value = {"/getUltraSrcFcst"}, method = RequestMethod.GET)
//    public ApiResponse getForecastTimeData(ApiInfo042Vo apiInfo042Vo, Locale locale, HttpServletRequest request) throws Exception {
//        String paramChk = ResponseCheck.getParamCheck(apiInfo042Vo,"file");
//
//        if (paramChk.equals("10")) {
//            return ApiResponse.error(400, "??????????????? ?????????????????????.");
//        }else if (paramChk.equals("20")){
//            return ApiResponse.error(400,"??????1????????? ???????????? ???????????????.");
//        };
//        main();
//
//        List<String> beach_info = getBeach_info(apiInfo042Vo.getBeachNum());
//
//        gpsVO = gpsVO.getGrid(Double.parseDouble(beach_info.get(1)),Double.parseDouble(beach_info.get(2)));
//
//        apiInfo042Vo.setNx((int) gpsVO.getX());
//        apiInfo042Vo.setNy((int) gpsVO.getY());
//
//        //?????? +9 ?????? ????????? ?????????
//
//        strDate = formatDate(apiInfo042Vo,"Ultra");
//
//        apiInfo042Vo.setBaseDate(strDate.substring(0,8));
//        apiInfo042Vo.setBaseTime(strDate.substring(8,12));
//        apiInfo042Vo.setBaseDate(apiInfo042Vo.getBase_date());
//        apiInfo042Vo.setBaseTime(apiInfo042Vo.getBase_time());
//        DFSPointVsrtData re	= apiInfo042Service.getForecastTimeData(apiInfo042Vo);
//
//        result = re.getDFSVsrtWeather();
//
//        ArrayList<ForecastVo> forecastVos = commonFunction.returnFrcVo(result,apiInfo042Vo.getPageNo(),apiInfo042Vo.getNumOfRows(),false);
//
//        return ApiResponse.ok(forecastVos,"????????? ??????");
//    };
//
//
//    @ApiOperation(value = "??????????????????", notes="??????????????????")
//    @ApiImplicitParams({
//            @ApiImplicitParam(required= true,name="baseDate", value="???????????????", paramType="query", defaultValue="20210521"),
//            @ApiImplicitParam(required= true,name="baseTime", value="?????????", paramType="query", defaultValue="0200"),
//            @ApiImplicitParam(required= true,name="nx", value="???????????? X ??????", paramType="query", defaultValue="55"),
//            @ApiImplicitParam(required= true,name="ny", value="???????????? Y ??????", paramType="query", defaultValue="127"),
//            @ApiImplicitParam(required= true,name="beachNum", value="?????? ??????", paramType = "query", defaultValue="1")
//    })
//    @RequestMapping(value = "/getVilageFcst", method = RequestMethod.GET)
//    public ApiResponse getForecastSpaceData(ApiInfo042Vo apiInfo042Vo, Locale locale) throws Exception {
//        //D:\DATA\dan\220501\DFS_SHRT_GRD_GEMD_HR01_VVV.202205011700
//        String paramChk = ResponseCheck.getParamCheck(apiInfo042Vo,"file");
//
//        if (paramChk.equals("10")) {
//            return ApiResponse.error(400, "??????????????? ?????????????????????.");
//        }else if (paramChk.equals("20")){
//            return ApiResponse.error(400,"??????1????????? ???????????? ???????????????.");
//        };
//        main();
//
//
//
//        List<String> beach_info = getBeach_info(apiInfo042Vo.getBeachNum());
//        gpsVO = gpsVO.getGrid(Double.parseDouble(beach_info.get(1)),Double.parseDouble(beach_info.get(2)));
//        apiInfo042Vo.setNx((int) gpsVO.getX());
//        apiInfo042Vo.setNy((int) gpsVO.getY());
//
//        strDate = formatDate(apiInfo042Vo,"Vilage");
//
//        apiInfo042Vo.setBaseDate(strDate.substring(0,8));
//        apiInfo042Vo.setBaseTime(strDate.substring(8,12));
//
//        DFSPointShrnData re	= apiInfo042Service.getForecastSpaceData(apiInfo042Vo);
//        result = re.getDFSShrnWeather();
//
//        ArrayList<ForecastVo> forecastVos = commonFunction.returnFrcVo(result,apiInfo042Vo.getPageNo(),apiInfo042Vo.getNumOfRows(),true);
//
//
//        return ApiResponse.ok(forecastVos,"?????? ??????");
//    };
//
//
////
////    @GetMapping("/getMidFcst")
////    public ApiResponse getMidBeach(ApiInfo042Vo apiInfo042Vo, Locale locale) throws Exception {
////        String paramChk = ResponseCheck.getParamCheck(apiInfo042Vo,"file");
////
////        if (paramChk.equals("10")) {
////            return ApiResponse.error(400, "??????????????? ?????????????????????.");
////        }else if (paramChk.equals("20")){
////            return ApiResponse.error(400,"??????1????????? ???????????? ???????????????.");
////        };
////
////        if (Integer.valueOf(apiInfo042Vo.getBase_time()) < 1200){
////            apiInfo042Vo.setBase_time("0000");
////        }else {
////            apiInfo042Vo.setBase_time("1200");
////        }
////
////        String base_date = apiInfo042Vo.getBase_date();
////        String base_time = apiInfo042Vo.getBase_time();
////
////        //?????? ?????? ??????.
////
////
////        List<String> beach_info = getBeach_info(apiInfo042Vo.getBeachNum());
////        gpsVO = gpsVO.getGrid(Double.parseDouble(beach_info.get(1)),Double.parseDouble(beach_info.get(2)));
////
////        apiInfo042Vo.setNx((int) gpsVO.getX());
////        apiInfo042Vo.setNy((int) gpsVO.getY());
////
////        ArrayList<ForecastVo> arrayLists = new ArrayList<>();
////
////        for(int i = 0; i < midCode.length ; i++) {
////            ArrayList<ForecastVo> hasg = MidForeCastNcReader.getMidVO(filePath,midCode[i], base_date, base_time, apiInfo042Vo.getNy(), apiInfo042Vo.getNx());
////            arrayLists.addAll(hasg);
////        }
////
////        return ApiResponse.ok(arrayLists,"?????? ??????");
////    }
//
//    //?????? ?????? ??????. ????????? ??????????????? ??????.
//    public static List<String> getBeach_info(int getBeachNum) {
//        beachMap.size();
//        List<String> beach_info = Arrays.asList(beachMap.get(getBeachNum));
//        return beach_info;
//    };
//
//    //?????? +9 ?????? ????????? ????????? ????????? ?????? ???????????? ??????????????????.
//    private String formatDate(ApiInfo042Vo apiInfo042Vo,String mode) throws Exception{
//
//        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmm");
//        Date baseDate = dt.parse(apiInfo042Vo.getBase_date()+ apiInfo042Vo.getBase_time());
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(baseDate);
//
//        //?????? -9 ????????? ?????? ?????????????????????.
//       //cal.add(Calendar.HOUR_OF_DAY,+9);
//
//
//        Date formatCal = cal.getTime();
//        String strDate = dt.format(formatCal);
//        Integer time = Integer.valueOf(strDate.substring(8));
//        if (time < 200){
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"2300");
//            strDate = dt.format(baseDate);
//        }else if (time < 500) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"0200");
//            strDate = dt.format(baseDate);
//        }else if (time < 800) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"0500");
//            strDate = dt.format(baseDate);
//        }else if (time < 1100) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"0800");
//            strDate = dt.format(baseDate);
//        }else if (time < 1400) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"1100");
//            strDate = dt.format(baseDate);
//        }else if (time < 1700) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"1400");
//            strDate = dt.format(baseDate);
//        }else if (time < 2000) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"1700");
//            strDate = dt.format(baseDate);
//        }else if (time < 2300) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"2000");
//            strDate = dt.format(baseDate);
//        }else if (time >= 2300 && time <=2400) {
//            cal.add(Calendar.HOUR_OF_DAY,-2);
//            formatCal = cal.getTime();
//            strDate = dt.format(formatCal);
//            baseDate = dt.parse(strDate.substring(0,8)+"2300");
//            strDate = dt.format(baseDate);
//        }
//
//
//        return strDate;
//    }
//
//
//}
