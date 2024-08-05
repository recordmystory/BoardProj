package common.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * Parameter 유효성 검사 및 URL 관련 Util
 */
public class StringUtil {

	private static Map<String, Map<String, String>> urlMappings;

	static { // JSON 파일 로드 및 URL 매핑 정보 설정
		Configuration configUtil = Configuration.getInstance();
		configUtil.loadJsonFile();
		urlMappings = configUtil.getUrlMappings();
	}

	/**
	 * URL을 서비스 메소드명으로 변환
	 *
	 * @param url URL 액션 문자열
	 * @return 메소드명
	 */
	public static String getMethodNameFromAction(String url) {
		String[] parts = url.split("/");
		if (parts.length < 3) return "";

		String category = parts[1];
		String pageName = parts[2].split("\\.")[0];

		return pageName + convertUrl(category);
	}

	/**
	 * 문자열의 첫 글자를 대문자로 변환
	 *
	 * @param str 변환할 문자열
	 * @return 변환된 문자열
	 */
	public static String convertUrl(String str) {
		if (str == null || str.isEmpty()) return str;
		
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * URL 타입 반환
	 *
	 * @param url URL 문자열
	 * @return URL 타입 (forward, redirect, ajax)
	 */
	public static String getUrlType(String url) {
		Map<String, String> type = urlMappings.get(url);
		return type != null ? type.get("type") : "";
	}

	/**
	 * 뷰명 반환
	 *
	 * @param url URL 문자열
	 * @return 뷰명
	 */
	public static String getViewName(String url) {
		Map<String, String> view = urlMappings.get(url);
		return view != null ? view.get("viewName") : "";
	}
	
	/** 서비스 클래스명 반환
	 * 
	 * @param url URL 문자열
	 * @return 서비스명
	 */
	public static String getServiceName(String url) {
		/*// 클래스 이름을 직접 사용하는 경우
		String className = "BoardService";
		
		try {
		    // 클래스 직접 로드 (일반적으로 필요하지 않음)
		    Class<?> clazz = Class.forName(className);
		    System.out.println("로딩된 클래스 이름: " + clazz.getName());
		
		    // 인스턴스 생성
		    Object instance = clazz.getDeclaredConstructor().newInstance();
		    System.out.println("인스턴스: " + instance.toString());
		} catch (ClassNotFoundException e) {
		    e.printStackTrace();
		} catch (InstantiationException e) {
		    e.printStackTrace();
		} catch (IllegalAccessException e) {
		    e.printStackTrace();
		} catch (NoSuchMethodException e) {
		    e.printStackTrace();
		} catch (InvocationTargetException e) {
		    e.printStackTrace();
		}*/
	        
	    // ==================================================================================
		
		String[] parts = url.split("/");
		return parts[1] + ".model.service." + parts[1].substring(0, 1).toUpperCase() + parts[1].substring(1) + "Service";
	}
	
	/*	private static List<Class<?>> getClassesForPackage(String path, String packageName)
				throws IOException, ClassNotFoundException {
			File directory = new File(path);
			if (!directory.exists()) {
				throw new IOException("디렉토리가 존재하지 않습니다: " + path);
			}
	
			List<Class<?>> classes = new ArrayList<>();
			URL[] urls = { directory.toURI().toURL() };
			URLClassLoader classLoader = new URLClassLoader(urls);
	
			for (File file : directory.listFiles()) {
				if (file.getName().endsWith(".class")) {
					String className = packageName + '.' + file.getName().replace(".class", "");
					Class<?> clazz = classLoader.loadClass(className);
					classes.add(clazz);
				}
			}
	
			return classes;
		}*/
	

	/** 파라미터명과 파라미터 값을 유효성 검사 진행 후 map에 담아줌
	 * <p>파라미터가 숫자 및 문자가 아닐 경우엔 Excetion 발생 및 에러 페이지로 포워딩됨</p>
	 * 
	 * @param request HTTP 요청
	 * @return paramMap 파라미터가 담긴 map
	 */
	public static Map<String, String> setMapParameter(HttpServletRequest request){
		Map<String, String> paramMap = new HashMap<>();
		Enumeration<String> paramsNames = request.getParameterNames();

		while (paramsNames.hasMoreElements()) {
			String name = paramsNames.nextElement();
			String value = request.getParameter(name);
			
			if(value == null || value.trim().isEmpty()) throw new IllegalArgumentException("유효하지 않은 문자 : " + name); 
			
			paramMap.put(name, value); // map에는 파라미터 name과 그에 해당하는 값이 담겨있음
		}
		
		return paramMap;
	}
	
}
