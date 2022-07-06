package com.khj.customize.powerweatherapi.controller;

import com.google.gson.Gson;
import com.khj.customize.powerweatherapi.module.PowerWeatherJsonParse;
import com.khj.customize.powerweatherapi.vo.PowerWeatherInVo;
import com.khj.customize.powerweatherapi.vo.PowerWeatherVo;
import com.khj.customize.utils.response.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class PowerWeatherController {

    @Autowired
    PowerWeatherJsonParse powerWeatherJsonParse;

    Gson gson = new Gson();

    @RequestMapping(value = {"/getPowerWeather" }, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getPowerWeatherData(PowerWeatherInVo powerweatherinVo, Locale locale, HttpServletRequest request) throws  Exception{

        if (powerweatherinVo.getBase_date() == null) {
            //System.out.println("base date : " + powerweatherinVo.getBase_date());
            ApiResponse apiResponse = ApiResponse.error(101, "base_date parameter is missing");
            return gson.toJson(apiResponse);
        }

        if (powerweatherinVo.getBase_date().length() != 8) {
            //System.out.println("base date : " + powerweatherinVo.getBase_date());
            ApiResponse apiResponse = ApiResponse.error(101, "base_date parameter is missing");
            return gson.toJson(apiResponse);
        }

        if (powerweatherinVo.getBase_time() == null) {
            //System.out.println("base time : " + powerweatherinVo.getBase_time());
            ApiResponse apiResponse = ApiResponse.error(102, "base_time parameter is missing");
            return gson.toJson(apiResponse);
        }

        if (powerweatherinVo.getBase_time().length() != 4) {
            //System.out.println("base time : " + powerweatherinVo.getBase_time());
            ApiResponse apiResponse = ApiResponse.error(102, "base_time parameter is missing");
            return gson.toJson(apiResponse);
        }

        if (powerweatherinVo.getBase_time().equals("0200")){

        }else if(powerweatherinVo.getBase_time().equals("0500")){

        }else if(powerweatherinVo.getBase_time().equals("0800")){

        }else if(powerweatherinVo.getBase_time().equals("1100")){

        }else if(powerweatherinVo.getBase_time().equals("1400")){

        }else if(powerweatherinVo.getBase_time().equals("1700")){

        }else if(powerweatherinVo.getBase_time().equals("2000")){

        }else if(powerweatherinVo.getBase_time().equals("2300")){

        }else {
            ApiResponse apiResponse = ApiResponse.error(102, "base_time parameter is missing");
            return gson.toJson(apiResponse);
        }



        if (powerweatherinVo.getX() == null) {
            //System.out.println("x : " + powerweatherinVo.getX());
            ApiResponse apiResponse = ApiResponse.error(103, "x parameter is missing");
            return gson.toJson(apiResponse);
        }
        if (powerweatherinVo.getY() == null) {
            //System.out.println("y : " + powerweatherinVo.getY());
            ApiResponse apiResponse = ApiResponse.error(104, "y parameter is missing");
            return gson.toJson(apiResponse);
        }

        if (powerweatherinVo.getX() < 1 || powerweatherinVo.getX() > 149) {
            ApiResponse apiResponse = ApiResponse.error(111, "x parameter value out of range (range : 1~149)");
            return gson.toJson(apiResponse);
        }
        if (powerweatherinVo.getY() < 1 || powerweatherinVo.getY() > 253) {
            ApiResponse apiResponse = ApiResponse.error(112, "y parameter value out of range (range : 1~253)");
            return gson.toJson(apiResponse);
        }

        //예외처리 다되고 나면 날짜, x, y 파라미터 보내서 결과 리턴 받음
        ArrayList<PowerWeatherVo> powerweatherVos = powerWeatherJsonParse.returnPowerWeatherVo(powerweatherinVo.getBase_date(),powerweatherinVo.getBase_time(),powerweatherinVo.getX(),powerweatherinVo.getY());

        if (powerweatherVos.size() == 0) {
            ApiResponse apiResponse = ApiResponse.error(400,"해당시간 자료가 없습니다.");
            return gson.toJson(apiResponse);
        }

        return gson.toJson(powerweatherVos);

    }


}
