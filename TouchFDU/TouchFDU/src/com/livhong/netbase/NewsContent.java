package com.livhong.netbase;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class NewsContent {

	public final String BASE_PATH = "http://news.fudan.edu.cn";
	
	int paraSize = 0;
	int imageSize = 0;
	ArrayList<String> paras = new ArrayList();
	ArrayList<Boolean> isImg = new ArrayList();
	ArrayList<String> images = new ArrayList();
	String content;
	
	private int count = 0;
	
	public NewsContent(String content){
		this.content = content;
	}
	
	//@return - wether has next element
	public boolean hasNext(){
		if(isImg.get(count)==null){
			count++;
			return false;
		}
		else{
			count++;
			return true;
		}
	}
	
	public boolean isImg(){
		return isImg.get(count);
	}
	
	//@return - wether after the paragraph is image.
	public String getNext(){
		return paras.get(count);
	}
	
	public void load(){
		Elements eParas = Jsoup.parse(content).getElementsByTag("p");
		int size = eParas.size();
		for(int i=0; i < size; i++){
			Elements eImgs = eParas.get(i).getElementsByTag("img");
			if(eImgs.size()==0){
				isImg.add(false);//return last paragraph wether has adjacent img
				paras.add(eParas.get(i).text());
				paraSize++;
			}else{
				String path = BASE_PATH+eImgs.first().attr("src");
				isImg.add(true);
				paras.add(path);
				images.add(path);
				imageSize++;
			}
		}
		isImg.add(null);
	}
	
}
