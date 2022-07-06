package com.khj.customize.powerweatherapi.module;

import com.google.gson.Gson;
import com.khj.customize.powerweatherapi.vo.PowerWeatherVo;
import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("PowerWeatherJsonParse")
public class PowerWeatherJsonParse {

    Gson gson = new Gson();

    //파일 경로
    //String pathPw = "D:\\API\\BIGDATA\\elec_index\\service_";
    String pathPw = "/API/BIGDATA/elec_index/service_";
    @SneakyThrows
    public ArrayList<PowerWeatherVo> returnPowerWeatherVo(String base_date, String base_time, Long x, Long y) throws FileNotFoundException {
        //결과물 리턴용 Vo 리스트 생성
        ArrayList<PowerWeatherVo> result = new ArrayList();
        SimpleDateFormat sdfYMDHm = new SimpleDateFormat("yyyyMMddHH");
        // 입력받은 KST 기준 날짜에 맞는 UTC기반 파일찾기위해, KST -> UTC 변환
        Date str_to_date = sdfYMDHm.parse(base_date + base_time.substring(0,2));
        //System.out.println("KST time : " + base_date + base_time);
        Calendar cal = Calendar.getInstance();
        cal.setTime(str_to_date);
        cal.add(Calendar.HOUR, -9);
        String utc_baseTime = sdfYMDHm.format(cal.getTime());
        //System.out.println("UTC time : " + utc_baseTime);

        String file_yyyy = utc_baseTime.substring(0, 4);
        String file_mm = utc_baseTime.substring(4, 6);
        String file_dd = utc_baseTime.substring(6, 8);
        String file_hh = utc_baseTime.substring(8, 10);

        // 운영서버 경로
        //File dir = new File(pathPw + file_yyyy +"/"+ file_mm +"/"+ file_dd +"/"+ file_hh +"/");
        // 개발버전 경로
        String filepath = pathPw + file_yyyy + File.separator +"3.output"+File.separator+ file_yyyy + File.separator + file_mm + File.separator + file_dd + File.separator + file_hh + File.separator;

        File dir = new File(filepath);
        //json 파일만 읽도록 필터링
        FileFilter filter = new FileFilter() {
            public boolean accept(File f) {
                return f.getName().endsWith("json");
            }
        };

        //json 파일경로 리스트 생성
        if (dir.listFiles(filter) == null){
            return result;
        };

        File files[] = dir.listFiles(filter);

        Arrays.sort(files,
                new Comparator<Object>()
                {
                    @Override
                    public int compare(Object object1, Object object2) {

                        String s1 = "";
                        String s2 = "";

                        s1 = ((File)object1).getName();
                        s2 = ((File)object2).getName();

                        return s1.compareTo(s2);

                    }
                });


        //json 파일 하나마다 반복
        for (File Jsonfile : files) {
            //System.out.println("Read file : " + Jsonfile);

            // UTC기반 파일별 예보시간을 KST형태로 리턴하기위해, UTC -> KST 변환
            Calendar cal2 = Calendar.getInstance();
            Date str_to_date2 = sdfYMDHm.parse(utc_baseTime);
            cal2.setTime(str_to_date2);
            //파일 명에 붙어있는 예보시간을 추출
            String file_name_hr[] = Jsonfile.toString().split("_");
            int FcstHr = Integer.parseInt(file_name_hr[file_name_hr.length-1].substring(0,2));

            // KST로 변환 위한 9시간 + 파일명 예보시간 hr 합산.
            cal2.add(Calendar.HOUR, +(FcstHr+9));
            String kst_FcstTime = sdfYMDHm.format(cal2.getTime());

            // Json 파일 읽기
            FileReader fr = new FileReader(Jsonfile);
            JSONParser jsonParse = new JSONParser();
            JSONArray jsonArray = (JSONArray)jsonParse.parse(fr);

            // array 전체 중 입력받은 x, y 와 모두 일치하는 케이스 탐색
            for (int i = 0; i < jsonArray.size(); ++i) {

                JSONObject jo = (JSONObject)jsonArray.get(i);
                if(jo.get("x").equals(x) && jo.get("y").equals(y) ) {

                    // x, y 일치하는 케이스의 "data" 오브젝트 파싱 및 리턴데이터 생성
                    JSONObject obj = (JSONObject)jo.get("data");
                    PowerWeatherVo powerWeatherVo = new PowerWeatherVo(kst_FcstTime+"00", obj.get("elec_idx"), obj.get("sts"), obj.get("wbgt"), obj.get("t1h"), obj.get("reh"), obj.get("wsd"));
                    result.add(powerWeatherVo);
                }
            }

            // json 파일 close
            fr.close();

        } // 파일당 반복 for 구간 종료



        return result;
    }
}
