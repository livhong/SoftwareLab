package com.livhong.netbase;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JwcAttrs extends WebAttrs{

	public final String BASE_PATH = "http://www.jwc.fudan.edu.cn";
	
	ArrayList<String> titles = new ArrayList();
	ArrayList<String> times = new ArrayList();
	ArrayList<String> links = new ArrayList();
	
	Document doc;
	Elements listItem;
	Elements listTime;
	Element item;
	
	private int size = -1;
	
	String itemTitle;
	String itemAuthor;
	String itemTime;
	
	ComContent itemContent;
	
	int imgSize = 320;
	
	public void parseListUrl(String link){
//		link = "http://www.fao.fudan.edu.cn/1691/list1.htm";
//		System.out.println(link);
		titles = new ArrayList();
		times = new ArrayList();
		links = new ArrayList();
		doc = Jsoup.parse(NetLinker.getHtml(link));
		Element wp = doc.getElementById("wp_news_w2");
		listItem = wp.getElementsByTag("a");
		listTime = wp.getElementsByAttributeValue("style", "white-space:nowrap");
		size = listItem.size();
		loadList();
	}
	
	private void loadList(){
		for(int i = 0; i < size; i++){
			String link = null;
			String time = null;
			Element ele = listItem.get(i);
			titles.add(ele.attr("title"));
			links.add(link = BASE_PATH+ (ele.attr("href")));
			times.add(listTime.get(i).text());
		}
	}
	
	public void parseItemUrl(String link){
		doc = Jsoup.parse(NetLinker.getHtml(link));
		item = doc.getElementById("container");
		loadItem();
	}
	
	public void parseItemUrl(String link, int imgWidth){
		this.imgSize = imgWidth;
		parseItemUrl(link);
	}
	
	private void loadItem(){
		try {
			//get the title of the article
			Element eTitle = item.getElementsByAttributeValue("class", "Article_Title").first();
			itemTitle = eTitle.text();
			System.out.println("itemTitle  "+itemTitle);
		} catch (Exception e) {
		}
		try {
			//get its author, source and time
			Element eAuthor = item.getElementsByAttributeValue("class", "Article_Author").first();
			itemAuthor = eAuthor.text();
		} catch (Exception e) {
		}
		try {
			Element eTime = item.getElementsByAttributeValue("class", "Article_PublishDate").first();
			itemTime = eTime.text();
		} catch (Exception e) {
		}
		
		//get the content of the article
		Element eContent = item.getElementsByAttributeValue("class", "Article_Content").first();
		itemContent = new ComContent(eContent.html(), ComContent.TYPE_JWC, imgSize);
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
		return "";
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return this.itemTitle;
	}
	
}
