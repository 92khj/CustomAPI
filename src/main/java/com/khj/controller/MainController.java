package com.khj.controller;

import com.google.gson.Gson;
import com.khj.customize.openapi.ForecastVo;
import com.khj.customize.openapi.forecast.DFSPointShrnData;
import com.khj.customize.openapi.forecast.DFSPointVsrtData;
import com.khj.customize.openapi.forecast.DFSShrnCacher;
import com.khj.customize.openapi.forecast.DFSVsrtCacher;
import com.khj.customize.openapi.service.ApiInfo042Service;
import com.khj.customize.openapi.service.vo.ApiInfo042Vo;
import com.khj.customize.openapi.util.CommonFunction;
import com.khj.customize.utils.response.ApiResponse;
import com.khj.customize.utils.response.ResponseCheck;
import com.khj.customize.wizaiapi.midforecast.MidForeCastNcReader;
import com.khj.customize.wizaiapi.other.BeachInfo;
import com.khj.customize.wizaiapi.vo.GpsVO;

//io.swagger 버그로 인한 제거
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class MainController {

    static Map<Integer,String[]> beachMap = BeachInfo.readCSV();
    private String[] midCode = {"POP","PTY","REH","RN3","SKY","SN3","T3H","TMN","TMX","UUU","VVV","WAV"};
    GpsVO gpsVO = new GpsVO();
    String strDate;

//    String pathShrn = "D:\\DATA\\DFS\\SHRT\\GEMD\\";
//    String pathVsrt = "D:\\DATA\\DFS\\VSRT\\GEMD\\";
//    String filePath = "D:\\DATA\\DFS\\MEDM\\MERG\\";
    String pathShrn = "/DATA/DFSD/SHRT/GEMD/";
    String pathVsrt = "/DATA/DFSD/VSRT/GEMD/";
    String filePath = "/DATA/DFSD/MEDM/MERG/";

    Gson gson = new Gson();

    @RequestMapping(value = "/")
    public String test( ) {
        return "index";
    }

    List<ApiInfo042Vo> result = new ArrayList<>();


    CommonFunction commonFunction;

    @Resource(name = "apiInfo042Service")
    private ApiInfo042Service apiInfo042Service;

    public void main() {
        DFSVsrtCacher.init(pathVsrt, false);
        DFSShrnCacher.init(pathShrn, false);
    };

    //단기예보
    @RequestMapping(value = "/getShrtFcst", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getForecastSpaceData(ApiInfo042Vo apiInfo042Vo, Locale locale) throws Exception {
        //D:\DATA\dan\220501\DFS_SHRT_GRD_GEMD_HR01_VVV.202205011700
        String paramChk = ResponseCheck.getParamCheckFile(apiInfo042Vo);

		if (paramChk.equals("10")) {
			ApiResponse api = ApiResponse.error(400, "파라미터가 잘못되엇습니다.");
			String jsonByGson = gson.toJson(api);
			return jsonByGson;

		} else if (paramChk.equals("20")) {
			ApiResponse api = ApiResponse.error(400, "최근3일간의 데이터만 제공합니다.");
			String jsonByGson = gson.toJson(api);
			return jsonByGson;
		};

        main();

        List<String> beach_info = getBeach_info(apiInfo042Vo.getBeachNum());

        gpsVO = gpsVO.getGrid(Double.parseDouble(beach_info.get(1)),Double.parseDouble(beach_info.get(2)));

        apiInfo042Vo.setNx((int) gpsVO.getX());
        apiInfo042Vo.setNy((int) gpsVO.getY());

        strDate = formatDate(apiInfo042Vo,"Vilage");

        apiInfo042Vo.setBaseDate(strDate.substring(0,8));
        apiInfo042Vo.setBaseTime(strDate.substring(8,12));

        DFSPointShrnData re	= apiInfo042Service.getForecastSpaceData(apiInfo042Vo);

        result = re.getDFSShrnWeather();

        
        ArrayList<ForecastVo> forecastVos = returnFrcVo(result,true);
        gson.toJson(forecastVos);

        //파일 읽기체크
        if (fileCheck(forecastVos.size()).equals("false")){
            ApiResponse api = ApiResponse.error2(400, "해당시간 자료가 없습니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;
        };

        return gson.toJson(forecastVos);
    };

    //초단기예보
    @RequestMapping(value = {
            "/getVsrtFcst" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getForecastTimeData(ApiInfo042Vo apiInfo042Vo, Locale locale, HttpServletRequest request)
            throws Exception {
        String paramChk = ResponseCheck.getParamCheckFile(apiInfo042Vo);

		if (paramChk.equals("10")) {
			ApiResponse api = ApiResponse.error(400, "파라미터가 잘못되엇습니다.");
			String jsonByGson = gson.toJson(api);
			return jsonByGson;

		} else if (paramChk.equals("20")) {
			ApiResponse api = ApiResponse.error(400, "최근3일간의 데이터만 제공합니다.");
			String jsonByGson = gson.toJson(api);

			return jsonByGson;
		};

        main();

        List<String> beach_info = getBeach_info(apiInfo042Vo.getBeachNum());

        gpsVO = gpsVO.getGrid(Double.parseDouble(beach_info.get(1)), Double.parseDouble(beach_info.get(2)));

        apiInfo042Vo.setNx((int) gpsVO.getX());
        apiInfo042Vo.setNy((int) gpsVO.getY());

        // 시간 +9 버그 있어서 잡는중
        //strDate = formatDate(apiInfo042Vo, "Ultra");

        //apiInfo042Vo.setBaseDate(strDate.substring(0, 8));
        //apiInfo042Vo.setBaseTime(strDate.substring(8, 12));
        apiInfo042Vo.setBaseDate(apiInfo042Vo.getBase_date());
        apiInfo042Vo.setBaseTime(apiInfo042Vo.getBase_time());

        DFSPointVsrtData re = apiInfo042Service.getForecastTimeData(apiInfo042Vo);


        result = re.getDFSVsrtWeather();

        ArrayList<ForecastVo> forecastVos = returnFrcVo(result,false);

        if (fileCheck(forecastVos.size()).equals("false")){
            ApiResponse api = ApiResponse.error2(400, "해당시간 자료가 없습니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;
        };

        return gson.toJson(forecastVos);
    };



    @RequestMapping(value = {
            "/getMedmBest" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getMidBeach(ApiInfo042Vo apiInfo042Vo, Locale locale) throws Exception {
      String paramChk = ResponseCheck.getParamCheckFile(apiInfo042Vo);

		if (paramChk.equals("10")) {
			ApiResponse api = ApiResponse.error(400, "파라미터가 잘못되엇습니다.");
			String jsonByGson = gson.toJson(api);
			return jsonByGson;

		} else if (paramChk.equals("20")) {
			ApiResponse api = ApiResponse.error(400, "최근3일간의 데이터만 제공합니다.");
			String jsonByGson = gson.toJson(api);
			return jsonByGson;
		};


        //파일 시간맞춤.. 중기는 0000시에 파일한번생성 1200시에 파일한번.
        if (Integer.valueOf(apiInfo042Vo.getBase_time()) < 1200) {
            apiInfo042Vo.setBase_time("0000");
        } else {
            apiInfo042Vo.setBase_time("1200");
        }



        //파라미터 비치넘에 대한 해변정보 리스트
        List<String> beach_info = getBeach_info(apiInfo042Vo.getBeachNum());

        //beach_info에 담겨있는 위도경도를
        //GpsVO에서 위도경도를 격자값 X,Y 값으로 파씽.
        gpsVO = gpsVO.getGrid(Double.parseDouble(beach_info.get(1)), Double.parseDouble(beach_info.get(2)));

        apiInfo042Vo.setNx((int) gpsVO.getX());
        apiInfo042Vo.setNy((int) gpsVO.getY());

        //리턴값 객체 생성
        ArrayList<ForecastVo> forecastVos = new ArrayList<>();

        String base_date = apiInfo042Vo.getBase_date();
        String base_time = apiInfo042Vo.getBase_time();

        //midCode의 갯수만큼 파일 읽어들임.
        for (int i = 0; i < midCode.length; i++) {
            ArrayList<ForecastVo> hasg = MidForeCastNcReader.getMidVO(filePath, midCode[i], base_date, base_time,
                    apiInfo042Vo.getNy(), apiInfo042Vo.getNx());
            forecastVos.addAll(hasg);
        }

        //파일 읽기체크
        if (fileCheck(forecastVos.size()).equals("false")){
            ApiResponse api = ApiResponse.error2(400, "해당시간 자료가 없습니다.");
            String jsonByGson = gson.toJson(api);
            return jsonByGson;
        };


        return gson.toJson(forecastVos);
    };

    @RequestMapping(value = {
            "/beachtest" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object returnXY(){
        ArrayList<Object> result = new ArrayList<>();

        for(int i = 0; i< beachMap.size(); i++){
            List<String> beach_info = getBeach_info(i+1);
            gpsVO = gpsVO.getGrid(Double.parseDouble(beach_info.get(1)), Double.parseDouble(beach_info.get(2)));
            result.add(gpsVO);
        }

        return gson.toJson(result);
    }


    //해변 길이 체크. 짧지만 공통이라서 뺏음.
    public static List<String> getBeach_info(int getBeachNum) {
        beachMap.size();
        List<String> beach_info = Arrays.asList(beachMap.get(getBeachNum));
        return beach_info;
    };


    //시간 +9 버그 있어서 잡는중 어디서 문제 생긴건지 파악중입니다.
    private String formatDate(ApiInfo042Vo apiInfo042Vo,String mode) throws Exception{

        SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmm");
        Date baseDate = dt.parse(apiInfo042Vo.getBase_date()+ apiInfo042Vo.getBase_time());

        Calendar cal = Calendar.getInstance();
        cal.setTime(baseDate);

        //시간 -9 안되면 이거 빼면해결됩니다.
        //cal.add(Calendar.HOUR_OF_DAY,+9);


        Date formatCal = cal.getTime();
        String strDate = dt.format(formatCal);
        Integer time = Integer.valueOf(strDate.substring(8));



        if (time < 200){
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"2300");
            strDate = dt.format(baseDate);
        }else if (time < 500) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"0200");
            strDate = dt.format(baseDate);
        }else if (time < 800) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"0500");
            strDate = dt.format(baseDate);
        }else if (time < 1100) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"0800");
            strDate = dt.format(baseDate);
        }else if (time < 1400) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"1100");
            strDate = dt.format(baseDate);
        }else if (time < 1700) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"1400");
            strDate = dt.format(baseDate);
        }else if (time < 2000) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"1700");
            strDate = dt.format(baseDate);
        }else if (time < 2300) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"2000");
            strDate = dt.format(baseDate);
        }else if (time >= 2300 && time <=2400) {
            cal.add(Calendar.HOUR_OF_DAY,-2);
            formatCal = cal.getTime();
            strDate = dt.format(formatCal);
            baseDate = dt.parse(strDate.substring(0,8)+"2300");
            strDate = dt.format(baseDate);
        }


        return strDate;
    }

    public ArrayList<ForecastVo> returnFrcVo(List<ApiInfo042Vo> object,boolean bug) throws ParseException {

        ArrayList<ForecastVo> forecastVos = new ArrayList();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm",Locale.ENGLISH);

        for (int i = 0; i < object.size(); i++) {
            if (bug == true) {
                String baseStrDate = object.get(i).getBaseDate() + object.get(i).getBaseTime();
                String fcstStrDate = object.get(i).getFcstDate() + object.get(i).getFcstTime();

                Date baseDate = format.parse(baseStrDate);
                Date fcstDate = format.parse(fcstStrDate);


                Calendar calendar = Calendar.getInstance();


                calendar.setTime(baseDate);

                Date formatCal = calendar.getTime();
                baseStrDate = format.format(formatCal);

                calendar.setTime(fcstDate);

                formatCal = calendar.getTime();
                fcstStrDate = format.format(formatCal);


                ForecastVo forecastVo = new ForecastVo(baseStrDate.substring(0,8)
                        , baseStrDate.substring(8)
                        , object.get(i).getCategory()
                        , fcstStrDate.substring(0,8)
                        , fcstStrDate.substring(8)
                        , object.get(i).getFcstValue()
                        , object.get(i).getNx()
                        , object.get(i).getNy());

                forecastVos.add(forecastVo);
            }else if(bug == false){
                ForecastVo forecastVo = new ForecastVo(object.get(i).getBaseDate()
                        , object.get(i).getBaseTime()
                        , object.get(i).getCategory()
                        , object.get(i).getFcstDate()
                        , object.get(i).getFcstTime()
                        , object.get(i).getFcstValue()
                        , object.get(i).getNx()
                        , object.get(i).getNy());
                forecastVos.add(forecastVo);
            }
        }
        return forecastVos;
    };

    public String fileCheck(int size) {
        if (size == 0){
          return "false";
        }
        else {
            return "true";
        }
    };



}