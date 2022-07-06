package com.khj.customize.openapi.forecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DFSOdamTimeSequence {
    Calendar utcCal;

    /** UTC tm */
    String utcTm;
    /** KST tm */
    String kstTm;
    /** 시간 순서 */
    int tmSeq;

    public String getKstTm() {
        return kstTm;
    }
    public void setKstTm(String kstTm) {
        this.kstTm = kstTm;
    }
    public String getUtcTm() {
        return utcTm;
    }
    public void setUtcTm(String tm) {
        this.utcTm = tm;
    }
    public int getTmSeq() {
        return tmSeq;
    }
    public void setTmSeq(int tmSeq) {
        this.tmSeq = tmSeq;
    }
    public DFSOdamTimeSequence(){
        super();
        this.utcTm = "";
        this.kstTm = "";
        this.tmSeq = 0;
    }
    public DFSOdamTimeSequence(Calendar utcCal) {
        super();
        this.setUtcCal(utcCal);
    }
    public DFSOdamTimeSequence(String utcTm, int tmSeq) {
        super();
        this.utcTm = utcTm;
        this.kstTm = utcToKst(utcTm);
        this.tmSeq = tmSeq;
    }
    public DFSOdamTimeSequence(String utcTm, String kstTm, int tmSeq) {
        super();
        this.utcTm = utcTm;
        this.kstTm = kstTm;
        this.tmSeq = tmSeq;
    }
    public String utcToKst(String utcTm){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        try{
            Date tDate = df.parse(utcTm);
            tDate.setTime(tDate.getTime() + 1000*60*60*9);
            return df.format(tDate);
        }catch(NullPointerException e) {

            return utcTm;
        } catch (ParseException e) {

            return utcTm;
        }
    }
    public Calendar getUtcCal() {
        return utcCal;
    }
    public void setUtcCal(Calendar utcCal) {
        this.utcCal = utcCal;
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm");
        this.utcTm = df.format(utcCal.getTime());

        utcCal.add(Calendar.HOUR_OF_DAY, 9);
        this.tmSeq = (int)((utcCal.get(Calendar.HOUR_OF_DAY)-1));  // 현재발표시간의 배열순번 조회
        if( this.getTmSeq() == -1 ) {
            this.setTmSeq(23);
        }

        this.kstTm = df.format(utcCal.getTime());
        utcCal.add(Calendar.HOUR_OF_DAY, -9);
    }

    public Calendar getKstCal() {
        Calendar kstCal = (Calendar)this.utcCal.clone();
        kstCal.add(Calendar.HOUR_OF_DAY, 9);
        return kstCal;
    }
    public String toString() {
        return this.getKstTm() + "(KST)";
    }
}

