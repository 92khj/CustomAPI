package com.khj.customize.openapi.forecast;

import com.khj.customize.openapi.util.StringUtils;
import com.khj.customize.openapi.util.Util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 동네실황예보 캐쉬
 * @author LGCNS
 *
 */
public class DFSVsrtCacher {

    private static DFSVsrtCacher instance;
    public static DFSVsrtCacher getInstance() {

        if(instance == null) {
            //logger.fatal("DFSVsrtCacher does not initialzed.");
        }
        return instance;
    }
    public DFSVsrtCacher(String dfsBaseDirectory) {
        this(dfsBaseDirectory, false);
    }
    public DFSVsrtCacher(String dfsBaseDirectory, boolean isTest) {
        super();
//		Config_Vsrt.isTest = isTest;
        this.dfsBaseDirectory = dfsBaseDirectory;

        cacheMap = new FastHashMap();

    }

    public synchronized static void init(String baseDirectory, boolean isTest) {
        if(instance == null) {
            /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
            //instance = new DFSVsrtCacher(baseDirectory + File.separator + "VSRT" + File.separator + "GEMD", isTest);
            instance = new DFSVsrtCacher(baseDirectory, isTest);

            /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        } else {
            //logger.warn("DFSShrnCacher aleady initialized");
        }
    }

    private String dfsBaseDirectory;

    /**
     * 동네예보 지점 시계열 데이터 캐쉬
     * Key : x,y
     * Value : DFSPointVsrtData
     */
    FastHashMap cacheMap = null;

    public FastHashMap getCache(String cacheDateTime) {
        FastHashMap cache =  (FastHashMap)cacheMap.get(cacheDateTime);
        if(cache == null) {
            synchronized(cacheMap) {
                cache = new FastHashMap(Config_Vsrt.GRID_WIDTH * Config_Vsrt.GRID_HEIGHT+20);
                cacheMap.put(cacheDateTime, cache);
                clearOldCache();
            }
        }
        return cache;
    }
    /**
     * 24시간 지난 날짜의 캐시를 지움
     */
    public void clearOldCache(){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR_OF_DAY, -6);
        String lastCache = StringUtils.getFormattedDate(cal.getTime(), "yyyyMMddHHmm");
        List<String> oldKeys = new ArrayList<String>();
        for(Object okey : cacheMap.keySet()) {
            String key = (String)okey;
            if(key.compareTo(lastCache) < 0) {
                oldKeys.add(key);
            }
        }
        for(String key : oldKeys) {
            cacheMap.remove(key);
        }
        oldKeys = null;
    }
    /**
     * 최근 발표 시각
     * 한시간 단위로 변경 변경됨 20100514
     */
    public DFSVsrtTimeSequence getNowTimeSequence() {
        Calendar tm = Calendar.getInstance();
        if(Config_Vsrt.isTest) {
            tm.set(Calendar.YEAR, 2016);
            tm.set(Calendar.MONDAY, Calendar.NOVEMBER);
            tm.set(Calendar.DAY_OF_MONTH, 14);
            tm.set(Calendar.HOUR_OF_DAY, 14);
            tm.set(Calendar.MINUTE, 40);
        }
        Calendar orgTm = (Calendar)tm.clone();

        //!!!System.out.println(this.getClass().getName()+".DFSVsrtTimeSequence():tm.get(Calendar.HOUR_OF_DAY) 1 :" + tm.get(Calendar.HOUR_OF_DAY) + " ############# \n ");
        /* UTC 로 바꿈  -9 시간 */
        tm.add(Calendar.HOUR_OF_DAY, -9);


        //!!!System.out.println(this.getClass().getName()+".DFSVsrtTimeSequence():tm.get(Calendar.HOUR_OF_DAY) 2 :" + tm.get(Calendar.HOUR_OF_DAY) + " ############# \n ");
//		int adjust = tm.get(Calendar.HOUR_OF_DAY);

        tm.set(Calendar.MINUTE, 0);
        tm.set(Calendar.SECOND, 0);
        tm.set(Calendar.MILLISECOND, 0);

        //!!!System.out.println(this.getClass().getName()+".DFSVsrtTimeSequence():tm.get(Calendar.HOUR_OF_DAY) 3 :" + tm.get(Calendar.HOUR_OF_DAY) + " ############# \n ");

        return new DFSVsrtTimeSequence(tm,orgTm);
    }

    /**
     * 최근 발표 시각
     * 한시간 단위로 변경 변경됨 20100514
     */
    public Calendar getNowTimeSequence2() {
        Calendar tm = Calendar.getInstance();
        if(Config_Vsrt.isTest) {
            tm.set(Calendar.YEAR, 2014);
            tm.set(Calendar.MONTH, Calendar.OCTOBER);
            tm.set(Calendar.DAY_OF_MONTH, 2);
            tm.set(Calendar.HOUR_OF_DAY, 14);
            tm.set(Calendar.MINUTE, 35);
        }
        /* UTC 로 바꿈  -9 시간 */
        tm.add(Calendar.HOUR_OF_DAY, -9);

        return tm;
    }

    public DFSPointVsrtData getPastPoint(int x, int y, DFSVsrtTimeSequence pastTm) {
        DFSPointVsrtData data = new DFSPointVsrtData();

        if (x <=0 || y<=0 || pastTm == null) return null;

        data.setX(x);
        data.setY(y);
        data.setAnnTime(pastTm);


        // 하늘 상태
        data.setSky(readRaw(x,y,pastTm, Config_Vsrt.V_SKY));
        // 강수형태
        data.setPty(readRaw(x,y,pastTm, Config_Vsrt.V_PTY));
        // 1시간강수량
        data.setRn1(readRaw(x,y,pastTm, Config_Vsrt.V_RN1));
        // 낙뢰
        //data.setLgt(readRaw(x, y, pastTm, Config_Vsrt.V_LGT));

        data.setT1h(readRaw(x, y, pastTm, Config_Vsrt.V_T1H));

        data.setReh(readRaw(x, y, pastTm, Config_Vsrt.V_REH));

        data.setUuu(readRaw(x, y, pastTm, Config_Vsrt.V_UUU));

        data.setVvv(readRaw(x, y, pastTm, Config_Vsrt.V_VVV));

        data.setVec(readRaw(x, y, pastTm, Config_Vsrt.V_VEC));

        data.setWsd(readRaw(x, y, pastTm, Config_Vsrt.V_WSD));


        data.updateTimeStamp();

        data.initDFSVsrtWeather( );

        setPoint(data);

        return data;
    }
    /**
     * 과거 동네예보 조회
     * @param x 좌표 x
     * @param y 좌표 y
     * @param kstTmFc (KST) yyyyMMddHH
     * @return
     */
    public DFSPointVsrtData getPastPoint(int x, int y, String kstTmFc){

        Calendar tm = Util.getTmFc2Cal(kstTmFc,"yyyyMMddHH");
        /* UTC 로 바꿈  -9 시간 */
        tm.add(Calendar.HOUR_OF_DAY, -9);

        tm.set(Calendar.MINUTE, 0);
        tm.set(Calendar.SECOND, 0);
        tm.set(Calendar.MILLISECOND, 0);
        DFSVsrtTimeSequence pastTm = new DFSVsrtTimeSequence(tm);

        DFSPointVsrtData point;
        point = (DFSPointVsrtData)getCache(pastTm.getKstTm()).get(Integer.toString(x) + "," + Integer.toString(y));
        //ogger.debug("getPastPoint(int x, int y,String kstTmFc)-pastTm.getKstTm() {}, point = {}" , pastTm.getKstTm(), point);
        if(point!= null) {
          //  logger.debug("point.getAnnTime().getUtcTm(): {}, pastTm.getUtcTm() : {}" , point.getAnnTime().getUtcTm(), pastTm.getUtcTm());
           // logger.debug("point.getTimeStamp() = {}, getDFSTimeStamp(x,y,Config_Vsrt.V_PTY,pastTm.getUtcTm() = {} ",point.getTimeStamp() ,getDFSTimeStamp(x,y, Config_Vsrt.V_PTY,pastTm.getUtcTm()));
        }

        if( point == null ||
                !point.getAnnTime().getUtcTm().equals(pastTm.getUtcTm()) ||
                point.getTimeStamp() != getDFSTimeStamp(x,y, Config_Vsrt.V_PTY,pastTm.getUtcTm())
        ) {
            // 타임스탬프는 한시간별 기온을 기준으로 함

            // 캐쉬에 없거나 최근 데이터가 아니면 캐쉬를 리로드(발표시간이 다르거나  timestamp 가 다르면)
            DFSPointVsrtData newPoint = getPastPoint(x,y,pastTm);
            
            if(newPoint != null) {
                point = newPoint;
            }
        } else {
            //logger.debug("DFSPointVsrtData cache pulled {}", point.getDFSVsrtWeather());
        }
        return point;

    }

    /*
     * 초단기 파일명 시분초 생성
     * 매정시 30분이 지나지 않으면 한시간전 파일시간을 생성
     */
    public Calendar getVsrtTimeSequence(Calendar tm) {

        Calendar vsrtTm = (Calendar)tm.clone();
        int iMinute = 0;

        //!!!System.out.println(this.getClass().getName()+".getVsrtTimeSequence() : START : ###  ");
        //!!!System.out.println("  tm : " + vsrtTm.toString() + "  ");
        //!!!System.out.println("  Calendar.HOUR_OF_DAY : " + vsrtTm.get(Calendar.HOUR_OF_DAY) + "  ");
        //!!!System.out.println("  Calendar.MINUTE : " + vsrtTm.get(Calendar.MINUTE) + "  ");
        //!!!System.out.println("  Calendar.SECOND : " + vsrtTm.get(Calendar.SECOND) + "  ");
        //!!!System.out.println("  Calendar.MILLISECOND : " + vsrtTm.get(Calendar.MILLISECOND) + "  ");
        //!!!System.out.println(" @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ ");
        /* UTC 로 바꿈  -9 시간 */
        //tm.add(Calendar.HOUR_OF_DAY, -9);

        iMinute = vsrtTm.get(Calendar.MINUTE);

        //!!!System.out.println("  iMinute : " + iMinute + "  ");


        // 현재 시간의 파일을 생성 ex) 20100526100000
        if( iMinute > 29 ) {
            vsrtTm.set(Calendar.MINUTE, 0);
            vsrtTm.set(Calendar.SECOND, 0);
            vsrtTm.set(Calendar.MILLISECOND, 0);
        } else { // 한시간 이전 파일을 생성
            vsrtTm.add(Calendar.HOUR_OF_DAY, -1);
            vsrtTm.set(Calendar.MINUTE, 0);
            vsrtTm.set(Calendar.SECOND, 0);
            vsrtTm.set(Calendar.MILLISECOND, 0);
        }

        ////!!!System.out.println("  vsrtTm : " + tm.toString() + "  ");
        //!!!System.out.println("  Calendar.HOUR_OF_DAY : " + vsrtTm.get(Calendar.HOUR_OF_DAY) + "  ");
        //!!!System.out.println("  Calendar.MINUTE : " + vsrtTm.get(Calendar.MINUTE) + "  ");
        //!!!System.out.println("  Calendar.SECOND : " + vsrtTm.get(Calendar.SECOND) + "  ");
        //!!!System.out.println("  Calendar.MILLISECOND : " + vsrtTm.get(Calendar.MILLISECOND) + "  ");
        //!!!System.out.println("  TIME : " + vsrtTm.get(Calendar.HOUR_OF_DAY) + "" + vsrtTm.get(Calendar.MINUTE) + "" + vsrtTm.get(Calendar.SECOND) + " " );
        //!!!System.out.println(this.getClass().getName()+".getTestDate() : END : ###  ");

        return vsrtTm;

    } // end getVsrtTimeSequence()

    public DFSPointVsrtData getPoint(int x, int y){

        Calendar cal = getNowTimeSequence2();
        Calendar vsrtCa = getVsrtTimeSequence(cal);  // 실황
        DFSVsrtTimeSequence tmVsrt = new DFSVsrtTimeSequence(vsrtCa,cal);

        return getPoint(x,y,tmVsrt);

    }

    public DFSPointVsrtData getPoint(int x, int y, DFSVsrtTimeSequence tm){

        //!!!System.out.println(this.getClass().getName()+".getPoint() Start ############# \n ");

        DFSPointVsrtData point;
        point = (DFSPointVsrtData)getCache(tm.getKstTm()).get(Integer.toString(x) + "," + Integer.toString(y));

        //!!!System.out.println(this.getClass().getName()+".getPoint() cacheMap.size() : " + cacheMap.size() + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() cacheMap.toString() : " + cacheMap.toString() + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() point is null : " + (point == null ) + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() tm.getUtcTm() : " + tm.getUtcTm() + " ############# \n ");

        //if( point != null ) {
        //!!!System.out.println(this.getClass().getName()+".getPoint() point.getAnnTime().getUtcTm() : " + point.getAnnTime().getUtcTm() + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() point.getTimeStamp() : " + point.getTimeStamp() + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() getDFSTimeStamp(x,y,Config_Vsrt.V_PTY,tm.getUtcTm()) : " +getDFSTimeStamp(x,y,Config_Vsrt.V_PTY,tm.getUtcTm()) + " ############# \n ");
        //}

        if( point == null ||
                !point.getAnnTime().getUtcTm().equals(tm.getUtcTm()) ||
                point.getTimeStamp() != getDFSTimeStamp(x,y, Config_Vsrt.V_PTY,tm.getUtcTm())
        )
        { // 타임스탬프는 한시간별 기온을 기준으로 함

            // 캐쉬에 없거나 최근 데이터가 아니면 캐쉬를 리로드(발표시간이 다르거나  timestamp 가 다르면)
            DFSPointVsrtData newPoint = fillCache(x,y,tm);

            //!!!System.out.println(this.getClass().getName()+".getPoint() newPoint is null : " + (newPoint == null ) + " ############# \n ");
            if(newPoint == null) {
                // 최신 파일이 등록되지 않았다. 시스템 오류이거나 DFS 로 부터 최신데이터가 넘어오지 않았을경우..
                // 시스템은 예보시간 10 분 이상 이전에 데이터가 업로드 되도록 되어있다.
                if(point == null) {
                    // 캐쉬에도 없을경우 Data 에 최신 데이터가 아니라는 표시(isLatest = false)를 하고 가장 최근 예보 데이터를 읽음
                    point = loadLastDFS(x,y,tm);
                }else {
                    ; // 캐쉬 데이터 리턴
                }
            }
            else {
                point = newPoint;
            }
        }
        return point;
    }
    /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
    /**
     * ex) /DATA/201611/01
     * @param baseDirectory
     * @param tm "yyyyMMdd~" : utc
     * @return
     */
/*	private String getDirectoryByDate(String baseDirectory, String utcTm) {
		try {
			return baseDirectory + File.separator + utcTm.substring(0,6)
				+ File.separator + utcTm.substring(6,8);
		}catch(Exception e) {
			logger.fatal("utcTm does not valid.",e);
			return baseDirectory;
		}
	}
*/
    /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
    /**
     * 이진 파일의 마지막 수정일
     * @param x
     * @param y
     * @param type
     * @param tm
     * @return
     */
    public long getDFSTimeStamp(int x, int y, int type, String utcTm){
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        //File dfsFile = new File(getDirectoryByDate(dfsBaseDirectory, utcTm) + File.separator + Config_Vsrt.DFS_PREFIX + Config_Vsrt.VCODE[type] + "." + utcTm.substring(0,10) + "30");
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/

        if (type < 0) return 0;

        StringBuffer strDfsFile = new StringBuffer(dfsBaseDirectory).append(File.separator).append(Config_Vsrt.DFS_PREFIX)
                .append(Config_Vsrt.VCODE[type]).append(".").append(utcTm.substring(0,10)).append("30");
       // logger.debug("1: 파일명 :["+strDfsFile+"]");
        File dfsFile = new File(strDfsFile.toString());
        try {
            return dfsFile.lastModified();
        }catch(NullPointerException e) {
            //Logger.getLogger(getClass()).error(e,e);
          //  logger.error("DFS - VSRT lastModified Check Error");
            return 0;
        }
    }
    /**
     * 캐쉬 데이터 업뎃.
     * @param data
     */
    private void setPoint(final DFSPointVsrtData data) {
        String kstTm = data.getAnnTime().getKstTm();
       // logger.debug("setPoint(final DFSPointVsrtData data) data.getAnnTime().getKstTm() {}" , kstTm);
        FastHashMap cache = getCache(kstTm);
        cache.setFast(false);
        cache.put(Integer.toString(data.getX()) + "," + Integer.toString(data.getY()), data);
        cache.setFast(true);
    }
    /**
     * 실시간 GEMD 에서 데이터 추출
     * 0 : "T1H" 1시간별 기온
     * 1 : "REH" 상대 습도
     * 2 : "SKY" 하늘상태
     * 3 : "PTY" 강수형태
     * 4 : "UUU" 동서방향풍속
     * 5 : "VVV" 남북방향풍속
     * 6 : "RN1" 1시간 강수량
     * @param x
     * @param y
     * @param tm yyyyMMddHHmm : UTC
     * @return
     */
    public static String errorFile = "";

    private DFSPointVsrtData fillCache(int x, int y, DFSVsrtTimeSequence tm) {

        if (x <=0 || y<=0 || tm == null) return null;

        DFSPointVsrtData data = new DFSPointVsrtData();
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
		/*String strDfsFile = "";
		for (int i =0; i < Config_Vsrt.VCODE.length; i++) {
			strDfsFile = getDirectoryByDate(dfsBaseDirectory,tm.getUtcTm()) + File.separator
									+ Config_Vsrt.DFS_PREFIX
									+ Config_Vsrt.VCODE[i]
									+ "." + tm.getUtcTm().substring(0,10) + "30";  // 시간패턴 30분이므로 수정
			if( !(new File(strDfsFile)).exists() ) {
				if ( !errorFile.equals(strDfsFile) ){
					errorFile = strDfsFile;
				}
				return null;
			}
		}*/
        /* 20140623 다량의 동네예보 에러로그 발생으로 인해 파일확인 전처리 시행*/
        for (int i = 0; i < Config_Vsrt.VCODE.length; i++) {
            String strDfsFile = new StringBuffer(dfsBaseDirectory).append(File.separator).append(Config_Vsrt.DFS_PREFIX)
                    .append(Config_Vsrt.VCODE[i]).append(".").append(tm.getUtcTm().substring(0,10)).append("30").toString();
          //  logger.debug("2: 파일명 :["+strDfsFile+"]");
            if( !(new File(strDfsFile)).exists() ) {
                if ( !errorFile.equals(strDfsFile) ){
                    errorFile = strDfsFile;
                }
                return null;
            }
        }
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        data.setX(x);
        data.setY(y);
        data.setAnnTime(tm);

        // 강수형태
        data.setPty(readRaw(x,y,tm, Config_Vsrt.V_PTY));
        // 1시간강수량
        data.setRn1(readRaw(x,y,tm, Config_Vsrt.V_RN1));
        // 하늘 상태
        data.setSky(readRaw(x,y,tm, Config_Vsrt.V_SKY));
        // LGT
        data.setLgt(readRaw(x,y,tm, Config_Vsrt.V_LGT));

        data.setT1h(readRaw(x, y, tm, Config_Vsrt.V_T1H));

        data.setReh(readRaw(x, y, tm, Config_Vsrt.V_REH));

        data.setUuu(readRaw(x, y, tm, Config_Vsrt.V_UUU));

        data.setVvv(readRaw(x, y, tm, Config_Vsrt.V_VVV));

        data.setVec(readRaw(x, y, tm, Config_Vsrt.V_VEC));

        data.setWsd(readRaw(x, y, tm, Config_Vsrt.V_WSD));

        data.updateTimeStamp();
        data.initDFSVsrtWeather( );
        // Cache updat;
        setPoint(data);

        //System.out.println("Fill Cache ==> 지점 : " + Integer.toString(x) + "," + Integer.toString(y));

        return data;
    }
    /**
     * 가장 최근 GEMD 에서 데이터 추출
     *
     * @param x
     * @param y
     * @param nowTm
     * @return
     */
    private DFSPointVsrtData loadLastDFS(int x, int y, DFSVsrtTimeSequence nowTm) {

        if (x <=0 || y<=0 || nowTm == null) return null;

        DFSPointVsrtData data = new DFSPointVsrtData();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar nowCal = nowTm.getUtcCal();
        /*
         * 1시간씩 감소시키면서 GEMD 데이터 파일이 존재하는지 검사 최대 24시간까지(24번)
         */
        String lastTm="";
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
/*		File dfsRoot = new File(getDirectoryByDate(dfsBaseDirectory, nowTm.getUtcTm()));
		for(int i = 0; i < 24 ; i++) {  // mhh
			nowCal.add(Calendar.HOUR_OF_DAY, -1); // mhh
			lastTm = df.format(nowCal.getTime());
			File dfsData = new File(dfsRoot,Config_Vsrt.DFS_PREFIX + "PTY." + lastTm.substring(0,10) + "30");	// 시간패턴 30분이므로 수정
			System.out.println(dfsData.getAbsolutePath());
			if(dfsData.exists()){
				break;
			}
		}
*/
        for(int i = 0; i < 24 ; i++) {  // mhh
            nowCal.add(Calendar.HOUR_OF_DAY, -1); // mhh
            lastTm = df.format(nowCal.getTime());
            File dfsData = new File(dfsBaseDirectory, new StringBuffer(Config_Vsrt.DFS_PREFIX).append("PTY.")
                    .append(lastTm.substring(0,10)).append("30").toString());
          //  logger.debug("3: 파일명 :["+dfsData+"]");
            if(dfsData.exists()){
                break;
            }
        }
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        /* 시간 순서 재배열 */
        nowTm.setUtcCal(nowCal);

        data.setX(x); data.setY(y); data.setAnnTime(nowTm);

        // 하늘 상태
        data.setSky(readRaw(x,y,nowTm, Config_Vsrt.V_SKY));
        // 강수상태
        data.setPty(readRaw(x,y,nowTm, Config_Vsrt.V_PTY));
        // 1시간강수량
        data.setRn1(readRaw(x,y,nowTm, Config_Vsrt.V_RN1));
        // LGT
        data.setLgt(readRaw(x,y,nowTm, Config_Vsrt.V_LGT));

        data.setT1h(readRaw(x, y, nowTm, Config_Vsrt.V_T1H));

        data.setReh(readRaw(x, y, nowTm, Config_Vsrt.V_REH));

        data.setUuu(readRaw(x, y, nowTm, Config_Vsrt.V_UUU));

        data.setVvv(readRaw(x, y, nowTm, Config_Vsrt.V_VVV));

        data.setVec(readRaw(x, y, nowTm, Config_Vsrt.V_VEC));

        data.setWsd(readRaw(x, y, nowTm, Config_Vsrt.V_WSD));

        data.updateTimeStamp();
        data.initDFSVsrtWeather( );
        setPoint(data);

        return data;
    }
    /** mhh
     * 원시 데이터 로드
     * @param x 1 base numbering
     * @param y 1 base numbering
     * @param tm
     * @param type
     * @return
     * @throws IOException
     */

    private DFSVsrtRaw readRaw(int x, int y, DFSVsrtTimeSequence tm, int type) {
        DFSVsrtRaw raw = null;

        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        //String strDfsFile = getDirectoryByDate(dfsBaseDirectory, tm.getUtcTm()) + File.separator + Config_Vsrt.DFS_PREFIX + Config_Vsrt.VCODE[type] + "." + tm.getUtcTm().substring(0,10) + "30";

        if (type < 0 || tm.getTmSeq() < 0) return raw;

        //파일경로
        StringBuffer strDfsFile = new StringBuffer(dfsBaseDirectory)
                                                    .append(tm.getUtcTm().substring(0,6)).append(File.separator)
                                                    .append(tm.getUtcTm().substring(6,8)).append(File.separator)
                                                    .append(Config_Vsrt.DFS_PREFIX)
                                                    .append(Config_Vsrt.VCODE[type]).append(".").append(tm.getUtcTm().substring(0,10)).append("30");
        //logger.debug("4: 파일명 :["+strDfsFile+"]");

        File dfsFile = new File(strDfsFile.toString());
        //System.out.println("getUtcTm" +  tm.getUtcTm());
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/

        /* Convert to zerobase */
        x--; y--;
        RandomAccessFile ra=null;

        try{
            ra = new RandomAccessFile(dfsFile,"r");

            int fctcnt = Config_Vsrt.FCT_CNT_EQ[type][tm.getTmSeq()];
            int unitSize = Config_Vsrt.GRID_WIDTH * Config_Vsrt.GRID_HEIGHT * 4;  // mhh 150788

            //!!!System.out.println(this.getClass().getName()+".readRaw() fctcnt : " + fctcnt + "  \n ");
            //!!!System.out.println(this.getClass().getName()+".readRaw() unitSize : " + unitSize + "  \n ");

            if (fctcnt < 0) return raw;

            byte[] body = new byte[fctcnt * 4];
            byte[] buffer = new byte[4];
            /*
             * i+1 시간 후 예보 데이터 예보 시간 수 만큼 읽기
             * 데이터 형식
             * |+1 시간 후의 (모든 지점 데이터) 데이터|
             * |+4 시간 후의 (모든 지점 데이터) 데이터|
             * ...
             * ...
             * |+55 시간 후의 (모든 지점 데이터) 데이터|
             *
             */
            for(int i = 0 ; i < fctcnt; i++) {
                //!!!System.out.println(this.getClass().getName()+".readRaw() 계산 : " + (256 + i * unitSize + ((long)y * Config_Vsrt.GRID_WIDTH*4) + ((long)x * 4)) + "  \n ");

                ra.seek( 256 + i * unitSize + ((long)y * Config_Vsrt.GRID_WIDTH*4) + ((long)x * 4) );
                // 2시일경우 , i ==0
                // fctcnt : 16  , x : 57 , y:128
                // 256 + 0 * 150788 + (128 *149 *4) + (57 *4 )
                // 256 + 0 + 76288 + 228 = 76772

                // 2시일경우 , i ==1
                // fctcnt : 16  , x : 57 , y:128
                // 256 + 1 * 150788 + (128 * 149 *4) + ( 57 * 4 )
                // 256 + 150788 + 76288 + 228 = 227560

                // 2시일경우 , i == 2
                // fctcnt : 16  , x : 57 , y:128
                // 256 + 2 * 150788 + (128 * 149 *4) + ( 57 * 4 )
                // 256 + 301576 + 76288 + 228 = 378348

                ra.read(buffer);
                System.arraycopy(buffer, 0, body, i * 4, 4);
            }
            raw = new DFSVsrtRaw(type,body,dfsFile.lastModified());


            //System.out.println(this.getClass().getName()+".readRaw() raw.getX() : " + raw.getX() + "  \n ");
            //System.out.println(this.getClass().getName()+".readRaw() raw.getY() : " + raw.getY() + "  \n ");
            //System.out.println(this.getClass().getName()+".readRaw() raw.getType() : " + raw.getType() + "  \n ");
            //System.out.println(this.getClass().getName()+".readRaw() raw.toString() : " + raw.toString() + "  \n ");
            //System.out.println(this.getClass().getName()+".readRaw() raw.getData() : " + raw.getData() + "  \n ");
            //System.out.println(raw.getData().length);
            //for (int j = 0; j < raw.getData().length; j++) {
            //    System.out.println(this.getClass().getName() + ".readRaw() raw.getData() : " + raw.getData()[j] + "  \n ");
            //}

        }catch(IOException e) {
            //logger.debug(e.getMessage(), e);
        }finally{
            if(ra!=null) { try { ra.close(); } catch(IOException ee) {
                //logger.debug(ee.getMessage(), ee);
            } };
        }


        return raw;
    }
    public long getVersion(int x, int y, String kstTmFc) throws Exception{
        return this.getPastPoint(x, y, kstTmFc.substring(0,10)).getTimeStamp();
    }

}
