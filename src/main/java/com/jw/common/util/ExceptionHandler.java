package com.jw.common.util;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ExceptionHandler {
	private static final Logger logger = Logger.getLogger(ExceptionHandler.class);

    public static void processException(HttpServletRequest request, HttpServletResponse response, String errorMessage,
                                       Exception e) {
        logger.error(errorMessage, e);
        try {
            if (!response.isCommitted()) {
                request.setAttribute("alertMsg", errorMessage);
                request.getRequestDispatcher("/views/board/errorPage.jsp").forward(request, response);
            }
        } catch (IOException | ServletException ex) {
            logger.error("오류 페이지 이동 실패: " + ex.getMessage(), ex);
        }
    }
}
