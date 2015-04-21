package com.livhong.netbase;

public class NetString {


	public final static String BASE_PATH_NEWS = "http://news.fudan.edu.cn";
	public final static String BASE_PATH_JWC = "http://www.jwc.fudan.edu.cn";
	public final static String BASE_PATH_FAO = "http://www.fao.fudan.edu.cn";
	
/*
 * 1. App' each page may contain 10 items.
 * 2. App shows no more than 100 items in total.
*/
	
/*
 * news links is easy to get 
 *  1. The format of the links is http://news.fudan.edu.cn/news/zhxw/#.html(# represents number, begin at number 1)
*/
	public final static String NEW_URL_STRING = "http://news.fudan.edu.cn/news/zhxw/";
	public final static String FIRST_NEW_STRING = "http://news.fudan.edu.cn/news/zhxw/1.html";
/*
 * 1. Format : http://www.jwc.fudan.edu.cn/3167/list#.htm(# represents number, begin at number 1)
*/
	public final static String JWC_URL_STRING = "http://www.jwc.fudan.edu.cn/3167/";
	public final static String FIRST_JWC_STRING = "http://www.jwc.fudan.edu.cn/3167/list1.htm";
/*
 * 1. Format : http://www.fao.fudan.edu.cn/1691/list#.htm(# represents number, begin at number 1)
*/
	public final static String FAO_URL_STRING = "http://www.fao.fudan.edu.cn/1691/";
	public final static String FIRST_FAO_STRING = "http://www.fao.fudan.edu.cn/1691/list1.htm";
	
}
