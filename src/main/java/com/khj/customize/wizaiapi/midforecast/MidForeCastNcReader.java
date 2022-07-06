package com.khj.customize.wizaiapi.midforecast;

import com.khj.customize.openapi.ForecastVo;
import com.khj.customize.wizaiapi.vo.MidForeCastVo;
import org.springframework.beans.factory.annotation.Value;
import ucar.ma2.Array;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Dimension;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.units.DateUnit;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//중기예보 NC 파일읽기
public class MidForeCastNcReader {


    public static String filePathName = "\\DFS_MEDM_GRD_BEST_MERG_HR03_"; //"\\DATA\\BIGDATA\\DFS_MEDM_GRD_BEST_MERG_HR03_";//"/DATA/DFS/MEDM/MERG/YYYYMM/DD";
    public static String LON = "longitude";
    public static String LAT = "latitude";
    public static String X = "x";
    public static String Y = "y";
    public static String fileString;

    public static ArrayList<ForecastVo> getMidVO(String filePath,String category , String base_date,String base_time,int searchY, int searchX) {


        fileString = filePath+ "\\"  + base_date.substring(0,6) + "\\"+ base_date.substring(6,8) + filePathName +category +"."+ base_date + base_time + ".nc";

        NetcdfFile dataFile = null;
        ArrayList<ForecastVo> voArrayList = new ArrayList<>();


        try {

            NetcdfDataset netcdfDatasetInputFile = NetcdfDataset.openDataset(fileString);

            // Retrieve the variable named "data"
            Variable variable = netcdfDatasetInputFile.findVariable(category);

            Dimension timeDimension = variable.getDimension(0);
            Variable timeVariable = netcdfDatasetInputFile.findVariable(timeDimension.getName());
            Array timeArray = timeVariable.read();

            double[] timeValues = (double[]) timeArray.copyToNDJavaArray();
            double[] times = (double[]) timeArray.get1DJavaArray(Double.class);

            String[] convertedTimeArray = readTimes(timeVariable);
            /*
            //Reading latitude variable
            Variable latitudeVariable = netcdfDatasetInputFile.findVariable(LAT);
            Array latitudeArray = latitudeVariable.read();
            double[][] latitudeValues = (double[][]) latitudeArray.copyToNDJavaArray();

            //Reading longitude variable
            Variable longitudeVariable = netcdfDatasetInputFile.findVariable(LON);
            Array longitudeArray = longitudeVariable.read();
            double[][] longitudeValues = (double[][]) longitudeArray.copyToNDJavaArray();

            //Reading x variable
            Variable xVariable = netcdfDatasetInputFile.findVariable(X);
            Array xArray = xVariable.read();
            double[] xValues = (double[]) xArray.copyTo1DJavaArray();

            //Reading y variable
            Variable yVariable = netcdfDatasetInputFile.findVariable(Y);
            Array yArray = yVariable.read();
            double[] yValues = (double[]) yArray.copyTo1DJavaArray();
            */

            //검색 범위;
            //String sectionSpec = "0:"+(times.length - 1)+":1,:,:";
            String sectionSpec = "0:"+(times.length -1)+":1,"+searchY+":"+searchY+","+searchX+":"+searchX;

            //Reading data variable
            Array data = variable.read();

            data = variable.read(sectionSpec);



            double[][][] datav = (double[][][]) data.copyToNDJavaArray();

            String[] foreValues = new String[times.length];

            for (int searchLength = 0 ; searchLength < times.length; searchLength++) {
                String opendate = getDateChange(convertedTimeArray[searchLength]);
                String baseDate = getDateChange2(base_date+base_time);
                String fcstValue = String.format("%.1f", datav[searchLength][0][0]);

                if (category.equals("RN3")){
                    fcstValue = fcstValueChnRN3(datav[searchLength][0][0]);
                }else if(category.equals("SN3")){
                    fcstValue = fcstValueChnSN3(datav[searchLength][0][0]);
                }else if(category.equals("SKY")){
                    fcstValue = String.format("%.0f", datav[searchLength][0][0]);
                }else if(category.equals("POP")){
                    fcstValue = String.format("%.0f", datav[searchLength][0][0]);
                }else if(category.equals("PTY")){
                    fcstValue = String.format("%.0f", datav[searchLength][0][0]);
                }else if(category.equals("REH")){
                    fcstValue = String.format("%.0f", datav[searchLength][0][0]);
                }

                ForecastVo forecastVo = new ForecastVo(baseDate.substring(0,8),baseDate.substring(8),category,opendate.substring(0,8),opendate.substring(8,12),fcstValue,searchX,searchY);
                voArrayList.add(forecastVo);
                //foreValues[searchLength] = String.format("%.1f", datav[searchLength][0][0]);
                //voArrayList.add(forecastVo);
            }

        } catch (IOException | InvalidRangeException e) {
            e.printStackTrace();

        }


        return voArrayList;
    };


    //RN3 혹은 SN3일때 범주값으로 데이터 변경
    private static String fcstValueChnRN3(Double fcstValue) {
        String result;

        if (fcstValue == 0.0) result = "강수없음";
        else if(fcstValue >= 0 && fcstValue < 1) result = "1.0mm 미만";
        else if (fcstValue >= 1 && fcstValue < 5) result = "1~4mm";
        else if (fcstValue >= 5 && fcstValue < 10) result = "5.0~10.0mm";
        else if (fcstValue >= 10 && fcstValue < 20) result = "10.0~20.0mm";
        else if (fcstValue >= 20 && fcstValue < 40) result = "20.0~40.0mm";
        else if (fcstValue >= 40 && fcstValue < 70) result = "40.0~69.0mm";
        else if (fcstValue >= 70) result = "70.0mm 이상";
        else result = "강수없음";
        return result;
    };

    private static String fcstValueChnSN3(Double fcstValue) {
        String result;

        if (fcstValue == 0.0) result = "적설없음";
        else if(fcstValue >= 0 && fcstValue < 1) result = "1cm 미만";
        else if (fcstValue >= 1 && fcstValue < 5) result = "1~4cm";
        else if (fcstValue >= 5 && fcstValue < 10) result = "5~9cm";
        else if (fcstValue >= 10 && fcstValue < 20) result = "10~19cm";
        else if (fcstValue >= 20) result = "20cm 이상";
        else result = "적설없음";

        return result;
    };

    //https://publicwiki.deltares.nl/display/FEWSDOC/Reading+and+writing+netcdf+gridded+data+with+Java
    private static String[] readTimes(Variable timeVariable) throws IOException {
        if (timeVariable == null) throw new RuntimeException("Time variable not present");

        //read times.
        Array timeArray = timeVariable.read();
        double[] times = (double[]) timeArray.get1DJavaArray(Double.class);

        //convert times.
        String[] convertedTimes = new String[times.length];
        DateUnit dateUnit = readTimeUnit(timeVariable);
        if (dateUnit == null) {
            throw new IOException("Invalid date time unit string has been coded in the file: '" + timeVariable.getUnitsString() +
                    "'.  Unit string should be for example: 'seconds since 2012-01-30 00:00:00'");

        }
        for (int i = 0; i < times.length; i++) {
            Date date = dateUnit.makeDate(times[i]);
            //System.out.println("date"+date);
            if (date != null) {
                convertedTimes[i] = date.toString();

            } else {//if date is null.
                convertedTimes[i] = "0";
            }
        }
        return convertedTimes;
    }

    public static DateUnit readTimeUnit(Variable timeVariable) {
        try {
            String unitString = timeVariable.getUnitsString();

            //if present, replace . by : in the timeZone specification in the unitString,
            //e.g. change "days since 2011-09-19 06:0:0.0 0.00" to "days since 2011-09-19 06:0:0.0 0:00".
            //This is a workaround implemented for FEWS-6544.
            String[] parts = unitString.split("\\s+");
            if (parts.length > 0 && parts[parts.length - 1].matches(".?\\d{1,2}\\.\\d{2}")) {
                //if a timeZone specification is present, then it is always the last part, see
                //http://cf-pcmdi.llnl.gov/documents/cf-conventions/1.5/cf-conventions.html#time-coordinate
                parts[parts.length - 1] = parts[parts.length - 1].replaceFirst("\\.", ":");
                StringBuilder buffer = new StringBuilder(parts[0]);
                for (int n = 1; n < parts.length; n++) {
                    buffer.append(' ').append(parts[n]);
                }
                unitString = buffer.toString();
            }

            return new DateUnit(unitString);
        } catch (Exception e) {
            //if the given variable does not have a unit of time.
            return null;
        }
    }

    public static String getDateChange(String date){
        String strDate = "";


        SimpleDateFormat recvSimpleFormat = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        SimpleDateFormat transSimpleFormat = new SimpleDateFormat("yyyyMMddHHmm",Locale.ENGLISH);

        try {
            Date formatdate = recvSimpleFormat.parse(date);
            //strDate = transSimpleFormat.format(formatdate);
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatdate);
            //cal.add(Calendar.HOUR,+13);
            Date formatCal = cal.getTime();
            strDate = transSimpleFormat.format(formatCal);

        }catch (ParseException e) {
            e.printStackTrace();
        }

        return strDate;
    }
    public static String getDateChange2(String date){


        String strDate = "";
        SimpleDateFormat transSimpleFormat = new SimpleDateFormat("yyyyMMddHHmm",Locale.ENGLISH);

        try {
            Date formatDate = transSimpleFormat.parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(formatDate);
            cal.add(Calendar.HOUR,+9);
            Date formatCal = cal.getTime();
            strDate = transSimpleFormat.format(formatCal);

        }catch (ParseException e) {
            e.printStackTrace();
        }
        return strDate;

    };
}
