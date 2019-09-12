package com.print.test;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import com.print.bean.DefaultPaper;
import com.print.bean.PrintItem;
import com.print.service.PrintService;

public class PrintServiceTest {
	private static void newWindow(List<PrintItem> listitem) {
		JFrame jf = new JFrame();
		jf.setSize(162, 600);
		jf.setUndecorated(true);
		jf.setLocationRelativeTo(null);
		
		Canvas cavs = new Canvas() {
			@Override
			public void paint(Graphics g) {
				super.paint(g);
			    PrintService2.printItem(g, listitem);
			}
		};
		jf.add( cavs );
		jf.setVisible(true);
		
	}
	
	public static void print(  ) {
		
		 if (PrinterJob.lookupPrintServices().length > 0) {  
	            PageFormat pageFormat = new PageFormat();
	            pageFormat.setOrientation(PageFormat.PORTRAIT);   //设置打印方向为：水平从左到右，垂直从上到下。
	            pageFormat.setPaper( DefaultPaper.defaultPaper());
	            Book book = new Book();  
	            book.append(new Printable() {
					@Override
					public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException {
						 if (pageIndex > 0) {  
                            return Printable.NO_SUCH_PAGE;  
                        }  
						for(int i=30;i<20+30;i++) {
							 g.drawLine(i,10, i+1, 10);
						}
						 
                   	 return Printable.PAGE_EXISTS;  
					}
				},pageFormat);
	            PrinterJob printerJob = PrinterJob.getPrinterJob();  
	            printerJob.setPageable(book);  
	          try {  
	                printerJob.print();  
	            } catch (PrinterException e) {  
	                e.printStackTrace();  
	            }   
	            
		}else {
		}
	}
	
	public static void main(String[] args) throws Exception {
	  List<PrintItem> printlist  =new ArrayList<PrintItem>();
		//标题
		 	PrintItem title = new PrintItem();
			title.setText("外卖点餐");
			title.setSize(24);
			title.setAlign("center");
		printlist.add(title);
		//选项1：
			PrintItem item1 = new PrintItem();
			item1.setText("名称：黄焖鸡米饭");
		printlist.add(item1);
		//选项2：
			PrintItem item2 = new PrintItem();
			item2.setText("价格：￥12");
		printlist.add(item2);
		 
		//选项3：
			PrintItem item3 = new PrintItem();
			item3.setText("备注：老板我手里只有一块钱。可以点你家的黄焖鸡米饭吗？");
		printlist.add(item3);
		//选项4：
			PrintItem item4 = new PrintItem();
			item4.setAlign("right");
			item4.setSize(16);
			item4.setText(getCurtime());
		printlist.add(item4);		
		//打印在画布上。
		//newWindow(printlist);
		//打印在小票上。
		PrintService.print(printlist);
		 
	}
	private static String getCurtime() {
		return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format( new Date());
	}
}
