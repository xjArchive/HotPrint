package com.print.web;

import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.print.bean.PrintItem;
import com.print.facade.PrintServiceFacade;
import com.print.task.PrintTaskManager;

@WebServlet("/doit.action")
public class PrintServlet 
extends HttpServlet{
	public static String json_body_charset = "UTF-8";
	
	@Override
	protected void doPost(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
		 rq.getInputStream();
		 byte  []bs  = new byte[500];
		 ByteArrayOutputStream bout = new ByteArrayOutputStream(1024);
		 InputStream in = rq.getInputStream();
		 while(true) {
			 int num = in.read(bs);
			 if(num ==-1 || num ==0)break;
			 bout.write(bs, 0, num);
		 }
		 String json = new String(bout.toByteArray(),json_body_charset);
		 JSONArray jsr = JSON.parseArray( json );
		 List<PrintItem> printlist = new ArrayList<PrintItem>();
		 for(Object item : jsr) {
			 JSONObject it = (JSONObject) item;
			 PrintItem pritem = JSONObject.toJavaObject(it, PrintItem.class);
			 printlist.add(pritem);
		 }
		 if( printlist.isEmpty() == false) {
			 //丢进阻塞队列中。
			 PrintTaskManager.addPrintTask(printlist);
		 } 
		 
		
	}
	
	@Override
	protected void doOptions(HttpServletRequest rq, HttpServletResponse rp) throws ServletException, IOException {
		  
	}
}
