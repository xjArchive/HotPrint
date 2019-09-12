package com.print.test;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.print.bean.DefaultPaper;
import com.print.bean.PrintItem;

public class PrintService2 {
	protected static final Logger log = LogManager.getLogger();
	public static void printItem(Graphics g, List<PrintItem> printlist) {
		 int y = DefaultPaper.offset_top;
		 for(PrintItem it :printlist) {
			 y = drawOneLineString(g, it, y);
			 if(y>= DefaultPaper.height) {
				 log.error("内容实际高度大于设定高度。停止输出。如需修改。请设置conf.ini再重启。");
				 break;
			 }
		 }
	}
	/**
	 * 打印一行字符串。一行字符串可能在纸张上会出现几行。
	 * @param g
	 * @param it
	 * @param y
	 * @return
	 */
	private static int drawOneLineString(Graphics g,PrintItem it,int y) {
		char []chs =it.getText().toCharArray();
		g.setFont(new Font(it.getFontfamily(), Font.PLAIN, it.getSize()));
		int x = 0;
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<chs.length;i++) {
			char ch  = chs[i];
			int chw = getCharacterWidth(ch,it.getSize());
			//打印之前，先判断需不需要换行。
			if(ch =='\n') {//换行。
				if(sb.length()>0) {
					if("left".equalsIgnoreCase(it.getAlign())) {
						g.drawString(sb.toString(),  DefaultPaper.offset_width  ,  y);
					}else if("center".equalsIgnoreCase( it.getAlign() )) {
						g.drawString(sb.toString(),  (DefaultPaper.width - x)/2 + DefaultPaper.offset_width ,  y);
					}else if("right".equalsIgnoreCase( it.getAlign() )) {
						g.drawString(sb.toString(),  (DefaultPaper.width - x) + DefaultPaper.offset_width ,  y);
					}
					sb.setLength(0);
				}
				x = 0;//
				y += it.getSize();//换下一行。
				continue;
			}
			
			if( x + chw >= DefaultPaper.width) {//需要换行。
				g.drawString(sb.toString(), DefaultPaper.offset_width,  y);
				sb.setLength(0);
				x = 0;//
				y += it.getSize();//换下一行。
			}
			x += chw;
			sb.append(ch);
			//判断下一个字符是否需要换行。
		}
		//剩余部分。
		if(sb.length()>0) {
			if("left".equalsIgnoreCase(it.getAlign())) {
				g.drawString(sb.toString(),  DefaultPaper.offset_width  ,  y);
			}else if("center".equalsIgnoreCase( it.getAlign() )) {
				g.drawString(sb.toString(),  (DefaultPaper.width - x)/2 + DefaultPaper.offset_width ,  y);
			}else if("right".equalsIgnoreCase( it.getAlign() )) {
				g.drawString(sb.toString(),  (DefaultPaper.width - x) + DefaultPaper.offset_width ,  y);
			}
			sb.setLength(0);
		}
		return y + it.getSize();//返回下一行y坐标。
	}
	
	private static int getCharacterWidth(char ch,int size) {
		 if(ch>=0 && ch <=127)return size /2;
		 return size;
	}
}
