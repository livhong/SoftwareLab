package com.livhong.netbase;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NetLinker {

	public static String getHtml(String path) {
		try {
			URL url = new URL(path);
			URLConnection uc = url.openConnection();
			InputStream ins = uc.getInputStream();
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int len = 0;
			while((len=ins.read(buf))!=-1){
				data.write(buf, 0, len);
			}
			ins.close();
			return new String(data.toByteArray());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			System.out.println("Error!");
			e1.printStackTrace();
		}
		return null;
	}
	
	public static String getNext(String url){
		String[] str = url.split("[0-9]+.htm");
		String start = str[0];
		int len = str.length;
		str = url.split(str[0]);
		String end = str[1];
		str = end.split(".htm");
		int page = Integer.parseInt(str[0]);
		page++;
		if(len==1)
			return start+page+".htm";
		else
			return start+page+".html";
	}
	
}
