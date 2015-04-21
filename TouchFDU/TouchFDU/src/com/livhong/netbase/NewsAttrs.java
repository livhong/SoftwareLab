package com.livhong.netbase;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewsAttrs extends WebAttrs{
	
	public final String BASE_PATH = "http://news.fudan.edu.cn/";
	
	ArrayList<String> titles = new ArrayList();
	ArrayList<String> times = new ArrayList();
	ArrayList<String> images = new ArrayList();
	ArrayList<String> links = new ArrayList();
//	ArrayList<String> contents = new ArrayList();
	
	Document doc;
	Elements listItem;
	Element item;
	
	private int size = -1;
	private int type = -1;
	
	String itemTitle;
	String itemAuthor;
	String itemSource;
	String itemTime;
	
	ComContent itemContent;
	
	int imgSize = 320;

	public void parseListUrl(String link){
//		link = "http://www.fao.fudan.edu.cn/1691/list1.htm";
		titles = new ArrayList();
		times = new ArrayList();
		links = new ArrayList();
		doc = Jsoup.parse(NetLinker.getHtml(link));
		listItem = doc.getElementsByClass("url");
		size = listItem.size();
		loadList();
	}
	
	public void loadList(){
		for(int i = 0; i < size; i++){
			String link = null;
			String time = null;
			Element ele = listItem.get(i);
			titles.add(ele.attr("title"));
			links.add(link = BASE_PATH+ (time = ele.attr("href")));
			String str[] = time.split("/");
			time = str[0] + "-" +str[1].substring(0, 2)+"-"+str[1].substring(2, 4);
			times.add(time);
		}
	}
	
	public void parseItemUrl(String link){
		doc = Jsoup.parse(NetLinker.getHtml(link));
		item = doc.getElementById("content");
		loadItem();
	}
	
	public void parseItemUrl(String link, int imgWidth){
		this.imgSize = imgWidth;
		parseItemUrl(link);
	}
	
	public void loadItem(){
		//get the title of the article
		Element eTitle = item.getElementById("content_head");
		itemTitle = eTitle.getAllElements().first().text();
		
		//get its author, source and time
		Elements eSummary = item.getElementsByTag("h2").first().getElementsByTag("span");
		for(int i = 0; i < eSummary.size(); i++){
			if(i==0){
				itemAuthor = eSummary.get(0).text();
			}
			else if(i==1){
				if(eSummary.get(0).getElementsByTag("span").size()==0)
					itemSource = eSummary.get(1).text();
			}
			else if(i==2){
				if(eSummary.get(0).getElementsByTag("span").size()==0&&eSummary.get(1).getElementsByTag("span").size()==0)
				itemTime = eSummary.get(2).text();
			}
		}
		
		//get the content of the article
		Element eContent = item.getElementById("endtext");
		itemContent = new ComContent(eContent.html(), ComContent.TYPE_NEWS, imgSize);
	}
	
	public String getContentHtml(){
		return itemContent.getContentHtml();
	}
	
	public String getBasePath(){
		return itemContent.getBasePath();
	}
	
	@Override
	public ArrayList<String> getListTitle() {
		return this.titles;
	}

	@Override
	public ArrayList<String> getListTime() {
		return this.times;
	}

	@Override
	public ArrayList<String> getListLink() {
		return this.links;
	}

	@Override
	public String getAuthor() {
		// TODO Auto-generated method stub
		return this.itemAuthor;
	}

	@Override
	public String getDate() {
		// TODO Auto-generated method stub
		return this.itemTime;
	}

	@Override
	public String getSrc() {
		// TODO Auto-generated method stub
		return this.itemSource;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return this.itemTitle;
	}
}
