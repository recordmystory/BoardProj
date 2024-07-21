package com.jw.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jw.board.model.vo.UrlMapping;

/**
 * mapper 파일 로드 및 Proprties 객체 관리
 */
public class ConfigUtil {
    private static final Logger logger = Logger.getLogger(ConfigUtil.class);
	private static Properties prop = new Properties();
	private static ConfigUtil instance;
	private static Map<String, UrlMapping> urlMappings = new HashMap<>();
    private static List<String> urlList;


	/**
	 * 클래스 로드 시 mapper 파일을 읽고 properties 객체에 저장됨
	 */
	private ConfigUtil() {
		loadUrlMappings();
		loadProperties();
		
		/* try {
		    String filePath = getClass().getResource("/db/mappers/board-mapper.xml").getPath();
		    if (filePath == null) {
		        throw new IOException("filePath == null");
		    }
		    prop.loadFromXML(new FileInputStream(filePath));
		} catch (IOException e) { // 파일이 존재하지 않거나 읽기에 실패 시 실행
		    logger.error("IOException 발생 ==> board-mapper.xml 파일 로드 실패: " + e.getMessage());
		    throw new RuntimeException("board-mapper.xml 파일을 로드할 수 없습니다. 애플리케이션 종료.", e);
		} */
    }
	
	/**
	 * JSON 파일에서 URL 매핑 정보 로드
	 */
	public void loadUrlMappings() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String filePath = getClass().getResource("/resources/boardUrlMappings.json").getPath();
            if (filePath == null) {
                throw new IOException("filePath == null");
            }
            UrlMappings mappings = mapper.readValue(new FileInputStream(filePath), UrlMappings.class);
            urlMappings = mappings.getUrls().stream().collect(Collectors.toMap(UrlMapping::getUrl, mapping -> mapping));
            urlList = mappings.getUrls().stream().map(UrlMapping::getUrl).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("IOException 발생 ==> boardUrlMappings.json 파일 로드 실패: " + e.getMessage());
            throw new RuntimeException("boardUrlMappings.json 파일을 로드할 수 없습니다. 애플리케이션 종료.", e);
        }
    }
	
	/**
	 * mapper 파일 로드
	 */
	private void loadProperties() {
        try {
            String filePath = getClass().getResource("/db/mappers/board-mapper.xml").getPath();
            if (filePath == null) {
                throw new IOException("filePath == null");
            }
            prop.loadFromXML(new FileInputStream(filePath));
        } catch (IOException e) { // 파일이 존재하지 않거나 읽기에 실패 시 실행
            logger.error("IOException 발생 ==> board-mapper.xml 파일 로드 실패: " + e.getMessage());
            throw new RuntimeException("board-mapper.xml 파일을 로드할 수 없습니다. 애플리케이션 종료.", e);
        }
    }
	
	/** 인스턴스 제공
	 * 
	 * @return instance
	 */
	public static synchronized ConfigUtil getInstance() {
		if (instance == null) // instance == null이면 인스턴스가 생성되지 않았음을 의미
			instance = new ConfigUtil();
		
		return instance;
	}
	
	/** property 객체 반환
	 * 
	 * @return prop
	 */
	public Properties getProperties() {
		return prop;
	}
	
	/** URL 매핑 정보 반환
	 *
	 * @return urlMappings
	 */
	public Map<String, UrlMapping> getUrlMappings() {
		return urlMappings;
	}

	/** URL 리스트  반환
	 * 
	 * @return url
	 */
	public List<String> getUrlList() {
		return urlList;
	}
	
	public static class UrlMappings {
        private List<UrlMapping> urls;

        // getters and setters
        public List<UrlMapping> getUrls() {
            return urls;
        }

        public void setUrls(List<UrlMapping> urls) {
            this.urls = urls;
        }
    }
	
	/**
     * ConfigUtil 인스턴스 생성 및 관리
     */
//    private static class Holder { // 한 번만 실행됨
//        private static final ConfigUtil INSTANCE = new ConfigUtil();
//    }
    
    /** 이 메서드 호출 시 Holder 클래스 로드(호출) 및 ConfigUtil 인스턴스 생성됨
     * 
     * @return ConfigUtil 인스턴스
     */
	/* public static ConfigUtil getInstance() { 
	    return Holder.INSTANCE;
	}*/

}
