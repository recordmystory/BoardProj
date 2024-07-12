package com.jw.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jw.board.interfaces.ControllerV2;
import com.jw.board.model.vo.MyView;

public class BoardRegistControllerV2 implements ControllerV2 {

	@Override
	public MyView process(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new MyView("/WEB-INF/views/board/registForm.jsp");
	}
}
