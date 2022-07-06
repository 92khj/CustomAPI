package com.khj.customize.openapi.util;


import javax.activation.FileTypeMap;
import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 문자열 관련 유틸리티 모음
 * @author 이시걸 <sglee.gm@gmail.com>
 *
 */
public class StringUtils{

    /**
     * ',' 추가된 숫자로
     * @param num
     * @return
     */
    public static String toNumberFormat(int num) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(0);
        return nf.format(num);
    }
    /**
     * 화씨를 섭씨로
     * @param f
     * @return
     */
    public static String f2c(String f) {
        if(f == null || f.trim().equals("")) return "-";
        double fc = StringUtils.toDouble(f,-999);
        if(fc == -999) return "-";
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        return nf.format((fc-32.0)/1.8);
    }
    public static String inch2mm(String inch) {
        if(inch == null || inch.trim().equals("")) return "-";
        double fc = StringUtils.toDouble(inch,-999);
        if(fc == -999) return "-";
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(3);
        return nf.format(fc/25.4);
    }
    public static String inch2cm(String inch) {
        if(inch == null || inch.trim().equals("")) return "-";
        double fc = StringUtils.toDouble(inch,-999);
        if(fc == -999) return "-";
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(fc/2.54);
    }
    /**
     * 날짜 Parse
     * @param dtStr
     * @param pattern
     * @return
     */
    public static Date parseDate(String dtStr , String pattern) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dtStr);
        }catch( ParseException e ) {
            return new Date();
        }
    }
    public static Date parseDate(String dtStr , String pattern, Date defaultDate) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dtStr);
        }catch( ParseException e ) {
            return defaultDate;
        }
    }
    /**
     * BASE64 인코딩
     * @param str
     * @return
     */
    /*
    public static String base64Encode(String str) {
        try {
            return Base64Coder.encodeString(str);
        }catch(NullPointerException e) {
            logger.debug(e.getMessage());
            return str;
        }
    }
    public static String base64Encode(String str, String encoding) {
        try {
            return Base64Coder.encodeString(str, encoding);
        }catch(NullPointerException e) {
            return str;
        }catch(UnsupportedEncodingException e) {
            return str;
        }

    }
    /**
     * BASE64 디코딩
     * @param str
     * @return
     */
    /*
    public static String base64Decode(String str) {
        try {
            return Base64Coder.decodeString(str);
        }catch(NullPointerException e) {
            return str;
        }
    }
    */
    public static Date safeParseDate(String dtStr , String pattern) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.parse(dtStr);
        }catch( NullPointerException e ) {
            return new Date();
        }catch(ParseException e) {
            return new Date();
        }
    }
    /**
     * 파일 확장자 조회
     * @param fileName
     * @return
     */
    public static String getFileExtension(String fileName) {
        if(fileName == null) return "";
        int pos = fileName.lastIndexOf(".");
        if(pos < 0) return "";
        return fileName.substring(pos+1, fileName.length());
    }
    /**
     * 확장자를 제외한 이름 조회
     * @param fileName
     * @return
     */
    public static String getFileBodyName(String fileName) {
        if(fileName == null) return "";
        int pos = fileName.lastIndexOf(".");
        if(pos < 0) return "";
        return fileName.substring(0, pos);
    }
    /**
     * 전화번호 검사
     * @param phoneNumber
     * @return
     */
    public static boolean checkNumberFormatPhone(String phoneNumber){
        if(phoneNumber == null) return false;
        return Pattern.matches("^0[2345][1-9]{0,1}-[0-9]{3,4}-[0-9]{4}$",phoneNumber);
    }
    /**
     * 휴대폰 번호 검사
     * @param mobileNumber
     * @return
     */
    public static boolean checkNumberFormatMobile(String mobileNumber){
        if(mobileNumber == null) return false;
        return Pattern.matches("^01[167890]-[0-9]{3,4}-[0-9]{4}$",mobileNumber);
    }
    /**
     * 이메일 형식 검사
     * @param email
     * @return
     */
    public static boolean checkStringFormatEmail(String email){
        if (email==null) return false;
        return Pattern.matches(
                "[\\w\\~\\-\\.]+@[\\w\\~\\-]+(\\.[\\w\\~\\-]+)+",
                email.trim());
    }
    public static boolean checkStringZipcode(String zipcode){
        if(zipcode == null) return false;
        return Pattern.matches("^[0-9]{3}-[0-9]{3}$",zipcode);
    }
    /**
     * 널이거나 공백인지 검사
     * @param str 문자열
     * @return null 이거나 공백이면 true, else false
     */
    public static boolean isEmpty(String str) {
        if(str == null || "".equals(str)) return true;
        else return false;
    }
    public static boolean isEmpty(Object str) {
        if(str == null || "".equals(str.toString())) return true;
        else return false;
    }
    /**
     * 모든 태그 삭제
     * @param str
     * @return
     */
    public static String removeAllTag(String str) {
        String rs = str;
        if(rs == null) return rs;
        rs = str.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
        //rs = str.replaceAll("(?:<!--.*?(?:--.*?--\\s*)*.*?-->)|(?:<(?:[^>'\"]*|\".*?\"|'.*?')+>)","");
        return rs;
    }
    public static String removeTagOnly(String str , String tag){
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs,"$", "&#36;");
        Pattern p = Pattern.compile("(<" + tag + "[^><]*>)",Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(rs);
        StringBuffer myStringBuffer = new StringBuffer();
        myStringBuffer.setLength(0);
        while (m.find()) {
            m.appendReplacement(myStringBuffer, "");
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();

        p = Pattern.compile("(</" + tag + "[^><]*>)",Pattern.CASE_INSENSITIVE);

        m = p.matcher(rs);
        myStringBuffer.setLength(0);
        while (m.find()) {
            m.appendReplacement(myStringBuffer, "");
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();
        return rs;
    }
    /**
     * 해당 태그 및 태그 사이의 바디 컨텐츠까지  삭제
     * @param str
     * @param tag
     * @return
     */
    public static String removeTag(String str , String tag){
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs,"$", "&#36;");
        Pattern p = Pattern.compile("(<" + tag + "[^><]*>.*</" + tag + "[^><]*>)",Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(rs);
        StringBuffer myStringBuffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(myStringBuffer, "");
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();

        return rs;
    }

    /**
     * 날짜 형식 문자열의 형식 변경
     * @param sourceDate 날짜 형식의 문자열
     * @param sourcePattern 기존의 형식 패턴
     * @param targetPattern 변경할 형식 패턴
     * @return 변경된 날짜 형식 문자열
     */
    public static String safeDateFormatConvert(String sourceDate, String sourcePattern
            , String targetPattern) {
        SimpleDateFormat df = new SimpleDateFormat(sourcePattern);
        try {
            Date dt = df.parse(sourceDate);
            df.applyPattern(targetPattern);
            return df.format(dt);
        } catch(NullPointerException e) {
            return "";
        } catch(ParseException e) {
            return "";
        }
    }
    /**
     * 날짜 형식 문자열의 형식 변경
     * @param sourceDate
     * @param sourcePattern
     * @param sourceLocale
     * @param targetPattern
     * @param targetLocale
     * @return
     */
    public static String safeDateFormatConvert(String sourceDate, String sourcePattern, Locale sourceLocale
            , String targetPattern, Locale targetLocale) {
        SimpleDateFormat df = new SimpleDateFormat(sourcePattern, sourceLocale);
        SimpleDateFormat df2 = new SimpleDateFormat(targetPattern, targetLocale);
        try {
            Date dt = df.parse(sourceDate);
            return df2.format(dt);
        } catch(NullPointerException e) {
            return "";
        } catch(ParseException e) {
            return "";
        }
    }
    /**
     * 널 -> 공백
     * @param str 문자열
     * @return
     */
    public static String fixNull(Object str){
        if(str == null) return "";
        return str.toString();
    }
    /**
     * 널 -> 공백
     * @param str 문자열
     * @return
     */
    public static String fixNull(String str){
        if(str == null) return "";
        str = str.trim();
        return str;
    }
    /**
     * 널 -> 공백
     * @param str 문자열
     * @return
     */
    public static String fixNull(String str, String def){
        if(str == null) return def;
        return str;
    }

    /**
     * 널 -> 공백 & trim
     * @param checkValue
     * @return
     */
    public static String nullCheck(String checkValue){
        if (checkValue == null)
            return "";
        else
            return checkValue.trim();
    }

    /**
     * 오브젝트 널체크 + 기본값 리턴
     * @param str 오브젝트
     * @param 기본값
     * @return 문자열
     */
    public static String nullToValue(Object str, String value){
        String result = value;
        if(str != null){
            result = str.toString();
        }
        return result;
    }

    /**
     * 문자열 -> 숫자 (Integer)
     * @param str 소스 문자열
     * @param defaultValue 숫자 형식이 아닐 경우 기본 값
     * @return 변경된 숫자 (Integer)
     */
    public static int toInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch(NullPointerException e){
            return defaultValue;
        } catch(NumberFormatException e){
            return defaultValue;
        }
    }
    /**
     * 문자열 -> 숫자 (Integer)
     * @param str 소스 문자열
     * @return 변경된 숫자(Integer) : 오류 발생시 0을 리턴
     */
    public static int toInt(String str) {
        return toInt(str,0);
    }
    public static int toInt(Object str) {
        return str==null?0:toInt(str.toString(),0);
    }
    public static int toInt(Object str, int defaultValue) {
        return str==null?0:toInt(str.toString(),defaultValue);
    }
    public static double toDouble(String str) {
        return toDouble(str,0.0);
    }
    public static double toDouble(String str, double defaultValue ) {
        try {
            return Double.parseDouble(str);
        } catch(NullPointerException e){
            return defaultValue;
        } catch(NumberFormatException e){
            return defaultValue;
        }
    }


    public static String safeSubstring(String str, int start, int end){
        if(str == null) return "";
        try{ return str.substring(start,end); }catch(NullPointerException e){ return str; }
    }
    public static String safeSubstring(String str, int start){
        if(str == null) return "";
        try{ return str.substring(start); }catch(NullPointerException e){ return str; }
    }
    public static String getFormattedDate(Date dt, String pattern){
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(dt);
    }
    public static String getFormattedDate(Date dt, String pattern, Locale locale){
        SimpleDateFormat df = new SimpleDateFormat(pattern, locale);
        return df.format(dt);
    }
    public static String getCurrentDate(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(Calendar.getInstance().getTime());
    }
    public static String getCurrentDate(){
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(Calendar.getInstance().getTime());
    }
    public static String getFileSizeString(long fileSize){
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(1);
        if(fileSize < 1000 ) {
            return Long.toString(fileSize) + "B";
        } else if(fileSize < 1000000){
            return nf.format((double)fileSize/1000.0) + "KB";
        } else if(fileSize < 1000000000){
            return nf.format((double)fileSize/1000000.0) + "MB";
        } else {
            return nf.format((double)fileSize/1000000000.0) + "GB";
        }
    }
    public static String getHeadString(String title, int length ) {
        return strCut(title, null, length,0,true,true);
    }
    public static String strCut(String szText, String szKey, int nLength, int nPrev, boolean isNotag, boolean isAdddot){  // 문자열 자르기
        if(szText == null) return szText;
        String r_val = szText;
        int oF = 0, oL = 0, rF = 0, rL = 0;
        int nLengthPrev = 0;
        Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE);  // 태그제거 패턴

        if(isNotag) {r_val = p.matcher(r_val).replaceAll("");}  // 태그 제거
        r_val = r_val.replaceAll("&amp;", "&");
        r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", "");  // 공백제거

        try {
            byte[] bytes = r_val.getBytes("UTF-8");     // 바이트로 보관
            if(szKey != null && !szKey.equals("")) {
                nLengthPrev = (r_val.indexOf(szKey) == -1)? 0: r_val.indexOf(szKey);  // 일단 위치찾고
                nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length;  // 위치까지길이를 byte로 다시 구한다
                nLengthPrev = (nLengthPrev-nPrev >= 0)? nLengthPrev-nPrev:0;    // 좀 앞부분부터 가져오도록한다.
            }

            // x부터 y길이만큼 잘라낸다. 한글안깨지게.
            int j = 0;

            if(nLengthPrev > 0) while(j < bytes.length) {
                if((bytes[j] & 0x80) != 0) {
                    oF+=2; rF+=3; if(oF+2 > nLengthPrev) {break;} j+=3;
                } else {if(oF+1 > nLengthPrev) {break;} ++oF; ++rF; ++j;}
            }

            j = rF;

            while(j < bytes.length) {
                if((bytes[j] & 0x80) != 0) {
                    if(oL+2 > nLength) {break;} oL+=2; rL+=3; j+=3;
                } else {if(oL+1 > nLength) {break;} ++oL; ++rL; ++j;}
            }

            r_val = new String(bytes, rF, rL, "UTF-8");  // charset 옵션

            if(isAdddot && rF+rL+3 <= bytes.length) {r_val+="...";}  // ...을 붙일지말지 옵션
        } catch(UnsupportedEncodingException e){
            //logger.debug(e.getMessage());
        }

        return r_val;
    }

    public static String toJSON(Map map){
        StringBuffer result = new StringBuffer();
        result.append("{");
        Iterator i = map.keySet().iterator();
        boolean isFirst = true;
        while(i.hasNext()) {
            Object key = i.next();
            Object value = map.get(key);
            if(!isFirst) result.append(",");
            result.append("'" + key.toString() + "'"); result.append(":");
            if(value instanceof Map) {
                result.append(toJSON((Map)value));
            }
            else if(value instanceof List) {
                result.append(toJSON((List)value));
            }
            else if(value instanceof Boolean) {
                result.append(value.toString());
            }
            else if(value instanceof Number
            ) {
                result.append(value.toString());
            }
            else {
                result.append("'");
                result.append(value.toString().replaceAll("'", "\\'"));
                result.append("'");
            }
            isFirst = false;
        }
        result.append("}\n");
        return result.toString();
    }
    public static String toJSON(List list) {
        StringBuffer result = new StringBuffer();
        result.append("[");
        Iterator i = list.iterator();
        boolean isFirst = true;
        while(i.hasNext()) {
            Object value = i.next();
            if(!isFirst) result.append(",");
            if(value instanceof Map) {
                result.append(toJSON((Map)value));
            }
            else if(value instanceof List) {
                result.append(toJSON((List)value));
            }
            else if(value instanceof Boolean) {
                result.append(value.toString());
            }
            else if(value instanceof Number
            ) {
                result.append(value.toString());
            }
            else {
                result.append("'");
                result.append(value.toString().replaceAll("'", "\\'"));
                result.append("'");
            }
            isFirst = false;
        }
        result.append("]\n");
        return result.toString();
    }


    /*
     * 파리미터 필터링을 위한 함수, 상수
     */
    public static String filter(String str) {
        if(str == null) return null;
        str = str.replace("'", "");
        str = str.replace("--", "");
        str = str.replace(";", "");
        return str;
    }
    private static final String ALLOWED_TAG = "strong|label|table|tbody|thead|font|img|td|tr|th|br|span|div|ol|ul|li|dl|dd|dt|hr|h1|h2|h3|h4|h5|em|i|u|a|p|b";
    private static final String DENIED_WORD = "onclick|onabort|onactivate|onafterupdate|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforepaste|onbeforeupdate|onblur|oncontextmenu|oncontrolselect|oncopy|oncut|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror|onerrorupdate|onfilterchange|onfocus|onfocusin|onfocusout|onhelp|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmouseout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onpaste|onpropertychange|onreadystatechange|onresize|onresizeend|onresizestart|onselectstart";

    public static String filterFileNameParameter(String param) {
        String rs = param;
        rs = StringUtils.replace(rs,"..","");
        rs = StringUtils.replace(rs,"~","");
        return rs;
    }
    public static String filterTextParameter(String param){
        String rs = param;
        //rs = stripSpecialChar(rs);
        //rs = replaceXSSChar(rs);
        //rs = replaceDenyWord(rs);
        rs = escapeHTML(rs);
        return rs==null?"":rs;
    }
    public static String filterDefaultParameter(String param){
        String rs = param;
        rs = stripSpecialChar(rs);
        rs = replaceXSSChar(rs);
        rs = replaceDenyWord(rs);
        return rs==null?"":rs;
    }
    public static String filterDefaultParameter(String param, String def){
        String rs = param;
        rs = stripSpecialChar(rs);
        rs = replaceXSSChar(rs);
        rs = replaceDenyWord(rs);
        return rs==null?def:rs;
    }
    public static String filterHtmlParameter(String param) {
        String rs = param;
        rs = stripSpecialChar(rs);
        rs = replaceAllowTag(rs);
        rs = replaceDenyWord(rs);
        return rs==null?"":rs;
    }
    public static boolean ereg(String inString, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(inString);
        return m.find();
    }
    public static boolean eregi(String inString, String pattern) {
        Pattern p = Pattern.compile(pattern,Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(inString);
        return m.find();
    }
    public static String replace(String inString, String oldPattern, String newPattern)
    {
        if(inString == null)
            return null;
        if(oldPattern == null || newPattern == null)
            return inString;
        StringBuffer sbuf = new StringBuffer();
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        int patLen = oldPattern.length();
        for(; index >= 0; index = inString.indexOf(oldPattern, pos))
        {
            sbuf.append(inString.substring(pos, index));
            sbuf.append(newPattern);
            pos = index + patLen;
        }

        sbuf.append(inString.substring(pos));
        return sbuf.toString();
    }

    public static String stripSpecialChar(String str) {
        String rs = str;
        if(rs == null) return rs;
        // '  - 필터링 하지 않음  모두 PreparedStatement 를 사용하므로
        rs = StringUtils.replace(str, "'","&acute;");
        //rs = StringUtils.replace(str, ";","");
        rs = StringUtils.replace(rs, "--","─");

        return rs;
    }
    public static String replaceXSSChar(String str) {
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs, "<","&lt;");
        rs = StringUtils.replace(rs, ">","&gt;");
//		rs = StringUtils.replace(rs, "(","&#40;");
//		rs = StringUtils.replace(rs, ")","&#41;");
        return rs;
    }
    public static String unreplaceXSSChar(String str) {
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs, "&lt;","<");
        rs = StringUtils.replace(rs, "&gt;",">");
        rs = StringUtils.replace(rs, "&#40;","(");
        rs = StringUtils.replace(rs, "&#41;",")");
        return rs;
    }
    public static String escapeHTML(String aText) {
        if(aText==null) return aText;
        final StringBuffer result = new StringBuffer();
        final StringCharacterIterator iterator = new StringCharacterIterator(aText);
        char character =  iterator.current();
        while (character != CharacterIterator.DONE ){
            if (character == '<') {
                result.append("&lt;");
            }
            else if (character == '>') {
                result.append("&gt;");
            }
            else if (character == '&') {
                result.append("&amp;");
            }
            else if (character == '\"') {
                result.append("&quot;");
            }
            else if (character == '\t') {
                addCharEntity(9, result);
            }
            else if (character == '!') {
                addCharEntity(33, result);
            }
            else if (character == '#') {
                addCharEntity(35, result);
            }
            else if (character == '$') {
                addCharEntity(36, result);
            }
            else if (character == '%') {
                addCharEntity(37, result);
            }
            else if (character == '\'') {
                addCharEntity(39, result);
            }
            else if (character == '(') {
                addCharEntity(40, result);
            }
            else if (character == ')') {
                addCharEntity(41, result);
            }
            else if (character == '*') {
                addCharEntity(42, result);
            }
            else if (character == '+') {
                addCharEntity(43, result);
            }
            else if (character == ',') {
                addCharEntity(44, result);
            }
            else if (character == '-') {
                addCharEntity(45, result);
            }
            else if (character == '.') {
                addCharEntity(46, result);
            }
            else if (character == '/') {
                addCharEntity(47, result);
            }
            else if (character == ':') {
                addCharEntity(58, result);
            }
            else if (character == ';') {
                addCharEntity(59, result);
            }
            else if (character == '=') {
                addCharEntity(61, result);
            }
            else if (character == '?') {
                addCharEntity(63, result);
            }
            else if (character == '@') {
                addCharEntity(64, result);
            }
            else if (character == '[') {
                addCharEntity(91, result);
            }
            else if (character == '\\') {
                addCharEntity(92, result);
            }
            else if (character == ']') {
                addCharEntity(93, result);
            }
            else if (character == '^') {
                addCharEntity(94, result);
            }
            else if (character == '_') {
                addCharEntity(95, result);
            }
            else if (character == '`') {
                addCharEntity(96, result);
            }
            else if (character == '{') {
                addCharEntity(123, result);
            }
            else if (character == '|') {
                addCharEntity(124, result);
            }
            else if (character == '}') {
                addCharEntity(125, result);
            }
            else if (character == '~') {
                addCharEntity(126, result);
            }
            else {
                //the char is not a special one
                //add it to the result as is
                result.append(character);
            }
            character = iterator.next();
        }
        return result.toString();
    }
    private static void addCharEntity(int aIdx, StringBuffer aBuilder){
        String padding = "";
        if( aIdx <= 9 ){
            padding = "00";
        }
        else if( aIdx <= 99 ){
            padding = "0";
        }
        else {
            //no prefix
        }
        String number = padding + Integer.toString(aIdx);
        aBuilder.append("&#" + number + ";");
    }

    public static String replaceDenyWord(String str) {
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs,"$", "&#36;");
        Pattern p = Pattern.compile("(" + DENIED_WORD + ")",Pattern.CASE_INSENSITIVE);

        Matcher m = p.matcher(rs);
        StringBuffer myStringBuffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(myStringBuffer, m.group() + "_");
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();
        return rs;
    }
    public static String replaceAllowTag(String str) {
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs,"$", "&#36;");
        rs = StringUtils.replace(rs, "<","&lt;");

        Pattern p = Pattern.compile("&lt;(" + ALLOWED_TAG + ")",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(rs);
        StringBuffer myStringBuffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(myStringBuffer, "<" + m.group().toLowerCase().substring(4));
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();
        myStringBuffer.setLength(0);

        p = Pattern.compile("&lt;(" + ALLOWED_TAG + ")>",Pattern.CASE_INSENSITIVE);
        m = p.matcher(rs);
        myStringBuffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(myStringBuffer, "<" + m.group().toLowerCase().substring(4));
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();
        myStringBuffer.setLength(0);

        p = Pattern.compile("&lt;/(" + ALLOWED_TAG + ")",Pattern.CASE_INSENSITIVE);
        m = p.matcher(rs);
        myStringBuffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(myStringBuffer, "</" + m.group().toLowerCase().substring(5));
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();
        myStringBuffer.setLength(0);
        return rs;
    }
    public static String replaceTagToLower(String str){
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs,"$", "&#36;");
        Pattern p = Pattern.compile("<(/?)([^<>]*)?>",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(rs);
        StringBuffer myStringBuffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(myStringBuffer, m.group().toLowerCase());
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();
        return rs;
    }
    public static String fixBr(String str){
        String rs = str;
        if(rs == null) return rs;
        rs = StringUtils.replace(rs,"$", "&#36;");
        Pattern p = Pattern.compile("<BR[ \\t]*>",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(rs);
        StringBuffer myStringBuffer = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(myStringBuffer, "<br />");
        }
        m.appendTail(myStringBuffer);
        rs = myStringBuffer.toString();
        return rs;
    }
    public static final String UNKNOWN_MIME_TYPE="application/x-unknown-mime-type";

    public static String getMimeType(File file)	{
        FileTypeMap map = MimetypesFileTypeMap.getDefaultFileTypeMap();
        return map.getContentType(file);
    }

    public static String encodeUTF8(String str){
        try{
            return URLEncoder.encode(str, "UTF8");
        }catch(NullPointerException e){ return str;
        }catch(UnsupportedEncodingException e){ return str;
        }

    }

    public static String encode(String str, String charset){
        try{
            return URLEncoder.encode(str, charset);
        }catch(NullPointerException e){ return str;
        }catch(UnsupportedEncodingException e) {return str;
        }
    }
    public static int cuttingNumber(int num, int depth){
        return num - (num % (10 * depth));
    }
    public static int cuttingNumber(double num, int depth){
        return (int)num - ((int)num % (10 * depth));
    }
    public static int cuttingNumber(float num, int depth){
        return (int)num - ((int)num % (10 * depth));
    }

    /**
     * 인코딩 변경
     * @param source
     * @param sourceEncoding
     * @param targetEncoding
     * @return
     */
    public static String changeEncoding(String source , String sourceEncoding, String targetEncoding) {
        try {
            return new String(source.getBytes(sourceEncoding),targetEncoding);
        }catch(NullPointerException e) {
            return source;
        }catch(UnsupportedEncodingException e) {
            return source;
        }
    }

    //**********************************************************************************************************//
    // 지정된 위치에서 반올림 해주는 메서드
    //**********************************************************************************************************//
    public static double round(double dValue, int sca){
        try{
            return new java.math.BigDecimal(dValue).setScale(sca, java.math.BigDecimal.ROUND_HALF_EVEN).doubleValue();
        }catch(NullPointerException e){ return dValue;
        }catch(NumberFormatException e){ return dValue; }
    }

    //**********************************************************************************************************//
    // 지정된 위치에서 올림 해주는 메서드
    //**********************************************************************************************************//
    public static double roundUp(double dValue, int sca){
        try{
            return new java.math.BigDecimal(dValue).setScale(sca, java.math.BigDecimal.ROUND_UP).doubleValue();
        }catch(NullPointerException e){ return dValue;
        }catch(NumberFormatException e){ return dValue; }
    }

    //**********************************************************************************************************//
    // 지정된 위치에서 버림 해주는 메서드
    //**********************************************************************************************************//
    public static double roundDown(double dValue, int sca){
        try{
            return new java.math.BigDecimal(dValue).setScale(sca, java.math.BigDecimal.ROUND_DOWN).doubleValue();
        }catch(NullPointerException e){ return dValue;
        }catch(NumberFormatException e){ return dValue; }
    }

    /**
     * Replaces one string with another throughout a source string.
     *
     * @param in the source String
     * @param from the sub String to replace
     * @param to the sub String to replace with
     * @return a new String with all occurences of from replaced by to
     */
    public static String replaceInString(String in, String from, String to) {
        if( in == null || from == null || to == null) {
            return in;
        }

        StringBuffer newValue = new StringBuffer();
        char[] inChars = in.toCharArray();
        int inLen = inChars.length;
        char[] fromChars = from.toCharArray();
        int fromLen = fromChars.length;

        for (int i = 0; i < inLen; i++) {
            if (inChars[i] == fromChars[0] && (i + fromLen) <= inLen) {
                boolean isEqual = true;
                for (int j = 1; j < fromLen; j++) {
                    if (inChars[i + j] != fromChars[j]) {
                        isEqual = false;
                        break;
                    }
                }
                if (isEqual) {
                    newValue.append(to);
                    i += fromLen - 1;
                }
                else {
                    newValue.append(inChars[i]);
                }
            }
            else {
                newValue.append(inChars[i]);
            }
        }
        return newValue.toString();
    }

    /***
     * 업로드 파일 확장자 필터링
     * @param allowedExtensions
     * @param fileName
     * @return
     */
    public static boolean filterFileExtension(String allowedExtensions, String fileName){
        if(allowedExtensions!=null&&!allowedExtensions.equals("")){
            StringTokenizer tokenizer = new StringTokenizer(allowedExtensions,",");
            String fileExt = StringUtils.getFileExtension(fileName);
            while(tokenizer.hasMoreTokens()){
                String ext = tokenizer.nextToken();
                if(ext.equalsIgnoreCase(fileExt)){
                    return true;
                }
            }
        }else{
            return true;
        }
        return false;
    }
    /**
     * KST TO UTC
     * @param kst
     * @return
     */
    public static Date changeKstToUtc(Date kst){
        try{
            Date date = new Date(kst.getTime()-(9*60*60*1000));
            return date;
        }
        catch (NullPointerException e){
            return StringUtils.parseDate("0001/01/01 00:00:00", "yyyy/MM/dd HH:mm:ss");
        }
    }
    public static String toFormattedNumber(float value, int maxFractal, boolean isGroupingUsed) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(maxFractal);
        nf.setGroupingUsed(isGroupingUsed);
        return nf.format(value);
    }
    /**
     * 음수 일때는 +0.5
     * @param value
     * @return
     */
    public static int round(float value) {
        return (int)(value+0.5f);
    }
}
