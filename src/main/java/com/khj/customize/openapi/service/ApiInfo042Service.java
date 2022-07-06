package com.khj.customize.openapi.service;


import com.khj.customize.openapi.forecast.DFSPointShrnData;
import com.khj.customize.openapi.forecast.DFSPointVsrtData;
import com.khj.customize.openapi.service.vo.ApiInfo042Vo;

public interface ApiInfo042Service {
	/**
	 * (신)동네예보정보조회서비스
	 * @param apiInfo010UserVo
	 * @return
	 * @throws Exception
	 */


	//2. 초단기예보조회
	DFSPointVsrtData getForecastTimeData(ApiInfo042Vo apiInfo042Vo) throws Exception;

	//3. 동네예보조회
	DFSPointShrnData getForecastSpaceData(ApiInfo042Vo apiInfo042Vo) throws Exception;
}
