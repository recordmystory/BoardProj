package com.jw.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * mapper 파일 로드 및 Proprties 객체 관리
 */
public class ConfigUtil {
    private static final Logger logger = Logger.getLogger(ConfigUtil.class);
	private static Properties prop = new Properties();

	/**
	 * 클래스 로드 시 mapper 파일을 읽고 properties 객체에 저장됨
	 */
	private ConfigUtil() {
        try {
            String filePath = getClass().getResource("/db/mappers/board-mapper.xml").getPath();
            if (filePath == null) {
                throw new IOException("filePath == null");
            }
            prop.loadFromXML(new FileInputStream(filePath));
        } catch (IOException e) { // 파일이 존재하지 않거나 읽기에 실패 시 실행
            logger.error("IOException 발생 ==> board-mapper.xml 파일 로드 실패: " + e.getMessage());
            // 예외 발생 시 애플리케이션 종료 
//            throw new RuntimeException("board-mapper.xml 파일을 로드할 수 없습니다. 애플리케이션을 종료합니다.", e);
        }
    }

    /**
     * ConfigUtil 인스턴스 생성 및 관리
     */
    private static class Holder { // 한 번만 실행됨
        private static final ConfigUtil INSTANCE = new ConfigUtil();
    }

    /** 이 메서드 호출 시 Holder 클래스 로드(호출) 및 ConfigUtil 인스턴스 생성됨
     * 
     * @return ConfigUtil 인스턴스
     */
    public static ConfigUtil getInstance() { 
        return Holder.INSTANCE;
    }

    /** property 객체 반환
     * 
     * @return prop
     */
    public Properties getProperties() {
        return prop;
    }
}
