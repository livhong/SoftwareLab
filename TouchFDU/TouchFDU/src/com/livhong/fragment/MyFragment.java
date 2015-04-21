package com.livhong.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.livhong.base.BaseString;
import com.livhong.netbase.ComContent;
import com.livhong.netbase.FaoAttrs;
import com.livhong.netbase.JwcAttrs;
import com.livhong.netbase.NetLinker;
import com.livhong.netbase.NetString;
import com.livhong.netbase.NewsAttrs;
import com.livhong.netbase.WebAttrs;
import com.livhong.touchfdu.R;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/**
 * Created by neokree on 16/12/14.
 */
public class MyFragment extends Fragment{
	
	private View view;
	private int num;
	private ListView listView;
	private String link;
	private ScrollView scrollView;
	private MyFragmentListener mylistener;
	
	private int type;
	private WebAttrs webAttr;
	
	private ArrayList<String> links;
	
	public MyFragment(int num, int type) {
		this.num = num;
		this.type = type;
		switch (type) {
		case ComContent.TYPE_FAO:
			type = ComContent.TYPE_FAO;
			link = NetString.FIRST_FAO_STRING;
			webAttr = new FaoAttrs();
			break;
		case ComContent.TYPE_JWC:
			type = ComContent.TYPE_JWC;
			link = NetString.FIRST_JWC_STRING;
			webAttr = new JwcAttrs();
			break;
		case ComContent.TYPE_NEWS:
			type = ComContent.TYPE_NEWS;
			link = NetString.FIRST_NEW_STRING;
			webAttr = new NewsAttrs();
			break;
		}
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        TextView text = new TextView(container.getContext());
//        text.setText("Fragment content "+num);
//        text.setGravity(Gravity.CENTER);
//
//        return text;
    	view = inflater.inflate(R.layout.content_inner_layout, container, false);
    	listView = (ListView) view.findViewById(R.id.baselist);
    	scrollView = (ScrollView) view.findViewById(R.id.list_scrollview);
    	mylistener.onFragmentCreate(num);
    	return view;
    }
    
    public int getType(){
    	return type;
    }
    
    public ArrayList<Map<String, Object>> getFirstList(){
    	webAttr.parseListUrl(link);
    	links = webAttr.getListLink();
    	ArrayList<Map<String, Object>> listMap = new ArrayList();
    	ArrayList times = webAttr.getListTime();
    	ArrayList titles = webAttr.getListTitle();
    	int size = times.size();
    	for(int i =0; i < size; i++){
    		Map<String, Object> map = new HashMap();
    		map.put(BaseString.LIST_TIME, times.get(i));
    		map.put(BaseString.LIST_TITLE, titles.get(i));
    		listMap.add(map);
    	}
    	return listMap;
    }
    
    public ArrayList<Map<String, Object>> getNextList(){
    	webAttr.parseListUrl(NetLinker.getNext(link));
    	links.addAll(webAttr.getListLink());
    	ArrayList<Map<String, Object>> listMap = new ArrayList();
    	ArrayList times = webAttr.getListTime();
    	ArrayList titles = webAttr.getListTitle();
    	int size = times.size();
    	for(int i =0; i < size; i++){
    		Map<String, Object> map = new HashMap();
    		map.put(BaseString.LIST_TIME, times.get(i));
    		map.put(BaseString.LIST_TITLE, titles.get(i));
    		listMap.add(map);
    	}
    	return listMap;
    }
    
    public String getLink(int position){
    	return links.get(position);
    }
    
    public void setMylistener(MyFragmentListener mylistener) {
		this.mylistener = mylistener;
	}

	public ListView getListView(){
    	return this.listView;
    }

	public ArrayList<String> getLinks() {
		return links;
	}
	
	public ScrollView getScrollView(){
		return this.scrollView;
	}
    
}
