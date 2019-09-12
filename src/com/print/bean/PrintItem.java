package com.print.bean;

public class PrintItem {
	private String text = "";//文本  溢出会根据水平对其方式折行。
	private int size = 12;//默认12号字体。可选8~24 默认24 。
	private String fontfamily = "宋体"; //可选 宋体【默认】、微软雅黑
	private String align ="left";//可选 left【默认】 center right 
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getFontfamily() {
		return fontfamily;
	}
	public void setFontfamily(String fontfamily) {
		this.fontfamily = fontfamily;
	}
	public String getAlign() {
		return align;
	}
	public void setAlign(String align) {
		this.align = align;
	}
	@Override
	public String toString() {
		return "PrintItem [text=" + text + ", size=" + size + ", fontfamily=" + fontfamily + ", align=" + align + "]";
	}
	 
	
}
