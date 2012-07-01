package com.se560.project;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Se560_projectServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private final Se560_requestHandler rh = new Se560_requestHandler();

	public void doGet (HttpServletRequest request, HttpServletResponse response) 
		throws IOException
	{
		doPost(request, response);
	}

	public void doPost (HttpServletRequest request, HttpServletResponse response) 
		throws IOException
	{
		rh.requestDispatcher(request, response, getServletContext());
	}
		
}
