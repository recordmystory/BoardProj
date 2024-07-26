package com.jw.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * XML 및 JSON 파일 설정 정보 로드 및 관리
 */
public class ConfigUtil {
	 private static final Logger logger = Logger.getLogger(ConfigUtil.class);
	    private static Properties prop = new Properties();
	    private static ConfigUtil instance;
	    
	    // json 파일 및 xml 파일 로드 상태 확인하는 플래그
	    private static boolean isXmlLoaded = false;
	    private static boolean isJsonLoaded = false;
	    
	    // URL 매핑 정보 저장 Map
	    private static Map<String, Map<String, String>> urlMappings = new HashMap<>();
	    
	    /**
	     * 클래스 로드 시 mapper 파일을 읽고 properties 객체에 저장됨
	     */
	    private ConfigUtil() { }

	    /**
	     * ConfigUtil 인스턴스 반환
	     * 
	     * @return ConfigUtil 인스턴스
	     */
	    public static synchronized ConfigUtil getInstance() {
	        if (instance == null) {
	            instance = new ConfigUtil();
	        }
	        return instance;
	    }
	    
	    /**
	     * xml 파일 로드 : XML 파일이 이미 로드된 경우 다시 로드하지 않음
	     */
	    public synchronized void loadXmlFile() {
	        if (!isXmlLoaded) {
	            try {
	                loadBoardMapperFile("db/mappers/board-mapper.xml");
	                isXmlLoaded = true;
	            } catch (IOException e) { // 파일이 존재하지 않거나 읽기에 실패 시 실행
	                logger.error("IOException 발생 ==> board-mapper.xml 파일 로드 실패: " + e.getMessage());
	                // 예외 발생 시 애플리케이션 종료
	                throw new RuntimeException("board-mapper.xml 파일을 로드할 수 없습니다. 애플리케이션을 종료합니다.", e);
	            }
	        }
	    }
	    
	    /**
	     * JSON 파일 로드 : JSON 파일이 이미 로드된 경우 다시 로드하지 않음
	     */
	    public synchronized void loadJsonFile() {
	        if (!isJsonLoaded) {
	            try {
	                loadBoardUrlFile("boardUrlMappings.json");
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
	     * mapper 파일 읽고 property에 저장
	     * 
	     * @param filePath
	     * @throws IOException
	     */
	    private void loadBoardMapperFile(String filePath) throws IOException {
	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
	        if (inputStream == null) {
	            logger.error("파일을 찾을 수 없습니다: " + filePath);
	            throw new IOException("파일을 찾을 수 없습니다: " + filePath);
	        }
	        try {
	            prop.loadFromXML(inputStream);
	        } catch (IOException e) {
	            logger.error("IOException 발생 ==> XML 파일 로드 실패: " + e.getMessage());
	            throw e;
	        } finally {
	            if (inputStream != null) {
	                inputStream.close();
	            }
	        }
	    }

	    /**
	     * JSON 파일을 읽고 URL 매핑값을 urlMappings라는 map에 저장 
	     * 
	     * @param filePath
	     * @throws IOException
	     * @throws JSONException
	     */
	    private void loadBoardUrlFile(String filePath) throws IOException, JSONException {
	        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
	        if (inputStream == null) {
	            logger.error("파일을 찾을 수 없습니다: " + filePath);
	            throw new IOException("파일을 찾을 수 없습니다: " + filePath);
	        }
	        StringBuilder jsonContent = new StringBuilder();
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
	            String line;
	            while ((line = reader.readLine()) != null) {
	                jsonContent.append(line);
	            }
	        }

	        JSONObject jsonObject = new JSONObject(jsonContent.toString());
	        JSONObject urls = jsonObject.getJSONObject("urls");

	        for (String key : urls.keySet()) {
	            JSONObject urlObject = urls.getJSONObject(key);
	            Map<String, String> mappingInfo = new HashMap<>();
	            mappingInfo.put("viewName", urlObject.optString("viewName")); // ajax일 경우 viewName 없음
	            mappingInfo.put("type", urlObject.getString("type"));
	            urlMappings.put(key, mappingInfo);
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
    
    
    /**
     * ConfigUtil 인스턴스 생성 및 관리
     */
    /*private static class Holder { // 한 번만 실행됨
	    private static final ConfigUtil INSTANCE = new ConfigUtil();
	}*/
}
