package com.khj.customize.openapi.forecast;


import com.khj.customize.openapi.service.vo.ApiInfo042Vo;
import com.khj.customize.openapi.util.StringUtils;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DFSPointShrnData {


    /** 타임 스탬프 3시간별 기온을 기준으로 잡 (Config_Shrn.V_T3H)*/
    long timeStamp;
    public long getTimeStamp() { return timeStamp; }
    public void setTimeStamp(long ts) { this.timeStamp = ts; }
    public void updateTimeStamp() {
        if(this.tmp != null) {
            if(timeStamp > 0) {
                this.timeStamp = tmp.getTimeStamp();
                if(timeStamp < tmp.getTimeStamp()) {
                    this.version++;
                }
            } else {
                this.timeStamp = tmp.getTimeStamp();
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

    public DFSPointShrnData() {
        super();
        this.dfsTs = new DFSShrnTimeSequence();
        this.x = 0;
        this.y = 0;
        this.version = 1;
        this.timeStamp = 0L;

    }
    /** 동네예보 발표시각 */
    DFSShrnTimeSequence dfsTs;
    public DFSShrnTimeSequence getAnnTime() { return this.dfsTs; }
    public void setAnnTime(DFSShrnTimeSequence ts) { this.dfsTs = ts; }

    /** 지점 좌표 X */
    int x;
    public int getX() {return x;}
    public void setX(int x) { this.x = x; }

    /** 지점 좌표 Y */
    int y;
    public int getY() { return y; }
    public void setY(int y) { this.y = y;}

    /** 1시간별 기온 시계열 */
    DFSShrnRaw tmp;
    public DFSShrnRaw getTmp() { return this.tmp;}
    public void setTmp(DFSShrnRaw data) { this.tmp = data; }

    /** 최고기온 */
    DFSShrnRaw tmx;
    public DFSShrnRaw getTmx() { return this.tmx; }
    public void setTmx(DFSShrnRaw data) { this.tmx = data; }

    /** 최저기온 */
    DFSShrnRaw tmn;
    public DFSShrnRaw getTmn() { return this.tmn; }
    public void setTmn(DFSShrnRaw data) { this.tmn = data; }

    /** 동서방향 풍속 */
    DFSShrnRaw uuu;
    public DFSShrnRaw getUuu() { return this.uuu; }
    public void setUuu(DFSShrnRaw data) { this.uuu = data; }

    /** 남북 방향 풍속 */
    DFSShrnRaw vvv;
    public DFSShrnRaw getVvv() { return this.vvv; }
    public void setVvv(DFSShrnRaw data) { this.vvv = data; }

    DFSShrnRaw vec;
    public DFSShrnRaw getVec() { return vec; }
    public void setVec(DFSShrnRaw vec) { this.vec = vec; }

    DFSShrnRaw wsd;
    public DFSShrnRaw getWsd() { return wsd; }
    public void setWsd(DFSShrnRaw wsd) { this.wsd = wsd; }

    /** 하늘상태 */
    DFSShrnRaw sky;
    public DFSShrnRaw getSky() { return this.sky; }
    public void setSky(DFSShrnRaw data) { this.sky = data; }

    /** 강수 형태 */
    DFSShrnRaw pty;
    public DFSShrnRaw getPty() { return this.pty; }
    public void setPty(DFSShrnRaw data) { this.pty = data; }

    /** 강수 확률 */
    DFSShrnRaw pop;
    public DFSShrnRaw getPop() { return this.pop; }
    public void setPop(DFSShrnRaw data) { this.pop = data; }

    /** 강수 확률 */
    DFSShrnRaw pcp;
    public DFSShrnRaw getPcp() { return this.pcp; }
    public void setPcp(DFSShrnRaw data) { this.pcp = data; }

    /** 적설 */
    DFSShrnRaw sno;
    public DFSShrnRaw getSno() { return this.sno; }
    public void setSno(DFSShrnRaw data) { this.sno = data; }

    /** 습도 */
    DFSShrnRaw reh;
    public DFSShrnRaw getReh() { return this.reh; }
    public void setReh(DFSShrnRaw data) { this.reh = data; }

    /** 파고 */
    DFSShrnRaw wav;
    public DFSShrnRaw getWav() { return this.wav; }
    public void setWav(DFSShrnRaw data) { this.wav = data; }

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
    public List<ApiInfo042Vo> getDFSShrnWeather() {
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

    public void initDFSShrnWeather() {
        int fctCnt = Config_Shrn.FCT_CNT[Config_Shrn.V_TMP][this.getAnnTime().getTmSeq()];
        weather = new ArrayList<ApiInfo042Vo>();

        String kstBaseTm = StringUtils.getFormattedDate(dfsTs.getKstCal().getTime(), "yyyyMMddHHmm");
        Calendar kstCal = (Calendar)getAnnTime().getKstCal().clone();
        kstCal.set(Calendar.MINUTE,0);
        kstCal.add(Calendar.HOUR_OF_DAY,1);
        Calendar timeCal = (Calendar)getAnnTime().getUtcCal().clone();
        // to KST
        timeCal.add(Calendar.HOUR_OF_DAY, 9);
        kstCal.add(Calendar.HOUR_OF_DAY,-1);
        kstCal.add(Calendar.HOUR_OF_DAY,0);
        timeCal.add(Calendar.MINUTE, -1);
        Calendar today = (Calendar)timeCal.clone();
        int t1 = ((today.get(Calendar.YEAR)*100 + today.get(Calendar.MONTH))*100+ today.get(Calendar.DAY_OF_MONTH));
        today.add(Calendar.DAY_OF_MONTH, 1);
        int t2 = ((today.get(Calendar.YEAR)*100 + today.get(Calendar.MONTH))*100+ today.get(Calendar.DAY_OF_MONTH));
        today.add(Calendar.DAY_OF_MONTH, 1);
        int t3 = ((today.get(Calendar.YEAR)*100 + today.get(Calendar.MONTH))*100+ today.get(Calendar.DAY_OF_MONTH));
        today.add(Calendar.DAY_OF_MONTH, 1);
        int t4 = ((today.get(Calendar.YEAR)*100 + today.get(Calendar.MONTH))*100+ today.get(Calendar.DAY_OF_MONTH));

        for(int i = 0 ; i < fctCnt ; i++) {
//			timeCal.add(Calendar.HOUR_OF_DAY,3); // kst
            timeCal.add(Calendar.HOUR_OF_DAY,1); // kst
//			kstCal.add(Calendar.HOUR_OF_DAY,3); // kst
            kstCal.add(Calendar.HOUR_OF_DAY,1); // kst

            int wHour = timeCal.get(Calendar.HOUR_OF_DAY)+1;
            /* 0, 1, 2 ( 오늘, 내일 , 모레) */
            int dayNum = 0;
            int tt = ((timeCal.get(Calendar.YEAR)*100 + timeCal.get(Calendar.MONTH))*100+ timeCal.get(Calendar.DAY_OF_MONTH));
            if(tt == t1) dayNum = 0;
            else if(tt == t2) dayNum = 1;
            else if(tt == t3) dayNum = 2;
            else dayNum = 3;


            /* TMP */
            if(this.getTmp() != null) {
                weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMP], kstBaseTm, kstCal, StringUtils.toFormattedNumber(Math.round(this.getTmp().getData()[i]*10f)/10f,1,false)));
            }
            /* UUU */
            if(this.getUuu() != null) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_UUU], kstBaseTm, kstCal, StringUtils.toFormattedNumber(StringUtils.round(this.getUuu().getData()[i]*10)/10f,1,false)));

            /* VVV */
            if(this.getVvv() != null) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_VVV], kstBaseTm, kstCal, StringUtils.toFormattedNumber(Math.round(this.getVvv().getData()[i]*10)/10f,1,false)));

            /* VEC */
            if(this.getVec() != null) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_VEC], kstBaseTm, kstCal, Integer.toString(StringUtils.round(this.getVec().getData()[i]))));

            /* WSD */
            if(this.getWsd() != null) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_WSD], kstBaseTm, kstCal, StringUtils.toFormattedNumber(StringUtils.round(this.getWsd().getData()[i]*10)/10f,1,false)));

            /* SKY */
            if(this.getSky() != null) {
                weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_SKY], kstBaseTm, kstCal, Integer.toString((int)(this.getSky().getData()[i]+0.5f))));
            }
            /* PTY */
            if(this.getPty() != null) {
                weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_PTY], kstBaseTm, kstCal, Integer.toString((int)(this.getPty().getData()[i]+0.5f))));
            }
            /* POP */
            if(this.getPop() != null) {
                weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_POP], kstBaseTm, kstCal, Integer.toString((int)(this.getPop().getData()[i]+0.5f))));
            }
            /* REH */
            String rehValue = "-";
            if(this.getReh() != null) {
                rehValue = Integer.toString(StringUtils.round(this.getReh().getData()[i]));
            }
            /* WAV */
            /*if(this.getWav() != null && DFSShrnCacher.getInstance().isWaveAvailable(x, y)) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_WAV], kstBaseTm, kstCal, StringUtils.toFormattedNumber(Math.round(this.getWav().getData()[i]*10)/10f,1,false)));*/
            if(this.getWav() != null) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_WAV], kstBaseTm, kstCal, StringUtils.toFormattedNumber(Math.round(this.getWav().getData()[i]*10)/10f,1,false)));

            /* PCP & REH & SNO */
            if(this.getPcp() != null) this.addPCP_REH_SNO(kstBaseTm, kstCal, Float.parseFloat(StringUtils.toFormattedNumber(this.getPcp().getData()[i],1,false)), rehValue, Float.parseFloat(StringUtils.toFormattedNumber(this.getSno().getData()[i],1,false)));

            /* 최고 기온 */
            /*
             * 발표 시간 검사
             * 2 시 ~ 23시( 0 ~ 7)
             */

            switch(this.getAnnTime().getTmSeq()) {
                case 0: case 1: case 2: case 3:  // KST 02-11시 요청의 경우 : 오늘, 내일, 모레의 데이터 가져옴
                    if(this.getTmx() != null && wHour == 15) {
                        if(dayNum == 0) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[0])));
                        else if(dayNum == 1) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[1])));
                        else if(dayNum == 2) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[2])));
                    }
                    break;

                case 4: // KST 14시 요청의 경우 : 내일, 모레의 데이터 가져옴
                    if(this.getTmx() != null && wHour == 15) {
                        if(dayNum == 1) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[1])));
                        else if(dayNum == 2) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[2])));
                    }
                    break;

                case 5: case 6: case 7: // KST 17-23시 요청의 경우 : 내일, 모레, 글피의 데이터 가져옴
                    if(this.getTmx() != null && wHour == 15) {
                        if(dayNum == 1) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[1])));
                        else if(dayNum == 2) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[2])));
                        else if(dayNum == 3) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Float.toString(this.getTmx().getData()[3])));
                    }
                    break;
                default: weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMX], kstBaseTm, kstCal, Integer.toString(-999)));
            }

            /* 최저 기온 */
            // 발표 시간 검사
            switch(this.getAnnTime().getTmSeq()) {
                case 0: // KST 02시 요청의 경우 : 오늘, 내일, 모레의 데이터 가져옴
                    if(this.getTmn() != null && wHour == 6) {
                        if(dayNum == 0) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[0])));
                        else if(dayNum == 1) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[1])));
                        else if(dayNum == 2) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[2])));
                    }
                    break;
                case 5: case 6: case 7:  // KST 14-23시 요청의 경우 : 내일, 모레, 글피의 데이터 가져옴
                    if(this.getTmn() != null && wHour == 6) {
                        if(dayNum == 1) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[1])));
                        else if(dayNum == 2) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[2])));
                        else if(dayNum == 3) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[3])));
                    }
                    break;
                case 1: case 2: case 3: case 4: // KST 05-14시 요청의 경우 : 내일, 모레의 데이터 가져옴
                    if(this.getTmn() != null && wHour == 6) {
                        if(dayNum == 1) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[1])));
                        else if(dayNum == 2) weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Float.toString(this.getTmn().getData()[2])));
                    }
                    break;
                default: weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_TMN], kstBaseTm, kstCal, Integer.toString(-999)));
            }
        }
    }

    private void addPCP_REH_SNO(String kstBaseTm, final Calendar kstCal, float pcpValue, String rehValue, float snoValue) {
        DecimalFormat dfSno = new DecimalFormat("##.#");
        dfSno.setRoundingMode(RoundingMode.DOWN);

        pcpValue= Math.round(pcpValue);
        String pcpToString = "";
        String snoToString = "";

        //System.out.println("PCP=============="+pcpValue);
        //System.out.println("SNO================="+snoValue);

        if (pcpValue == 0.0) pcpToString = "강수없음";
        else if(pcpValue > 0 && pcpValue < 1) pcpToString = "1.0mm 미만";
        else if (pcpValue >= 1 && pcpValue < 30) pcpToString = pcpValue + "mm";
        else if (pcpValue >= 30 && pcpValue <= 50) pcpToString = "30.0~50.0mm";
        else if (pcpValue > 50)pcpToString = "50.0mm 이상";
        else pcpToString = "강수없음";

        if (snoValue == 0.0) snoToString = "적설없음";
        else if(snoValue > 0 && snoValue < 1) snoToString = "1cm미만";
        else if (snoValue >= 1 && snoValue < 5) snoToString = dfSno.format(snoValue) + "cm";
        else snoToString = "5cm이상";

        weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_PCP], kstBaseTm, kstCal, pcpToString));
        weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_REH], kstBaseTm, kstCal, rehValue));
        weather.add(makeValue(Config_Shrn.VCODE[Config_Shrn.V_SNO], kstBaseTm, kstCal, snoToString));
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
        return weather == null ? 0 : weather.size();
    }



}

