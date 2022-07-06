package com.khj.customize.openapi.forecast;



/**
 * 디지털 초단기 예보(동네 예보) 기본 설정 로드 및 상수 집합
 *
 * @version 1.0
 */
public class Config_Vsrt {
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
     * 시간별 최대 예보 갯수
     */
    public static  int MAX_FCT_CNT = 4;

    /**
     * 발표시간별 실황 예보 갯수
     * 0 : "PTY" 강수형태
     * 1 : "RN1" 1시간 강수량
     * 2 : "SKY" 하늘상태
     *
     * x : 변수 형식 코드
     * y : 시간 Seq
     */

    // 파일에서 가져오는 판수
    //20191010 초단기 예보 예측 시간 변경 (2~4시간 -> 4~6시간) 에 따른 판수 변경
/*	public static int[][] FCT_CNT = new int[][]{
		//      0:30,     1:30,     2:30,     3:30,     4:30,     5:30,     6:30,     7:30,   8:30,     9:30,    10:30,  11:30,  12:30,  13:30,  14:30,   15:30, 16:30,   17:30,  18:30,  19:30,  20:30,  21:30,  22:30,    23:30
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // 강수형태
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // 하늘상태
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 },  // 1시간 강수량
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // LGT
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // T1H
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // REH
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // UUU
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // VVV
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 }, // VEC
		{      3,        2,        4,        3,        2,        4,        3,        2,        4,        3,        2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4,       3,       2,       4 } // WSD
		};
*/
    //20191010 초단기 예보 예측 시간 변경 (2~4시간 -> 4~6시간) 에 따른 판수 변경
//	public static int[][] FCT_CNT = new int[][]{
//		//      0:30,     1:30,     2:30,     3:30,     4:30,     5:30,     6:30,     7:30,   8:30,     9:30,    10:30,  11:30,  12:30,  13:30,  14:30,   15:30, 16:30,   17:30,  18:30,  19:30,  20:30,  21:30,  22:30,  23:30
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 강수형태(PTY)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 하늘상태(SKY)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 1시간강수량(RN1)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 낙뢰(LGT)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 기온(T1H)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 습도(REH)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 동서바람(UUU)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 남북바람(VVV)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 풍향(VEC)
//		{      6,        5,        4,        6,        5,        4,        6,        5,        4,        6,        5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4,       6,       5,       4 }, // 풍속(WSD)
//		};
//
    //20210608 초단기 예보 예측 시간 변경 (4~6시간 -> 일괄 6시간) 에 따른 판수 추가
    public static int[][] FCT_CNT_EQ = new int[][]{
            //      0:30,     1:30,     2:30,     3:30,     4:30,     5:30,     6:30,     7:30,   8:30,     9:30,    10:30,  11:30,  12:30,  13:30,  14:30,   15:30, 16:30,   17:30,  18:30,  19:30,  20:30,  21:30,  22:30,  23:30
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 강수형태(PTY)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 하늘상태(SKY)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 1시간강수량(RN1)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 낙뢰(LGT)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 기온(T1H)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 습도(REH)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 동서바람(UUU)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 남북바람(VVV)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 풍향(VEC)
            {      6,        6,        6,        6,        6,        6,        6,        6,        6,        6,        6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6,       6 }, // 풍속(WSD)
    };



    // 화면에서 사용하는 판수
/*	public static int[][] FCT_DISP_CNT = new int[][]{
		// 20100611 초단기 화면에서 사용하는 판수 수정
	//       0,  030,  1, 130,  2, 230,  3,  330,  4, 430,  5, 530,  6, 630,  7,  730, 8,  830,  9, 930, 10,1030, 11,1130,12,1230,13,1330,14,1430, 15,1530,16,1630,17,1730, 18,1830, 19,1930,20,2030,21,2130,22,2230, 23,2330,
			{ 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4}, // 강수형태
			{ 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4}, // 하늘상태
			{ 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4},  // 1시간 강수량
		    { 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4 },  // LGT
		    { 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4 },  // T1H
		    { 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4 },  // REH
		    { 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4 },  // UUU
		    { 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4 },  // VVV
		    { 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4 },  // VEC
		    { 3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4,   3,   3,   2,   2,   2,   4 }  // WSD
		};
		*/
    /**
     * 파일명 Prefix (DFS_SHRT_GRD_GEMD_변수코드.년월일시분)
     */
    public static final String DFS_PREFIX = "DFS_VSRT_GRD_GEMD_";   // 실황

    /**
     * 발표시간별 실황 예보 갯수
     * 0 : "PTY" 강수형태
     * 1 : "RN1" 1시간 강수량
     * 2 : "SKY" 하늘상태
     * 3 : "LGT" 낙뢰여부
     * 4 : "T1H" 1시간별 기온
     * 5 : "REH" 상대 습도
     * 6 : "UUU" 동서방향풍속
     * 7 : "VVV" 남북방향풍속
     * 8 : "VEC" 풍향 degree
     * 9 " "WSD" 풍속 ms?
     *
     */
    public static final String[] VCODE = new String[] {
            "PTY",
            "RN1",
            "SKY",
            "LGT",
            "T1H",
            "REH",
            "UUU",
            "VVV",
            "VEC",
            "WSD"
    };
    public static final int V_PTY = 0;
    public static final int V_RN1 = 1;
    public static final int V_SKY = 2;
    public static final int V_LGT = 3;
    public static final int V_T1H = 4;
    public static final int V_REH = 5;
    public static final int V_UUU = 6;
    public static final int V_VVV = 7;
    public static final int V_VEC = 8;
    public static final int V_WSD = 9;
}