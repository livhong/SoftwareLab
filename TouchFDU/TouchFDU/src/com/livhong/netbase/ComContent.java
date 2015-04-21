package com.livhong.netbase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.livhong.base.BaseString;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ComContent {

	public static final int TYPE_NEWS = 0;
	public static final int TYPE_JWC = 1;
	public static final int TYPE_FAO = 2;
	
	private int content_type = -1;
	
	String base_path;
	
	String content;
	String html;
	
	int img_width = 320;
	
	public ComContent(String content, int type){
		this(content, type, 320);
	}
	
	public ComContent(String content, int type, int imgWidth){
		this.content=content;
		this.content_type = type;
		this.img_width = imgWidth;
		switch(type){
			case TYPE_FAO:
				base_path = NetString.BASE_PATH_FAO;
				break;
			case TYPE_JWC:
				base_path = NetString.BASE_PATH_JWC;
				break;
			case TYPE_NEWS:
				base_path = NetString.BASE_PATH_NEWS;
				break;
		}
		load();
	}
	
	public int getContentType(){
		return this.content_type;
	}
	
	public String getBasePath(){
		return base_path;
	}
	
	public void load(){
		Document dPara = Jsoup.parse(content);
		Elements imgs = dPara.getElementsByTag("img");
		int size = imgs.size();
		for(int i = 0; i < size; i++){
			Element img = imgs.get(i);
			if(img.attr("src").contains("/themes/default/images/")||img.attr("src").contains("editor/images/file/"))
				continue;
			img.attr("style", "");
			img.attr("width",""+img_width);
			img.removeAttr("height");
		}
		html = dPara.html();
	}
	
	public String getContentHtml(){
		return this.html;
	}
	
}
