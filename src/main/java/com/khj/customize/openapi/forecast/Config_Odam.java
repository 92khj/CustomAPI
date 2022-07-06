package com.khj.customize.openapi.forecast;

/**
 * 디지털 실황 예보(동네 예보) 기본 설정 로드 및 상수 집합
 *
 * @author Seagull
 * @version 1.0
 */
public class Config_Odam {
    public static boolean isTest = false;
    /**
     * 격자 데이터 Width
     */
    public static final int GRID_WIDTH = 149;

    /**
     * 격자 데이터 Height
     */
    public static final int GRID_HEIGHT = 253;

    /**
     * 실황 최대 갯수
     */
    public static final int MAX_FCT_CNT = 1;

    /** mhh
     * 발표시간별 실황 예보 갯수
     * 0 : "T1H" 1시간별 기온
     * 1 : "REH" 상대 습도
     * X : "SKY" 하늘상태 - 20181010 삭제
     * 2 : "PTY" 강수형태
     * 3 : "UUU" 동서방향풍속
     * 4 : "VVV" 남북방향풍속
     * 5 : "RN1" 1시간 강수량
     * X : "LGT" 낙뢰 - 20181010 삭제
     * 6 : "VEC" 풍향 degree
     * 7 " "WSD" 풍속 ms?
     * x : 변수 형식 코드
     * y : 시간 Seq
     * 실황은 한건이다.
     */
    public static final int[][] FCT_CNT = new int[][]{
//       0,1,2,3,4,5,6,7,8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23
//		 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//		{1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
//	    {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1,1,1,1,1,1,1,1,1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
    };

    /**
     * 파일명 Prefix (DFS_SHRT_GRD_GEMD_변수코드.년월일시분)
     */
    public static final String DFS_PREFIX = "DFS_ODAM_GRD_GEMD_";   // 실황

    /**
     * 발표시간별 실황 예보 갯수
     * 0 : "T1H" 1시간별 기온
     * 1 : "REH" 상대 습도
     * X : "SKY" 하늘상태 - 2018.10.10 부터 미제공
     * 2 : "PTY" 강수형태
     * 3 : "UUU" 동서방향풍속
     * 4 : "VVV" 남북방향풍속
     * 5 : "RN1" 1시간 강수량
     * X : "LGR" 낙뢰 여부 - 2018.10.10 부터 미제공
     * 6 : "VEC" 풍향 degree
     * 7 : "WSD" 풍속 ms?
     */
    public static final String[] VCODE = new String[] {
            "T1H",
            "REH",
            //"SKY",
            "PTY",
            "UUU",
            "VVV",
            "RN1",
            // "LGT",
            "VEC",
            "WSD"
    };
    public static final int V_T1H = 0;
    public static final int V_REH = 1;
    //public static final int V_SKY = 2;
    public static final int V_PTY = 2;
    public static final int V_UUU = 3;
    public static final int V_VVV = 4;
    public static final int V_RN1 = 5;
    //public static final int V_LGT = 7;
    public static final int V_VEC = 6;
    public static final int V_WSD = 7;
}
