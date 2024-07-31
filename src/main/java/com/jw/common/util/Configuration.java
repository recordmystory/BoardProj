package com.jw.common.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONException;

/**
 * XML 및 JSON 파일 설정 정보 로드 및 관리
 */
public class Configuration {
	private static Logger logger = Logger.getLogger(Configuration.class);
	private static Properties prop = new Properties();
	private static Configuration instance;

	// json 파일 및 xml 파일 로드 상태 확인하는 플래그
	private static boolean isXmlLoaded = false;
	private static boolean isJsonLoaded = false;

	// URL 매핑 정보 저장 Map
	private static Map<String, Map<String, String>> urlMappings = new HashMap<>();

	/**
	 * 클래스 로드 시 mapper 파일을 읽고 properties 객체에 저장됨
	 */
	private Configuration() {
	}

	/**
	 * ConfigUtil 인스턴스 반환
	 * 
	 * @return ConfigUtil 인스턴스
	 */
	public static Configuration getInstance() {
		if (instance == null) {
			instance = new Configuration();
		}
		return instance;
	}

	/**
	 * xml 파일 로드 : XML 파일이 이미 로드된 경우 다시 로드하지 않음
	 */
	public void loadXmlFile() {
		if (!isXmlLoaded) {
			try {
				prop = FileLoaderUtil.loadBoardMapperFile("db/mappers/boardMapper.xml");
				isXmlLoaded = true;
			} catch (IOException e) { // 파일이 존재하지 않거나 읽기에 실패 시 실행
				logger.error("IOException 발생 ==> boardMapper.xml 파일 로드 실패: " + e.getMessage());
				// 예외 발생 시 애플리케이션 종료
				throw new RuntimeException("boardMapper.xml 파일을 로드할 수 없습니다. 애플리케이션을 종료합니다.", e);
			}
		}
	}

	/**
	 * JSON 파일 로드 : JSON 파일이 이미 로드된 경우 다시 로드하지 않음
	 */
	public void loadJsonFile() {
		if (!isJsonLoaded) {
			try {
				urlMappings = FileLoaderUtil.loadBoardUrlFile("boardUrlMappings.json");
				isJsonLoaded = true;
			} catch (IOException e) { // 파일이 존재하지 않거나 읽기에 실패 시 실행
				logger.error("IOException 발생 ==> boardUrlMappings.json 파일 로드 실패: " + e.getMessage());
				// 예외 발생 시 애플리케이션 종료
				throw new RuntimeException("boardUrlMappings.json 파일을 로드할 수 없습니다. 애플리케이션을 종료합니다.", e);
			} catch (JSONException e) {
				logger.error("JSONException 발생 ==> boardUrlMappings.json 파일 파싱 실패: " + e.getMessage());
				// 예외 발생 시 애플리케이션 종료
				throw new RuntimeException("boardUrlMappings.json 파일을 파싱할 수 없습니다. 애플리케이션을 종료합니다.", e);
			}
		}
	}
	
	/**
	 * URL 매핑 정보 반환
	 * 
	 * @return URL 매핑 정보 Map
	 */
	public Map<String, Map<String, String>> getUrlMappings() {
		return urlMappings;
	}

	/**
	 * property 객체 반환
	 * 
	 * @return prop
	 */
	public Properties getProperties() {
		return prop;
	}

}
