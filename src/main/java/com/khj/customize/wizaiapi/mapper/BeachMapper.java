package com.khj.customize.wizaiapi.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

public interface BeachMapper {

    List<Map<String,Object>> findByFargoVOL(@Param("date") String date, @Param("beach_num") int beach_num);

    List<Map<String,Object>>  findByWaterVOL(@Param("date") String date, @Param("beach_num") int beach_num);

    List<Map<String,Object>> findBySunVOL(@Param("date") String date,@Param("beach_num") int beach_num);

    List<Map<String,Object>> findByTideVOL(@Param("date") String date,@Param("beach_num")int beach_num);

    List<Map<String,Object>> findTestVOL(@Param("beach_num")int beach_num);

}
