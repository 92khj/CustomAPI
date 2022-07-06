package com.khj.customize.wizaiapi.other;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;


import java.nio.charset.StandardCharsets;
import java.util.*;

public class BeachInfo {

    @SneakyThrows
    public static Map<Integer,String[]> readCSV() {

        ClassPathResource resource = new ClassPathResource("data/2022_beach_info.csv");

        byte[] bdata = FileCopyUtils.copyToByteArray(resource.getInputStream());

        String csvStr = new String(bdata, StandardCharsets.UTF_8);

        Map<Integer,String[]> csvMap = new HashMap<>();
        String line = "";

        String[] lineArr = csvStr.split("\n");

        for (int i =0; i<lineArr.length;i++){
            csvMap.put(i+1,lineArr[i].split(","));
        }

        return csvMap;
    }
}
