package com.khj.customize.openapi.forecast;

/**
 * DFS 격자점 한지점 변수별 시계열 원시 포맷 <br />
 * 자료포맷은 이진 파일이고
 * 앞에 256 bytes 의 헤더영역이 있고
 * 뒤에 149*253 개의 격자점값이 float (4bytes) 형식으로 저장된 판이
 * 첨부한 예보시간수+1 개 만큼 저장되어 있습니다.
 * 여기서 +1은 앞에 +1H 이 더 저장되어 있다는 것이고 +1H 자료를 사용하지 않으면 됩니다.
 * <br /> <br />
 * 역할 : 한 변수 한 지점의 20개 예보시간의 원시 데이터를 표현
 * @author Seagull
 *
 */
public class DFSShrnRaw {
    /** 타임스탬프 */
    long timeStamp;
    public long getTimeStamp() { return this.timeStamp; }
    public void setTimeStamp(long ts) { this.timeStamp = ts; }

    int type;
    float[] data;

    int x;
    int y;

    public DFSShrnRaw() {
        this.type = -1;
        this.data = new float[Config_Shrn.MAX_FCT_CNT];
        this.timeStamp = -1l;
    }

    /**
     * 생성 ( with TimeStamp )
     * @param type
     * @param tmFc
     * @param bin
     * @param ts
     */
    public DFSShrnRaw(int type, byte[] bin, long ts){
        super();
        int blength = bin.length;
        int flength = blength/4;
        byte[] buf = new byte[4];
        // bit sequence resort
        this.data = new float[flength];
        for(int i = 0 ; i < blength ; i+=4){
            System.arraycopy(bin, i, buf, 0, 4);

            data[(int)(i/4)] = Float.intBitsToFloat(((buf[3]<<24)&0xFF000000) |
                    ((buf[2]<<16)&0xFF0000) |
                    ((buf[1]<<8)&0xFF00) |
                    (buf[0]&0xFF));
			/*
			ByteBuffer bb = ByteBuffer.allocate(4);
			bb.put(swapbuf);
			data[(int)(i/4)] = bb.getFloat(0);
			*/
        }

        this.type = type;
        this.timeStamp = ts;
    }

    public int getX() { return this.x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return this.y; }
    public void setY(int y) { this.y = y; }

    public float[] getData() {	return data; }
    public void setData(float[] data) { this.data = data; }

    public int getType() { return type; }
    public void setType(int type) { this.type = type; }
}
