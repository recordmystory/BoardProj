package com.jw.board.interfaces;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jw.board.model.vo.MyView;

public interface ControllerV2 {
	  MyView process(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
