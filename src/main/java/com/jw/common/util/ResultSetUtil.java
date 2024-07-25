package com.jw.common.util;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class ResultSetUtil {
	private static final Map<Class<?>, BiConsumer<Object, ResultSet>> typeSetters = new HashMap<>();

//	static {
//		typeSetters.put(int.class, (obj, rset) -> setField(obj, rset, Integer.TYPE, ResultSet::getInt));
//		
//	}

}
