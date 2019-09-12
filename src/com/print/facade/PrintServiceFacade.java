package com.print.facade;

import java.util.List;
import java.util.Map;

import com.print.bean.DefaultPaper;
import com.print.bean.PrintItem;
import com.print.service.PrintService;

public class PrintServiceFacade {
	public static void print( List<PrintItem> printlist) {
		PrintService.print(printlist);
	}
	
	public static void setInitParameter(Map<String,String> param) {
		 if(param!= null && !param.isEmpty()) {
			String height = param.get("printer.maxHeight");
			if(height == null || "".equals( height.trim())) {
				height = "300";
			}
			DefaultPaper.height = Integer.parseInt( height );
		 }
	}
}
