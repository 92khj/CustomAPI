package com.khj.customize.openapi.util;

//import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    /**
     * Returns the specified string converted to a format suitable for
     * HTML. All signle-quote, double-quote, greater-than, less-than and
     * ampersand characters are replaces with their corresponding HTML
     * Character Entity code.
     *
     * @param in the String to convert
     * @return the converted String
     */

    public static String toHTMLString(String in) {
        if(in == null) return "";
        StringBuffer out = new StringBuffer();
        for (int i =0; in != null && i < in.length() ; i++) {
            char c = in.charAt(i);
            switch(c){
                case '\'':
                    out.append("&#39;"); break;
                case '"':
                    out.append("&quot;"); break;
                case '<':
                    out.append("&lt;"); break;
                case '>':
                    out.append("&gt;"); break;
                case '&':
                    out.append("&amp;"); break;
                default :
                    out.append(c);
            }
        }
        return out.toString();
    }
    public static String stripSpecialChar(String in) {
        if(in == null) return "";
        StringBuffer out = new StringBuffer();
        for (int i =0; in != null && i < in.length() ; i++) {
            char c = in.charAt(i);
            switch(c){
                case '\'':
                    out.append("&#39;"); break;
                case '<':
                    out.append("&lt;"); break;
                case '>':
                    out.append("&gt;"); break;
                default :
                    out.append(c);
            }
        }
        return out.toString();
    }
    public static String stripSpecialChar(String in, String defVal) {
        if(in == null || "".equals(in)) return defVal;
        StringBuffer out = new StringBuffer();
        for (int i =0; in != null && i < in.length() ; i++) {
            char c = in.charAt(i);
            switch(c){
                case '\'':
                    out.append("&#39;"); break;
                case '<':
                    out.append("&lt;"); break;
                case '>':
                    out.append("&gt;"); break;
                default :
                    out.append(c);
            }
        }
        return out.toString();
    }
    public static String getCookieValue(HttpServletRequest request, String name){
        Cookie c = getCookie(request,name);
        return (c==null)?"":c.getValue();
    }
    public static Cookie getCookie(HttpServletRequest request, String name){
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;

        if(cookies != null){
            for(int i=0;i < cookies.length; i++){
                if(cookies[i].getName().equals(name)){
                    returnCookie = cookies[i];
                    break;
                }
            }
        }
        return returnCookie;
    }
    public static void setCookie(HttpServletResponse response, String name, String value){
        Cookie cookie = new Cookie(name, value);
        response.addCookie(cookie);
    }
    public static void setCookie(HttpServletResponse response, String name, String value, int age){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        response.addCookie(cookie);
    }
    /**
     * 파일 카피
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copyFile(File in, File out) throws IOException {
        copyFile(new FileInputStream(in),new FileOutputStream(out));
    }
    public static void copyFile(FileInputStream in, FileOutputStream out) throws IOException {
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(),
                    outChannel);
        }
        catch (IOException e) {
            //Logger.getLogger(Util.class).error(e);
            throw e;
        }
        finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }
    /**
     * 예보 발표시간포멧을 Calendar 로 변환
     * @param tmFc
     * @return
     */
    public static Calendar getTmFc2Cal(String tmFc){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmm",java.util.Locale.KOREAN);
        try {
            Date tDate = df.parse(tmFc);
            Calendar cal = Calendar.getInstance();
            cal.setTime(tDate);
            return cal;
        }catch(NullPointerException e) {
            //Logger.getLogger(Util.class).error(e);
            return Calendar.getInstance();
        }catch(ParseException e) {
            //Logger.getLogger(Util.class).error(e);
            return Calendar.getInstance();
        }
    }
    public static Calendar getTmFc2Cal(String tmFc, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern,java.util.Locale.KOREAN);
        try {
            Date tDate = df.parse(tmFc);
            Calendar cal = Calendar.getInstance();
            cal.setTime(tDate);
            return cal;
        }catch(NullPointerException e) {
            //Logger.getLogger(Util.class).error(e);
            return Calendar.getInstance();
        }catch(ParseException e) {
            //Logger.getLogger(Util.class).error(e);
            return Calendar.getInstance();
        }
    }
    public static int convertToInteger(String num, int def) {
        try{
            return Integer.parseInt(num);
        }catch(NumberFormatException e){
            //Logger.getLogger(Util.class).error(e);
            return def;
        }
    }
    public static double convertToDouble(String num, double def) {
        try{
            return Double.parseDouble(num);
        }catch(NumberFormatException e){
            //Logger.getLogger(Util.class).error(e);
            return def;
        }
    }
    public static String getCuttedString(String str,int max) {
        int tLen =str.length();
        int count = 0;
        char c;
        int s=0;
        for(s=0;s<tLen;s++){
            c = str.charAt(s);
            if(count > max) break;
            if(c>127) count +=2;
            else count ++;
        }
        return (tLen >s)? str.substring(0,s)+"..." : str;
    }
    /**
     * 패턴 검색
     * @param str
     * @param patternStr
     * @return
     */
    public static boolean eregCheck(String str , String patternStr){
        if(str == null || patternStr == null) return false;
        Pattern pattern = Pattern.compile(patternStr);
        Matcher fit = pattern.matcher(str);
        return fit.matches();
    }
}
