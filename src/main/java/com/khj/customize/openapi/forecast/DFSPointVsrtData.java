package com.khj.customize.openapi.forecast;

import com.khj.customize.openapi.service.vo.ApiInfo042Vo;
import com.khj.customize.openapi.util.StringUtils;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 동네예보 실황 시계열 데이터
 * 0 : "PTY" 강수형태
 * 1 : "RN1" 1시간 강수량
 * 2 : "SKY" 하늘상태
 * <br /><br />
 * 역할 : 원시 데이터의 가공 처리를 담당
 * @author Seagull
 */
public class DFSPointVsrtData {


    /** 타임 스탬프 1시간별 기온을 기준으로 잡 (Config_Vsrt.V_PTY)*/
    long timeStamp;
    public long getTimeStamp() { return timeStamp; }
    public void setTimeStamp(long ts) { this.timeStamp = ts; }
    public void updateTimeStamp() {
        if(this.pty != null) {
            if(timeStamp > 0) {
                this.timeStamp = pty.getTimeStamp();
                if(timeStamp < pty.getTimeStamp()) {
                    this.version++;
                }
            } else {
                this.timeStamp = pty.getTimeStamp();
                this.version = 1;
            }
        }
    }
    /** 버전 **/
    int version;
    public int getVersion() {
        return version;
    }

    /** 실시간 여부 : true : 실시간 , false : 최근 */
    boolean isLatest = true;
    public boolean IsLatest() { return this.isLatest; }
    public void setLatest(boolean l) { this.isLatest = l; }

    public DFSPointVsrtData() {
        super();
        this.dfsTs = new DFSVsrtTimeSequence();
        this.x = 0;
        this.y = 0;
        this.version = 1;
        this.timeStamp = 0L;
    }
    /** 동네예보 발표시각 */
    DFSVsrtTimeSequence dfsTs;
    public DFSVsrtTimeSequence getAnnTime() { return this.dfsTs; }
    public void setAnnTime(DFSVsrtTimeSequence ts) { this.dfsTs = ts; }

    /** 지점 좌표 X */
    int x;
    public int getX() {return x;}
    public void setX(int x) { this.x = x; }

    /** 지점 좌표 Y */
    int y;
    public int getY() { return y; }
    public void setY(int y) { this.y = y;}

    /** 12시간  강수량 */
    DFSVsrtRaw rn1;
    public DFSVsrtRaw getRn1() { return this.rn1; }
    public void setRn1(DFSVsrtRaw data) { this.rn1 = data; }

    /** 하늘상태 */
    DFSVsrtRaw sky;
    public DFSVsrtRaw getSky() { return this.sky; }
    public void setSky(DFSVsrtRaw data) { this.sky = data; }

    /** 강수 형태 */
    DFSVsrtRaw pty;
    public DFSVsrtRaw getPty() { return this.pty; }
    public void setPty(DFSVsrtRaw data) { this.pty = data; }

    DFSVsrtRaw lgt;
    public DFSVsrtRaw getLgt() { return this.lgt; }
    public void setLgt(DFSVsrtRaw data) { this.lgt = data; }

    DFSVsrtRaw t1h;
    DFSVsrtRaw reh;
    DFSVsrtRaw uuu;
    DFSVsrtRaw vvv;
    DFSVsrtRaw vec;
    DFSVsrtRaw wsd;

    public DFSVsrtRaw getT1h() {
        return t1h;
    }
    public void setT1h(DFSVsrtRaw t1h) {
        this.t1h = t1h;
    }
    public DFSVsrtRaw getReh() {
        return reh;
    }
    public void setReh(DFSVsrtRaw reh) {
        this.reh = reh;
    }
    public DFSVsrtRaw getUuu() {
        return uuu;
    }
    public void setUuu(DFSVsrtRaw uuu) {
        this.uuu = uuu;
    }
    public DFSVsrtRaw getVvv() {
        return vvv;
    }
    public void setVvv(DFSVsrtRaw vvv) {
        this.vvv = vvv;
    }
    public DFSVsrtRaw getVec() {
        return vec;
    }
    public void setVec(DFSVsrtRaw vec) {
        this.vec = vec;
    }
    public DFSVsrtRaw getWsd() {
        return wsd;
    }
    public void setWsd(DFSVsrtRaw wsd) {
        this.wsd = wsd;
    }

    /**
     * 풍향 벡터로 부터 풍속 구하기
     * @param u
     * @param v
     * @return
     */
    static double getWsFromUV(float u, float v){
        double ws = 0.0;
        if (u > -90 && v > -90) {
            ws = 0.1*(int)(10.0*Math.sqrt(u*u + v*v) + 0.5);
        }
        else {
            ws = -999.0;
        }
        return ws;
    }
    /**
     * 풍향 벡터로 부터 풍향 구하기
     * @param u
     * @param v
     * @return
     */
    static int getWdFromUV(float u, float v) {
        int wd = -1;
        double  theta;
        if (u > -90 && v > -90) {
            theta = Math.atan2(v,u)*180.0/Math.PI;
            if (theta < 0.0) theta += 360.0;
            wd = ((int)(360.0 - theta + 270.0 + 22.5) % 360) / 45;
        }
        else {
            wd = -1;
        }
        return wd;
    }
    List<ApiInfo042Vo> weather;
    public List<ApiInfo042Vo> getDFSVsrtWeather() {
        return this.weather;
    }
    public List<ApiInfo042Vo> getPagedItems(int pageNo, int numOfRows) {
        List<ApiInfo042Vo> result = new ArrayList<ApiInfo042Vo>();
        int begin = numOfRows*(pageNo-1);
        int end = numOfRows*(pageNo);
        if(weather != null) {
            if(begin == 0 && end >= weather.size()) {
                result = weather;
            } else {
                for(int i = begin ; i < end ; i++) {
                    if(i >= 0 && i < weather.size()) {
                        result.add(weather.get(i));
                    }
                }
            }
        }
        return result;
    }

    public void initDFSVsrtWeather() {
        int fctCnt = Config_Vsrt.FCT_CNT_EQ[Config_Vsrt.V_SKY][this.getAnnTime().getTmSeq()];
        weather = new ArrayList<ApiInfo042Vo>();
        Calendar kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 30);
        String kstBaseTm = StringUtils.getFormattedDate(kstCal.getTime(), "yyyyMMddHHmm");
        kstCal.set(Calendar.MINUTE, 0);
//		if(weather.size() > 0) {

//        for(int i = 0 ; i < fctCnt ; i++) {
//            if (this.getLgt() != null) {
//                kstCal.add(Calendar.HOUR_OF_DAY, 1);
//                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_LGT], kstBaseTm, kstCal, Integer.toString((int) (this.getLgt().getData()[i]))));
//            }
//
//        }


        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getPty() != null) {
            for(int i = 0 ; i < fctCnt ; i++) {
                kstCal.add(Calendar.HOUR_OF_DAY, 1);
                /* PTY */
                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_PTY], kstBaseTm, kstCal, Integer.toString((int) (this.getPty().getData()[i]))));
                }
        }
        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getRn1() != null) {
            for(int i = 0 ; i < fctCnt ; i++) {
                kstCal.add(Calendar.HOUR_OF_DAY, 1);
                /* RN1 */
                //weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_RN1], kstBaseTm, kstCal, StringUtils.toFormattedNumber(StringUtils.round(this.getRn1().getData()[i]*10)/10f,1,false)));
                this.addRN1(kstBaseTm, kstCal, Float.parseFloat(StringUtils.toFormattedNumber(this.getRn1().getData()[i], 1, false)));
            }
        }
        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if (this.getSky() != null) {
            for(int i = 0 ; i < fctCnt ; i++) {

            kstCal.add(Calendar.HOUR_OF_DAY, 1);

            /* SKY */
            weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_SKY], kstBaseTm, kstCal, Integer.toString((int) (this.getSky().getData()[i]))));
            }
        }

        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getT1h() != null) {
            for(int i = 0 ; i < fctCnt ; i++) {
                kstCal.add(Calendar.HOUR_OF_DAY, 1);

                /* T1H */
                // 20220105 초단기예보 값은 정수로 표출 되어야 하는데 반올림 부분 때문에 소숫점으로 표출되고 있어 해당 부분 주석처리 함
                //weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_T1H], kstBaseTm, kstCal, StringUtils.toFormattedNumber(StringUtils.round(this.getT1h().getData()[i]*10f)/10f,1,false)));
                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_T1H], kstBaseTm, kstCal, StringUtils.toFormattedNumber(this.getT1h().getData()[i], 1, false)));
            }
        }

        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getReh() != null) {
            for(int i = 0 ; i < fctCnt ; i++) {
                kstCal.add(Calendar.HOUR_OF_DAY, 1);
                /* REH */
                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_REH], kstBaseTm, kstCal, Integer.toString(StringUtils.round(this.getReh().getData()[i]))));
                }
        }

        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getUuu() != null) {
            for(int i = 0 ; i < fctCnt ; i++) {

                kstCal.add(Calendar.HOUR_OF_DAY, 1);

                /* UUU */
                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_UUU], kstBaseTm, kstCal, StringUtils.toFormattedNumber(StringUtils.round(this.getUuu().getData()[i] * 10) / 10f, 1, false)));
            }
        }

        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getVvv() != null) {
            for (int i = 0; i < fctCnt; i++) {
                kstCal.add(Calendar.HOUR_OF_DAY, 1);
                /* VVV */
                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_VVV], kstBaseTm, kstCal, StringUtils.toFormattedNumber(StringUtils.round(this.getVvv().getData()[i] * 10) / 10f, 1, false)));
            }
        }

        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getVec() != null) {
            for (int i = 0; i < fctCnt; i++) {
                kstCal.add(Calendar.HOUR_OF_DAY, 1);

                /* VEC */
                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_VEC], kstBaseTm, kstCal, Integer.toString(StringUtils.round(this.getVec().getData()[i]))));
            }
        }

        kstCal = (Calendar)dfsTs.getKstCal().clone();
        kstCal.set(Calendar.MINUTE, 0);
        if(this.getWsd() != null) {
            for (int i = 0; i < fctCnt; i++) {
                kstCal.add(Calendar.HOUR_OF_DAY, 1);

                /* WSD */
                weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_WSD], kstBaseTm, kstCal, StringUtils.toFormattedNumber(StringUtils.round(this.getWsd().getData()[i] * 10) / 10f, 1, false)));
            }
        }
	//	}
    }

    // 20210709 김경옥 주무관님 변경 요청 : 가이드에는 범주로 되어 있는데 데이터만 나오고 있음 범주로 변경 요청
    private void addRN1(String kstBaseTm, final Calendar kstCal, float rn1Value) {
        rn1Value = Math.round(rn1Value);
        String rn1ToString = "";

        //System.out.println("RN1=============="+rn1Value);

        if (rn1Value == 0.0) rn1ToString = "강수없음";
        else if(rn1Value >= 0 && rn1Value < 1) rn1ToString = "1.0mm 미만";
        else if (rn1Value >= 1 && rn1Value < 30) rn1ToString = rn1Value +"mm";
        else if (rn1Value >= 30 && rn1Value <= 50) rn1ToString = "30.0~50.0mm";
        else if (rn1Value > 50) rn1ToString = "50.0mm 이상";
        else rn1ToString = "강수없음";

        weather.add(makeValue(Config_Vsrt.VCODE[Config_Vsrt.V_RN1], kstBaseTm, kstCal, rn1ToString));
    }

    private ApiInfo042Vo makeValue(String vCode, String kstBaseTm, final Calendar kstCal, String value) {
        ApiInfo042Vo w = new ApiInfo042Vo();
        w.setBaseDate(kstBaseTm.substring(0,8));
        w.setBaseTime(kstBaseTm.substring(8));
        w.setCategory(vCode);

        String fcstDateTime = StringUtils.getFormattedDate(kstCal.getTime(), "yyyyMMddHHmm");
        w.setFcstDate(fcstDateTime.substring(0,8));
        w.setFcstTime(fcstDateTime.substring(8));
        w.setFcstValue(value);

        w.setNx(x);
        w.setNy(y);
        return w;
    }
    public int getTotalItemCount() {
        return weather==null?0:weather.size();
    }

} // end class
