package com.khj.customize.openapi.forecast;

public class Config_Shrn {
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
     * 시계열3 시간별 최대 예보 갯수
     * 2014년 예보연장 이전.
     */

    public static int MAX_FCT_CNT = 23;  // 변경처리  갯수가 늘어남

    /**
     *
     0. "TMP", // 1시간 기온
     1. "TMX", // 일 최고 기온
     2. "TMN", // 일 최저 기온
     3. "UUU", // 동서 바람 성분
     4. "VVV", // 남북 바람 성분
     5. "VEC", // 풍향
     6. "WSD", // 풍속
     7. "SKY", // 하늘 상태
     8. "PTY", // 강수 형태
     9. "POP", // 강수 확률
     10. "PCP", // 1시간 강수량
     11. "SNO", // 1시간 신적설
     12. "REH", // 습도
     13. "WAV" // 파고
     * x : 변수 형식 코드
     * y : 시간 Seq
     *
     * @author codep
     **/


    public static final String DFS_PREFIX = "DFS_SHRT_GRD_GEMD_HR01_";


    //	2021.06.18. 전체 요소 일괄 1시간 단위 시간 상세화로 인한 변경
    public static int[][] FCT_CNT = new int[][] {
            //	  2,  5,  8, 11, 14, 17, 20, 23
            {70, 67, 64, 61, 58, 79, 76, 73},  // 1시간 기온 		:: TMP
            { 4,  4,  4,  4,  3,  4,  4,  4},  // 최고 기온  		:: TMX
            { 4,  3,  3,  3,  3,  4,  4,  4},  // 최저 기온  		:: TMN
            {70, 67, 64, 61, 58, 79, 76, 73},  // 동서 바람 성분	:: UUU
            {70, 67, 64, 61, 58, 79, 76, 73},  // 남북 바람 성분	:: VVV
            {70, 67, 64, 61, 58, 79, 76, 73},  // 풍향			:: VEC
            {70, 67, 64, 61, 58, 79, 76, 73},  // 풍속			:: WSD
            {70, 67, 64, 61, 58, 79, 76, 73},  // 하늘 상태		:: SKY
            {70, 67, 64, 61, 58, 79, 76, 73},  // 강수 형태		:: PTY
            {70, 67, 64, 61, 58, 79, 76, 73},  // 강수 확률		:: POP
            {70, 67, 64, 61, 58, 79, 76, 73},  // 1시간 강수량		:: PCP
            {70, 67, 64, 61, 58, 79, 76, 73},  // 1시간 신적설		:: SNO
            {70, 67, 64, 61, 58, 79, 76, 73},  // 습도			:: REH
            {70, 67, 64, 61, 58, 79, 76, 73}   // 파고			:: WAV
    };


    public static final String[] VCODE = new String[] {
            "TMP", // 1시간 기온
            "TMX", // 일 최고 기온
            "TMN", // 일 최저 기온
            "UUU", // 동서 바람 성분
            "VVV", // 남북 바람 성분
            "VEC", // 풍향
            "WSD", // 풍속
            "SKY", // 하늘 상태
            "PTY", // 강수 형태
            "POP", // 강수 확률
            "PCP", // 1시간 강수량
            "SNO", // 1시간 신적설
            "REH", // 습도
            "WAV"  // 파고
    };


    public static final int V_TMP = 0;
    public static final int V_TMX = 1;
    public static final int V_TMN = 2;
    public static final int V_UUU = 3;
    public static final int V_VVV = 4;
    public static final int V_VEC = 5;
    public static final int V_WSD = 6;
    public static final int V_SKY = 7;
    public static final int V_PTY = 8;
    public static final int V_POP = 9;
    public static final int V_PCP = 10;
    public static final int V_SNO = 11;
    public static final int V_REH = 12;
    public static final int V_WAV = 13;

}