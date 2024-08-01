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

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * 파일 로드 Util
 */
public class FileLoaderUtil {
	private static Logger logger = Logger.getLogger(FileLoaderUtil.class);
	
	 /**
     * mapper 파일 읽고 property에 저장
     * 
     * @param filePath : 파일 경로
     * @return prop : property 객체
     * @throws IOException : 파일이 존재하지 않을 때, 파일 읽기 중 오류가 발생했을 때, 스트림을 닫는 중 오류가 발생했을 때 
     */
	public static Properties loadBoardMapperFile(String filePath) throws IOException {
    	Properties prop = new Properties();
        InputStream inputStream = FileLoaderUtil.class.getClassLoader().getResourceAsStream(filePath);
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
            if (inputStream != null) inputStream.close();
        }
        return prop;
    }

    /**
     * JSON 파일을 읽고 각 URL마다 존재하는 viewName, type, serviceName 속성을 urlMappings map에 저장 
     * 
     * @param filePath : 파일 경로
     * @throws IOException : 파일이 존재하지 않을 때, 파일 읽기 중 오류가 발생했을 때, 스트림을 닫는 중 오류가 발생했을 때 
     * @throws JSONException : JSON 형식이 잘못됐거나 JSON 파싱 중 오류가 발생했을 때
     */
	public static Map<String, Map<String, String>> loadBoardUrlFile(String filePath) throws IOException, JSONException {
		Map<String, Map<String, String>> urlMappings = new HashMap<>();
		
		try (InputStream inputStream = FileLoaderUtil.class.getClassLoader().getResourceAsStream(filePath)) {
			if (inputStream == null) {
				logger.error("파일을 찾을 수 없습니다: " + filePath);
				throw new IOException("파일을 찾을 수 없습니다: " + filePath);
			}

			try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
				JsonObject urls = new Gson().fromJson(reader, JsonObject.class).getAsJsonObject("urls");

				urls.entrySet().forEach(entry -> {
					JsonObject urlObject = entry.getValue().getAsJsonObject();
					Map<String, String> mappingInfo = new HashMap<>();
					mappingInfo.put("viewName", urlObject.has("viewName") ? urlObject.get("viewName").getAsString() : "");
					mappingInfo.put("type", urlObject.get("type").getAsString());
					mappingInfo.put("serviceName", urlObject.get("serviceName").getAsString());
					urlMappings.put(entry.getKey(), mappingInfo);
				});
			}
		}

		return urlMappings;
	}
}
