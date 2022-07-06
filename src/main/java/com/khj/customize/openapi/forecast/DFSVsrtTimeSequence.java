package com.khj.customize.openapi.forecast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DFSVsrtTimeSequence {
    Calendar utcCal;
    Calendar orgCal;

    /** UTC tm */
    String utcTm;
    /** KST tm */
    String kstTm;
    /** �ð� ���� */
    int tmSeq;

    int orgTmSeq;

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
    public int getOrgTmSeq() {
        return orgTmSeq;
    }
    public void setOrgTmSeq(int orgTmSeq) {
        this.orgTmSeq = orgTmSeq;
    }

    public DFSVsrtTimeSequence(){
        super();
        this.utcTm = "";
        this.kstTm = "";
        this.tmSeq = 0;
    }
    public DFSVsrtTimeSequence(Calendar utcCal) {
        super();
        this.orgCal = (Calendar)utcCal.clone();
        this.setUtcCal(utcCal);
    }

    public DFSVsrtTimeSequence(Calendar utcCal , Calendar tm) {
        super();
        this.orgCal = (Calendar)tm.clone();
        this.setUtcCal(utcCal);
    }


    public DFSVsrtTimeSequence(String utcTm, int tmSeq) {
        super();
        this.utcTm = utcTm;
        this.kstTm = utcToKst(utcTm);
        this.tmSeq = tmSeq;
    }
    public DFSVsrtTimeSequence(String utcTm, String kstTm, int tmSeq) {
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
        //orgCal.add(Calendar.HOUR_OF_DAY, 9);

//
//		System.out.println(this.getClass().getName()+".setUtcCal() Calendar.HOUR_OF_DAY : " + Calendar.HOUR_OF_DAY + " ############# \n ");
//		System.out.println(this.getClass().getName()+".setUtcCal() utcCal.get(Calendar.HOUR_OF_DAY) : " + utcCal.get(Calendar.HOUR_OF_DAY) + " ############# \n ");
//		System.out.println(this.getClass().getName()+".setUtcCal() orgCal.get(Calendar.HOUR_OF_DAY) : " + orgCal.get(Calendar.HOUR_OF_DAY) + " ############# \n ");
//		System.out.println(this.getClass().getName()+".setUtcCal() orgCal.get(Calendar.MINUTE) : " + orgCal.get(Calendar.MINUTE) + " ############# \n ");
//
//		System.out.println(this.getClass().getName()+".setUtcCal() (utcCal.get(Calendar.HOUR_OF_DAY)-2) : " + (utcCal.get(Calendar.HOUR_OF_DAY)-2) + " ############# \n ");
//	    System.out.println(this.getClass().getName()+".setUtcCal() ((utcCal.get(Calendar.HOUR_OF_DAY)-2)/3) : " + ((utcCal.get(Calendar.HOUR_OF_DAY)-2)/3) + " ############# \n ");
        //System.out.println(this.getClass().getName()+".setUtcCal()  : " +  + " ############# \n ");

        //this.tmSeq = (int)((utcCal.get(Calendar.HOUR_OF_DAY)-2)/3);
        this.tmSeq = (int)((utcCal.get(Calendar.HOUR_OF_DAY)));

        int i = 0 ;  // �Ž� 30���� ������ ������ ���۵Ǿ�´�.

        //System.out.println(this.getClass().getName()+".setUtcCal() orgCal.get(Calendar.MINUTE): " + orgCal.get(Calendar.MINUTE) + " ############# \n ");

        if( orgCal.get(Calendar.MINUTE) >= 30 ) {
            i = 1;
        }
        //this.orgTmSeq = (int ) (  ( (utcCal.get(Calendar.HOUR_OF_DAY)) * 2 ) + i );
        this.orgTmSeq = (int ) (  ( (orgCal.get(Calendar.HOUR_OF_DAY)) * 2 ) + i );


//		System.out.println(this.getClass().getName()+".setUtcCal() this.tmSeq : " + this.tmSeq + " ############# \n ");
//		System.out.println(this.getClass().getName()+".setUtcCal() this.orgTmSeq : " + this.orgTmSeq + " ############# \n ");

        //this.tmSeq = (int)utcCal.get(Calendar.HOUR_OF_DAY+1); //
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
