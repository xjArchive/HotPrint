package com.print.task;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.print.bean.PrintItem;
import com.print.facade.PrintServiceFacade;

public class PrintTaskManager {
	private static BlockingQueue<List<PrintItem>> que = new LinkedBlockingDeque<>(2);
	/**
	 * 初始化阻塞线程。
	 */
	public static void init() {
		new Thread() {
			public void run() {
				while(true) {
					try {
						List<PrintItem> printitem = que.take();
						PrintServiceFacade.print(printitem);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public static void addPrintTask(List<PrintItem> task) {
		try {
			que.put(task);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
