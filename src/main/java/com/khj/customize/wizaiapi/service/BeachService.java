package com.khj.customize.wizaiapi.service;

import com.khj.customize.wizaiapi.mapper.BeachMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Repository
public class BeachService {

    @Autowired
    private SqlSession sqlSession;


    public List<Map<String,Object>> getFargoVOL(String date, int beach_num) {
        BeachMapper mapper = sqlSession.getMapper(BeachMapper.class);
        return mapper.findByFargoVOL(date,beach_num);

    }

    public List<Map<String,Object>> getTideVOL(String date, int beach_num) {
        BeachMapper mapper = sqlSession.getMapper(BeachMapper.class);
        return mapper.findByTideVOL(date,beach_num);
    }

    public List<Map<String,Object>> getSunVOL(String date, int beach_num) {
        BeachMapper mapper = sqlSession.getMapper(BeachMapper.class);
        return mapper.findBySunVOL(date,beach_num);
    }
    public List<Map<String,Object>> getWaterVOL(String date, int beach_num) {
        BeachMapper mapper = sqlSession.getMapper(BeachMapper.class);
        return mapper.findByWaterVOL(date,beach_num);
    }

    public List<Map<String,Object>> getTest(int beach_num) {
        BeachMapper mapper = sqlSession.getMapper(BeachMapper.class);
        return mapper.findTestVOL(beach_num);
    };
}

