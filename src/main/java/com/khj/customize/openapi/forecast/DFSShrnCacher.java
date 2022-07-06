package com.khj.customize.openapi.forecast;

import com.khj.customize.openapi.util.StringUtils;
import com.khj.customize.openapi.util.Util;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * 동네예보 캐쉬
 * @author Seagull
 *
 */
public class DFSShrnCacher {


    private final List<Byte> seaLandMask;
    private static DFSShrnCacher instance;

    public static DFSShrnCacher getInstance() {
        if(instance == null) {
            //logger.fatal("DFSShrnCacher does not initialzed.");
        }

        return instance;
    }

    public DFSShrnCacher(String dfsBaseDirectory) {
        this(dfsBaseDirectory, false);
    }
    public DFSShrnCacher(String dfsBaseDirectory, boolean isTest) {
        super();
//		Config_Shrn.isTest = isTest;
        this.dfsBaseDirectory = dfsBaseDirectory;
        cacheMap = new FastHashMap();
        seaLandMask = readSeaLandMask();
    }

    public boolean isWaveAvailable(int x, int y) {
        byte mask = findSeaLandMask(x,y);
        if(mask == 12
                || mask == 13
                || mask == 22
                || mask == 23
                || mask == 32
                || mask == 33
                || mask == 42
                || mask == 43
                || mask == 52
                || mask == 53
                || mask == 62
                || mask == 63
                || mask == 72
                || mask == 73
        ) {
            return true;
        } else {
            return false;
        }
    }
    public byte findSeaLandMask(int x, int y) {
        int index = x-1 + ((y-1) * Config_Shrn.GRID_WIDTH);
        if(index < this.seaLandMask.size()) {
            return seaLandMask.get(index);
        } else {
            return -1;
        }
    }
    /**
     * 파고 MASK 읽음.
     * @return
     */
    private List<Byte> readSeaLandMask() {
        List<Byte> mask = new ArrayList<Byte>();
        BufferedInputStream bis = null;
        /*
        try {
            bis = new BufferedInputStream(getClass().getResourceAsStream("sealandmask.dat"));
            byte[] buffer = new byte[1024];
            int read = 0;
            // 147 바이트 헤더 건너뜀.
            for(int i = 0 ; i < 147 ; i++) {
                //bis.read();
            }
            while((read = bis.read(buffer)) > 0) {
                for(int i = 0 ; i < read; i++) {
                    mask.add(buffer[i]);
                }
            }

        }catch(IOException e) {
            logger.error(e.getMessage(), e);
        }finally{
            try {if(bis != null)bis.close();} catch (IOException e) {logger.debug(e.getMessage());}
        }
        logger.info("SeaLandMask {}", mask);

         */
        return mask;
    }
    public synchronized static void init(String baseDirectory, boolean isTest) {
        if(instance == null) {
            /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
            //instance = new DFSShrnCacher(baseDirectory + File.separator + "SHRT" + File.separator + "GEMD", isTest);
            instance = new DFSShrnCacher(baseDirectory, isTest);
            /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        } else {
            //logger.warn("DFSShrnCacher aleady initialized");
        }
    }

    private String dfsBaseDirectory;

    /**
     * 동네예보 지점 시계열 데이터 캐쉬
     * Key : x,y
     * Value : DFSPointShrnData
     */
    FastHashMap cacheMap = null;

    public FastHashMap getCache(String cacheDateTime) {
        FastHashMap cache =  (FastHashMap)cacheMap.get(cacheDateTime);
        if(cache == null) {
            synchronized(cacheMap) {
                cache = new FastHashMap(Config_Shrn.GRID_WIDTH * Config_Shrn.GRID_HEIGHT+20);
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
     * mhh : 3시간 단위에서 매시간단위로 변경됨 20100514
     */
    public DFSShrnTimeSequence getNowTimeSequence() {

        Calendar tm = Calendar.getInstance();
        if(Config_Shrn.isTest) {
            tm.set(Calendar.YEAR, 2016);
            tm.set(Calendar.MONTH, Calendar.NOVEMBER);
            tm.set(Calendar.DAY_OF_MONTH, 14);
            tm.set(Calendar.HOUR_OF_DAY, 14);
            tm.set(Calendar.MINUTE, 25);
        }
        //!!!System.out.println(this.getClass().getName()+".DFSOdamTimeSequence():tm.get(Calendar.HOUR_OF_DAY) 1 :" + tm.get(Calendar.HOUR_OF_DAY) + " ############# \n ");
        /* UTC 로 바꿈  -9 시간 */
        tm.add(Calendar.HOUR_OF_DAY, -9);

//		//!!!System.out.println(this.getClass().getName()+".DFSShrnTimeSequence():tm.get(Calendar.HOUR_OF_DAY) 2 :" + tm.get(Calendar.HOUR_OF_DAY) + " ############# \n ");
        int adjust = tm.get(Calendar.HOUR_OF_DAY);
        ////!!!System.out.println(this.getClass().getName()+".DFSShrnTimeSequence():adjust:" +adjust+ " ############# \n ");
        ////!!!System.out.println(this.getClass().getName()+".DFSShrnTimeSequence():(-1)*(adjust+1)%3 =:" + ( (-1)*(adjust+1)%3) + " ############# \n ");
        tm.add(Calendar.HOUR_OF_DAY, (-1)*(adjust+1)%3); // 22 -23%3 = -2 필요없음 매시간단위로 데이터 받음

        tm.set(Calendar.MINUTE, 0);
        tm.set(Calendar.SECOND, 0);
        tm.set(Calendar.MILLISECOND, 0);

        return new DFSShrnTimeSequence(tm);
    }


    public DFSPointShrnData getPastPoint(int x, int y, DFSShrnTimeSequence pastTm){

        DFSPointShrnData data = new DFSPointShrnData();


        data.setX(x);
        data.setY(y);
        data.setAnnTime(pastTm);
        // 3시간별 기온 읽기
        data.setTmp(readRaw(x,y,pastTm, Config_Shrn.V_TMP));
        // 최고기온
        data.setTmx(readRaw(x,y,pastTm, Config_Shrn.V_TMX));
        // 최저기온
        data.setTmn(readRaw(x,y,pastTm, Config_Shrn.V_TMN));
        // 습도
        data.setReh(readRaw(x,y,pastTm, Config_Shrn.V_REH));
        // 하늘 상태
        data.setSky(readRaw(x,y,pastTm, Config_Shrn.V_SKY));
        // 강수형태
        data.setPty(readRaw(x,y,pastTm, Config_Shrn.V_PTY));
        // 강수 확률
        data.setPop(readRaw(x,y,pastTm, Config_Shrn.V_POP));

        /**
         // 06시간누적강수량
         try { data.setPCP(readRaw(x,y,pastTm,Config_Shrn.V_PCP)); } catch(Exception innerEx) { }
         // 06시간누적적설량
         try { data.setSNO(readRaw(x,y,pastTm,Config_Shrn.V_SNO)); } catch(Exception innerEx) { }
         **/

        // 06시간누적강수량
        data.setPcp(readRaw(x,y,pastTm, Config_Shrn.V_PCP));
        // 06시간누적적설량
        data.setSno(readRaw(x,y,pastTm, Config_Shrn.V_SNO));

        // 동서방향풍속
        data.setUuu(readRaw(x,y,pastTm, Config_Shrn.V_UUU));
        // 남북방향풍속
        data.setVvv(readRaw(x,y,pastTm, Config_Shrn.V_VVV));

		/*
		// 파고
		try { data.setWav(readRaw(x,y,pastTm,Config_Shrn.V_WAV)); } catch(Exception innerEx) {logger.debug(innerEx.getMessage(), innerEx); }

		try { data.setVec(readRaw(x,y,pastTm,Config_Shrn.V_VEC)); } catch(Exception innerEx) {logger.debug(innerEx.getMessage(), innerEx); }
		try { data.setWsd(readRaw(x,y,pastTm,Config_Shrn.V_WSD)); } catch(Exception innerEx) {logger.debug(innerEx.getMessage(), innerEx); }
		*/

        // 파고
        data.setWav(readRaw(x,y,pastTm, Config_Shrn.V_WAV));
        data.setVec(readRaw(x,y,pastTm, Config_Shrn.V_VEC));
        data.setWsd(readRaw(x,y,pastTm, Config_Shrn.V_WSD));

        data.initDFSShrnWeather();
        data.updateTimeStamp();

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
    public DFSPointShrnData getPastPoint(int x, int y,String kstTmFc){


        Calendar tm = Util.getTmFc2Cal(kstTmFc,"yyyyMMddHH");
        /* UTC 로 바꿈  -9 시간 */
        tm.add(Calendar.HOUR_OF_DAY, -9);

        //tm.add(Calendar.HOUR_OF_DAY, 1);
        tm.set(Calendar.MINUTE, 0);
        tm.set(Calendar.SECOND, 0);
        tm.set(Calendar.MILLISECOND, 0);

        DFSShrnTimeSequence pastTm = new DFSShrnTimeSequence(tm);



        DFSPointShrnData point;
        point = (DFSPointShrnData)getCache(pastTm.getKstTm()).get(Integer.toString(x) + "," + Integer.toString(y));


        if( point == null ||
                !point.getAnnTime().getUtcTm().equals(pastTm.getUtcTm()) ||
                point.getTimeStamp() != getDFSTimeStamp(x,y, Config_Shrn.V_TMP,pastTm.getUtcTm())
        ) { // 타임스탬프는 3시간별 기온을 기준으로 함

            // 캐쉬에 없거나 최근 데이터가 아니면 캐쉬를 리로드(발표시간이 다르거나  timestamp 가 다르면)
            DFSPointShrnData newPoint = getPastPoint(x,y,pastTm);

            if(newPoint != null) {
                point = newPoint;
            }
        } else {
            //logger.debug("DFSPointShrnData cache pulled {}", point.getDFSShrnWeather());
        }
        return point;

    }

    public DFSPointShrnData getPoint(int x, int y){
        DFSShrnTimeSequence tm = getNowTimeSequence();
        return getPoint(x,y,tm);
    }

    public DFSPointShrnData getPoint(int x, int y, DFSShrnTimeSequence tm){

        DFSPointShrnData point;
        point = (DFSPointShrnData)getCache(tm.getKstTm()).get(Integer.toString(x) + "," + Integer.toString(y));

        //DFSShrnTimeSequence tm = getNowTimeSequence();

        //!!!System.out.println(this.getClass().getName()+".getPoint() cacheMap.size() : " + cacheMap.size() + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() cacheMap.toString() : " + cacheMap.toString() + " ############# \n ");

        //!!!System.out.println(this.getClass().getName()+".getPoint() point is null : " + (point == null ) + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() tm.getUtcTm() : " + tm.getUtcTm() + " ############# \n ");

        //if( point != null ) {
        //!!!System.out.println(this.getClass().getName()+".getPoint() point.getAnnTime().getUtcTm() : " + point.getAnnTime().getUtcTm() + " ############# \n ");

        //!!!System.out.println(this.getClass().getName()+".getPoint() point.getTimeStamp() : " + point.getTimeStamp() + " ############# \n ");
        //!!!System.out.println(this.getClass().getName()+".getPoint() getDFSTimeStamp(x,y,Config_Shrn.V_TMP,tm.getUtcTm()) : " +getDFSTimeStamp(x,y,Config_Shrn.V_TMP,tm.getUtcTm()) + " ############# \n ");

        //}

        //if( point == null ||
        //	!point.getAnnTime().getUtcTm().equals(tm.getUtcTm()) ||
        //  	point.getTimeStamp() != getDFSTimeStamp(x,y,Config_Shrn.V_TMP,tm.getUtcTm())
        //  )
        //{ // 타임스탬프는 3시간별 기온을 기준으로 함

        // 캐쉬에 없거나 최근 데이터가 아니면 캐쉬를 리로드(발표시간이 다르거나  timestamp 가 다르면)
        DFSPointShrnData newPoint = fillCache(x,y,tm);


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
        //}
        return point;
    }
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
	}*/
    /**
     * 이진 파일의 마지막 수정일
     * @param x
     * @param y
     * @param type
     * @param
     * @return
     */
    public long getDFSTimeStamp(int x, int y, int type, String utcTm){
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        //String strDfsFile = getDirectoryByDate(dfsBaseDirectory, utcTm) + File.separator + Config_Shrn.DFS_PREFIX + Config_Shrn.VCODE[type] + "." + utcTm;
        //File dfsFile = new File(strDfsFile);

        if (type < 0) return 0;


        StringBuffer strDfsFile = new StringBuffer(dfsBaseDirectory).append(File.separator)
                .append(utcTm.substring(0,6)).append(File.separator).append(File.separator)
                .append(utcTm.substring(6,8)).append(File.separator).append(File.separator)
                .append(Config_Shrn.DFS_PREFIX)
                .append(Config_Shrn.VCODE[type]).append(".").append(utcTm);


        //logger.debug("1: 파일명 :["+strDfsFile+"]");
        File dfsFile = new File(strDfsFile.toString());
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        try {
            return dfsFile.lastModified();
        }catch(NullPointerException e) {
            //Logger.getLogger(getClass()).error(e,e);
          //  logger.error("DFS - SHRN lastModified Check Error");
            return 0;
        }
    }
    /**
     * 캐쉬 데이터 업뎃.
     * @param data
     */
    private void setPoint(final DFSPointShrnData data) {
        String kstTm = data.getAnnTime().getKstTm();
        FastHashMap cache = getCache(kstTm);
        cache.setFast(false);
        cache.put(Integer.toString(data.getX()) + "," + Integer.toString(data.getY()), data);
        cache.setFast(true);
    }

    public static String errorFile = "";

    private DFSPointShrnData fillCache(int x, int y, DFSShrnTimeSequence tm) {
        DFSPointShrnData data = new DFSPointShrnData();
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        /* 20140623 다량의 동네예보 에러로그 발생으로 인해 파일확인 전처리 시행*/
		/*String strDfsFile = "";
		for (int i =0; i < Config_Shrn.VCODE.length; i++) {
			if(   Config_Shrn.VCODE[i] == Config_Shrn.VCODE[Config_Shrn.V_R12]
			  || Config_Shrn.VCODE[i] == Config_Shrn.VCODE[Config_Shrn.V_S12] ){
				continue;
			}
			strDfsFile = getDirectoryByDate(dfsBaseDirectory,tm.getUtcTm()) + File.separator
									+ Config_Shrn.DFS_PREFIX
									+ Config_Shrn.VCODE[i]
									+ "." + tm.getUtcTm();
			if( !(new File(strDfsFile)).exists() ) {
				if ( !errorFile.equals(strDfsFile) ){
					errorFile = strDfsFile;
					//System.out.println(errorFile );
				}
				return null;
			}
		}*/

//		for (int i =0; i < Config_Shrn.VCODE.length; i++) {
//			if(   Config_Shrn.VCODE[i] == Config_Shrn.VCODE[Config_Shrn.V_R12]
//					  || Config_Shrn.VCODE[i] == Config_Shrn.VCODE[Config_Shrn.V_S12] ){
//						continue;
//			}
//			String strDfsFile = new StringBuffer(dfsBaseDirectory).append(File.separator).append(Config_Shrn.DFS_PREFIX)
//					.append(Config_Shrn.VCODE[i]).append(".").append(tm.getUtcTm()).toString();
//			logger.debug("2: 파일명 :["+strDfsFile+"]");
//			if( !(new File(strDfsFile)).exists() ) {
//				if ( !errorFile.equals(strDfsFile) ){
//					errorFile = strDfsFile;
//				}
//				return null;
//			}
//		}
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        data.setX(x);
        data.setY(y);
        data.setAnnTime(tm);

        // 3시간별 기온 읽기
        data.setTmp(readRaw(x,y,tm, Config_Shrn.V_TMP));
        // 최고기온
        data.setTmx(readRaw(x,y,tm, Config_Shrn.V_TMX));
        // 최저기온
        data.setTmn(readRaw(x,y,tm, Config_Shrn.V_TMN));
        // 습도
        data.setReh(readRaw(x,y,tm, Config_Shrn.V_REH));
        // 하늘 상태
        data.setSky(readRaw(x,y,tm, Config_Shrn.V_SKY));
        // 강수형태
        data.setPty(readRaw(x,y,tm, Config_Shrn.V_PTY));
        // 강수 확률
        data.setPop(readRaw(x,y,tm, Config_Shrn.V_POP));

        // 06시간누적강수량
//			try { data.setPCP(readRaw(x,y,tm,Config_Shrn.V_PCP)); } catch(Exception innerEx) { logger.error(innerEx.getMessage(), innerEx);}
//			// 06시간누적적설량
//			try { data.setSNO(readRaw(x,y,tm,Config_Shrn.V_SNO)); } catch(Exception innerEx) { logger.error(innerEx.getMessage(), innerEx);}

        // 06시간누적강수량
        data.setPcp(readRaw(x,y,tm, Config_Shrn.V_PCP));
        // 06시간누적적설량
        data.setSno(readRaw(x,y,tm, Config_Shrn.V_SNO));

        // 동서방향풍속
        data.setUuu(readRaw(x,y,tm, Config_Shrn.V_UUU));
        // 남북방향풍속
        data.setVvv(readRaw(x,y,tm, Config_Shrn.V_VVV));
        // 파고 - 파고는 읽지 않는다.
//			try { data.setWav(readRaw(x,y,tm,Config_Shrn.V_WAV)); } catch(Exception innerEx) {logger.error(innerEx.getMessage(), innerEx); }
//
//			try { data.setVec(readRaw(x,y,tm,Config_Shrn.V_VEC)); } catch(Exception innerEx) {logger.error(innerEx.getMessage(), innerEx); }
//			try { data.setWsd(readRaw(x,y,tm,Config_Shrn.V_WSD)); } catch(Exception innerEx) {logger.error(innerEx.getMessage(), innerEx); }

        data.setWav(readRaw(x,y,tm, Config_Shrn.V_WAV));

        data.setVec(readRaw(x,y,tm, Config_Shrn.V_VEC));
        data.setWsd(readRaw(x,y,tm, Config_Shrn.V_WSD));

        data.initDFSShrnWeather();
        data.updateTimeStamp();
        // Cache updat;
        setPoint(data);

        return data;
    }
    /**
     * 가장 최근 GEMD 에서 데이터 추출
     * mhh
     * @param x
     * @param y
     * @param nowTm
     * @return
     */
    private DFSPointShrnData loadLastDFS(int x, int y, DFSShrnTimeSequence nowTm) {
        DFSPointShrnData data = new DFSPointShrnData();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        Calendar nowCal = nowTm.getUtcCal();

        /*
         * 3시간씩 감소시키면서 GEMD 데이터 파일이 존재하는지 검사 최대 24시간까지(8번)
         */
        String lastTm="";
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
/*
		File dfsRoot = new File(getDirectoryByDate(dfsBaseDirectory, nowTm.getUtcTm()));
		for(int i = 0; i < 8 ; i++) {  // mhh
			nowCal.add(Calendar.HOUR_OF_DAY, -3); // mhh
			// 변수정보 원시데이터중 LGT 낙뢰 데이터가 가장 마지막에 생성되는 것 같음
			lastTm = df.format(nowCal.getTime());
			File dfsData = new File(dfsRoot,Config_Shrn.DFS_PREFIX + "LGT." + lastTm);
			if(dfsData.exists()){
				break;
			}
		}
*/
        for(int i = 0; i < 8 ; i++) {  // mhh
            nowCal.add(Calendar.HOUR_OF_DAY, -3); //
            lastTm = df.format(nowCal.getTime());
            File dfsData = new File(dfsBaseDirectory,new StringBuffer(Config_Shrn.DFS_PREFIX).append("LGT.").append(lastTm).toString());
            //logger.debug("3: 파일명 :["+dfsData+"]");
            if(dfsData.exists()){
                break;
            }
        }
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        /* 시간 순서 재배열 */
        nowTm.setUtcCal(nowCal);

        data.setX(x); data.setY(y); data.setAnnTime(nowTm);
        // 3시간별 기온 읽기
        data.setTmp(readRaw(x,y,nowTm, Config_Shrn.V_TMP));
        // 최고기온
        data.setTmx(readRaw(x,y,nowTm, Config_Shrn.V_TMX));
        // 최저기온
        data.setTmn(readRaw(x,y,nowTm, Config_Shrn.V_TMN));
        // 습도
        data.setReh(readRaw(x,y,nowTm, Config_Shrn.V_REH));
        // 하늘 상태
        data.setSky(readRaw(x,y,nowTm, Config_Shrn.V_SKY));
        // 강수형태
        data.setPty(readRaw(x,y,nowTm, Config_Shrn.V_PTY));
        // 강수 확률
        data.setPop(readRaw(x,y,nowTm, Config_Shrn.V_POP));

		/*
		// 06시간누적강수량
		try { data.setPCP(readRaw(x,y,nowTm,Config_Shrn.V_PCP)); } catch(Exception innerEx) { }
		// 06시간누적적설량
		try { data.setSNO(readRaw(x,y,nowTm,Config_Shrn.V_SNO)); } catch(Exception innerEx) { }
		*/

        // 06시간누적강수량
        data.setPcp(readRaw(x,y,nowTm, Config_Shrn.V_PCP));
        // 06시간누적적설량
        data.setSno(readRaw(x,y,nowTm, Config_Shrn.V_SNO));

        // 동서방향풍속
        data.setUuu(readRaw(x,y,nowTm, Config_Shrn.V_UUU));
        // 남북방향풍속
        data.setVvv(readRaw(x,y,nowTm, Config_Shrn.V_VVV));

		/*
		// 파고 - 파고는 읽지 않는다.
		try { data.setWav(readRaw(x,y,nowTm,Config_Shrn.V_WAV)); } catch(Exception innerEx) { logger.error(innerEx.getMessage(), innerEx);}

		try { data.setVec(readRaw(x,y,nowTm,Config_Shrn.V_VEC)); } catch(Exception innerEx) { logger.error(innerEx.getMessage(), innerEx);}
		try { data.setWsd(readRaw(x,y,nowTm,Config_Shrn.V_WSD)); } catch(Exception innerEx) { logger.error(innerEx.getMessage(), innerEx);}
		*/

        // 파고 - 파고는 읽지 않는다.
        data.setWav(readRaw(x,y,nowTm, Config_Shrn.V_WAV));

        data.setVec(readRaw(x,y,nowTm, Config_Shrn.V_VEC));
        data.setWsd(readRaw(x,y,nowTm, Config_Shrn.V_WSD));

        data.initDFSShrnWeather();
        data.updateTimeStamp();
        setPoint(data);

        return data;
    }
    /** mhh
     * 원시 데이터 로드
     * 20100611 SHRN 데이터 사용 안함 --> SHRT사용으로 변경
     * @param x 1 base numbering
     * @param y 1 base numbering
     * @param tm
     * @param type
     * @return
     * @throws IOException
     */
    private DFSShrnRaw readRaw(int x, int y, DFSShrnTimeSequence tm, int type) {
        DFSShrnRaw raw = null;
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        //String strDfsFile = getDirectoryByDate(dfsBaseDirectory, tm.getUtcTm()) + File.separator + Config_Shrn.DFS_PREFIX + Config_Shrn.VCODE[type] + "." + tm.getUtcTm();
        //File dfsFile = new File(strDfsFile); // mhh 파일path , 파일명 초단기 신규 prod

        if (type < 0 || tm.getTmSeq() < 0) return raw;

        StringBuffer strDfsFile = new StringBuffer(dfsBaseDirectory)
                .append(tm.getUtcTm().substring(0,6)).append(File.separator)
                .append(tm.getUtcTm().substring(6,8)).append(File.separator)
                .append(Config_Shrn.DFS_PREFIX)
                .append(Config_Shrn.VCODE[type]).append(".").append(tm.getUtcTm());
        System.out.println(strDfsFile);
        //logger.debug("4: 파일명 :["+strDfsFile+"]");
        File dfsFile = new File(strDfsFile.toString());
        /*****2018.04.03 - 홈페이지 NAS를 읽어 이미지 파일 처리하도록 변경함에 따라 수정*****/
        /* Convert to zerobase */
        x--; y--;
        RandomAccessFile ra=null;

        try{
            ra = new RandomAccessFile(dfsFile,"r");

            int fctcnt = Config_Shrn.FCT_CNT[type][tm.getTmSeq()];  // td count
            int unitSize = Config_Shrn.GRID_WIDTH * Config_Shrn.GRID_HEIGHT * 4;  // mhh 150788

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
             */
            for(int i = 0 ; i < fctcnt; i++) {
                ra.seek( 256 + i * unitSize + ((long)y * Config_Shrn.GRID_WIDTH*4) + ((long)x * 4) );
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
            raw = new DFSShrnRaw(type,body,dfsFile.lastModified());

        }catch(FileNotFoundException e) {
            //logger.debug(e.getMessage(),e);
        }catch(IOException e) {
            //logger.debug(e.getMessage(),e);
        }finally{
            if(ra!=null) { try { ra.close(); } catch(IOException ee) {
              //  logger.debug(ee.getMessage(),ee);
            } };
        }
        return raw;
    }
    public long getVersion(int x, int y, String kstTmFc) throws Exception{
        return this.getPastPoint(x, y, kstTmFc.substring(0,10)).getTimeStamp();
    }

}
