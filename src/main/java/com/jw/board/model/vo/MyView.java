package com.jw.board.model.vo;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyView {
	private String viewPath;
	
	public MyView(String viewPath) {
		this.viewPath = viewPath;
	}
	
	  public String getViewPath() {
	        return viewPath;
	    }
	
	public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
	        try {
	            dispatcher.forward(request, response);
	        } catch (Exception e) {
	            throw new IOException(e);
	        }
	}
}
