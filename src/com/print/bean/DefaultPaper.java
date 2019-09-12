package com.print.bean;

import java.awt.print.Paper;

public class DefaultPaper {
	public static final int width = 136;//实际打印范围。单位px。
	public static   int height = 130;
	public static final int offset_top = 16;
	public static final int offset_width = 15;
	 
	public static Paper defaultPaper() {
		 Paper paper = new Paper();
		 paper.setSize(width+offset_width*2, height); 
		 paper.setImageableArea(0, 0,width+offset_width*2, height );  
		return paper ;
	}  
}
