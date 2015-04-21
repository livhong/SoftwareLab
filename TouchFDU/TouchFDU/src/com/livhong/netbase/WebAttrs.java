package com.livhong.netbase;

import java.util.ArrayList;

public abstract class WebAttrs {

	public abstract void parseListUrl(String link);
	public abstract void parseItemUrl(String link);
	public abstract void parseItemUrl(String link, int imgWidth);
	public abstract String getContentHtml();
	public abstract String getBasePath();
	public abstract ArrayList<String> getListTitle();
	public abstract ArrayList<String> getListTime();
	public abstract ArrayList<String> getListLink();
	public abstract String getAuthor();
	public abstract String getDate();
	public abstract String getSrc();
	public abstract String getTitle();
	
}
