package com.khj.customize.openapi.forecast;

import com.khj.customize.openapi.service.vo.ApiInfo042Vo;
import com.khj.customize.openapi.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 동네예보 실황 시계열 데이터
 * 0 : "T1H" 1시간별 기온
 * 1 : "REH" 상대 습도
 * X : "SKY" 하늘상태 - 20181010 삭제
 * 2 : "UUU" 동서방향풍속
 * 3 : "VVV" 남북방향풍속
 * 4 : "PTY" 강수형태
 * 5 : "RN1" 강수량
 * X : "LGT" 낙뢰 여부 - 20181010 삭제
 * 6 : "VEC" 풍향 degree
 * 7 : "WSD" 풍속 ms?
 *
 * <br /><br />
 * 역할 : 원시 데이터의 가공 처리를 담당
 * @author Seagull
 *
 */
public class DFSPointOdamData {

    /** 타임 스탬프 1시간별 기온을 기준으로 잡 (Config_Odam.V_T1H)*/
    long timeStamp;
    public long getTimeStamp() { return timeStamp; }
    public void setTimeStamp(long ts) { this.timeStamp = ts; }
    public void updateTimeStamp() {
        if(this.t1h != null) {
            if(timeStamp > 0) {
                this.timeStamp = t1h.getTimeStamp();
                if(timeStamp < t1h.getTimeStamp()) {
                    this.version++;
                }
            } else {
                this.timeStamp = t1h.getTimeStamp();
                this.version = 1;
            }

        }
    }
    /** 버전 **/
    int version;
    public int getVersion() {
        return version;
    }

    /** 실시간 여부 : true : 실시간 , false 	: 최근 */
    boolean isLatest = true;
    public boolean IsLatest() { return this.isLatest; }
    public void setLatest(boolean l) { this.isLatest = l; }

    public DFSPointOdamData() {
        super();
        this.dfsTs = new DFSOdamTimeSequence();
        this.x = 0;
        this.y = 0;
        this.version = 1;
        this.timeStamp = 0L;
    }
    /** 동네예보 발표시각 */
    DFSOdamTimeSequence dfsTs;
    public DFSOdamTimeSequence getAnnTime() { return this.dfsTs; }
    public void setAnnTime(DFSOdamTimeSequence ts) { this.dfsTs = ts; }


    /** 지점 좌표 X */
    int x;
    public int getX() {return x;}
    public void setX(int x) { this.x = x; }

    /** 지점 좌표 Y */
    int y;
    public int getY() { return y; }
    public void setY(int y) { this.y = y;}

    /** 1시간별 기온 시계열 */
    DFSOdamRaw t1h;
    public DFSOdamRaw getT1h() { return this.t1h;}
    public void setT1h(DFSOdamRaw data) { this.t1h = data; }

    /** 12시간  강수량 */
    DFSOdamRaw rn1;
    public DFSOdamRaw getRn1() { return this.rn1; }
    public void setRn1(DFSOdamRaw data) { this.rn1 = data; }

    /** 하늘상태 */
    //DFSOdamRaw sky;
    //public DFSOdamRaw getSky() { return this.sky; }
    //public void setSky(DFSOdamRaw data) { this.sky = data; }

    /** 강수 형태 */
    DFSOdamRaw pty;
    public DFSOdamRaw getPty() { return this.pty; }
    public void setPty(DFSOdamRaw data) { this.pty = data; }

    /** 동서방향 풍속 */
    DFSOdamRaw uuu;
    public DFSOdamRaw getUuu() { return this.uuu; }
    public void setUuu(DFSOdamRaw data) { this.uuu = data; }

    /** 남북 방향 풍속 */
    DFSOdamRaw vvv;
    public DFSOdamRaw getVvv() { return this.vvv; }
    public void setVvv(DFSOdamRaw data) { this.vvv = data; }

    /** 습도 */
    DFSOdamRaw reh;
    public DFSOdamRaw getReh() { return this.reh; }
    public void setReh(DFSOdamRaw data) { this.reh = data; }

    /** 낙뢰 */
    //DFSOdamRaw lgt;
    //public DFSOdamRaw getLgt() { return this.lgt; }
    //public void setLgt(DFSOdamRaw data) { this.lgt = data; }

    /** 풍향(degree) */
    DFSOdamRaw vec;
    public DFSOdamRaw getVec() { return this.vec; }
    public void setVec(DFSOdamRaw data) { this.vec = data; }

    /** 풍속(ms?) */
    DFSOdamRaw wsd;
    public DFSOdamRaw getWsd() { return this.wsd; }
    public void setWsd(DFSOdamRaw data) { this.wsd = data; }

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
    public List<ApiInfo042Vo> getDFSOdamWeather() {
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

    public void initDFSOdamWeather() {
        // 예보 시간 체크  mhh 확인사항
        int fctCnt = Config_Odam.FCT_CNT[Config_Odam.V_T1H][this.getAnnTime().getTmSeq()];
        weather = new ArrayList<ApiInfo042Vo>();
        String baseKstTm = dfsTs.getKstTm();
        for(int i = 0 ; i < fctCnt ; i++) {
            //weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_LGT], baseKstTm, Integer.toString((int)this.getLgt().getData()[i])));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_PTY], baseKstTm, Integer.toString((int)this.getPty().getData()[i])));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_REH], baseKstTm, Integer.toString(StringUtils.round(this.getReh().getData()[i]))));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_RN1], baseKstTm, StringUtils.toFormattedNumber(StringUtils.round(this.getRn1().getData()[i]*10)/10f,1,false)));
            //weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_SKY], baseKstTm, Integer.toString((int)this.getSky().getData()[i])));
            //20220105 T1H값 음수일 경우 반올림 문제로 값 변형이 발생되어 해당부분 주석 처리함
            //weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_T1H], baseKstTm, StringUtils.toFormattedNumber(StringUtils.round(this.getT1h().getData()[i]*10)/10f,1,false)));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_T1H], baseKstTm, StringUtils.toFormattedNumber(this.getT1h().getData()[i],1,false)));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_UUU], baseKstTm, StringUtils.toFormattedNumber(StringUtils.round(this.getUuu().getData()[i]*10)/10f,1,false)));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_VEC], baseKstTm, Integer.toString(StringUtils.round(this.getVec().getData()[i]))));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_VVV], baseKstTm, StringUtils.toFormattedNumber(StringUtils.round(this.getVvv().getData()[i]*10)/10f,1,false)));
            weather.add(makeValue(Config_Odam.VCODE[Config_Odam.V_WSD], baseKstTm, StringUtils.toFormattedNumber(StringUtils.round(this.getWsd().getData()[i]*10)/10f,1,false)));
        }
    }
    private ApiInfo042Vo makeValue(String vCode, String kstBaseTm, String value) {
        ApiInfo042Vo w = new ApiInfo042Vo();
        w.setBaseDate(kstBaseTm.substring(0,8));
        w.setBaseTime(kstBaseTm.substring(8));
        w.setCategory(vCode);
        w.setNx(x);
        w.setNy(y);
        w.setObsrValue(value);
        return w;
    }
    public int getTotalItemCount() {
        return weather==null?0:weather.size();
    }
} // end class
