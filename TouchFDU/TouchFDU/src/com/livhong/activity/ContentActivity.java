package com.livhong.activity;

import java.lang.reflect.Method;

import com.livhong.base.BaseString;
import com.livhong.base.DensityUtil;
import com.livhong.base.MMAlert;
import com.livhong.base.Util;
import com.livhong.netbase.ComContent;
import com.livhong.netbase.FaoAttrs;
import com.livhong.netbase.JwcAttrs;
import com.livhong.netbase.NetString;
import com.livhong.netbase.NewsAttrs;
import com.livhong.netbase.WebAttrs;
import com.livhong.touchfdu.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

public class ContentActivity extends Activity implements OnMenuItemClickListener{

	String link = "";
	int content_type;
	int screen_width;
	
	String author = "";
	String date = "";
	String source = "";
	String title = "";
	String html = "";
	String base_path = "";
	
	TextView view_title, view_author, view_src, view_date;
	WebView webView;
	View parent;
	WebAttrs webAttrs;
	
	private IWXAPI wxApi; 
	private static final int MMAlertSelect1  =  0;
	public final static String WX_APP_ID = "wxd2049051fb9bae2f";
	
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			changeView();
			checkString();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID);  
		wxApi.registerApp(WX_APP_ID);
		setContentView(R.layout.content_layout);
		Bundle bundle = this.getIntent().getExtras();
		link = bundle.getString(BaseString.INTENT_LINK);
		content_type = bundle.getInt(BaseString.INTENT_CONTENT_TYPE);
		DisplayMetrics dm = new DisplayMetrics();  
		dm = getResources().getDisplayMetrics();
//		screen_width  = getWindowManager().getDefaultDisplay().getWidth();
		screen_width = DensityUtil.px2dip(getApplicationContext(), dm.widthPixels)-20;
//		System.out.println("screen_width "+screen_width);
		setupVar();
		fillData();
	}
	
	public void setupVar(){
		parent = findViewById(R.id.content_parent);
		view_title = (TextView) findViewById(R.id.content_title);
		view_author = (TextView) findViewById(R.id.content_author);
		view_src = (TextView) findViewById(R.id.content_src);
		view_date = (TextView) findViewById(R.id.content_date);
		webView = (WebView) findViewById(R.id.content_webview);
		switch (content_type) {
		case ComContent.TYPE_FAO:
			webAttrs = new FaoAttrs();
			break;
		case ComContent.TYPE_JWC:
			webAttrs = new JwcAttrs();
			break;
		case ComContent.TYPE_NEWS:
			webAttrs = new NewsAttrs();
			break;
		}
	}
	
	public void changeView(){
		view_title.setText(title);
		view_author.setText(author);
		view_src.setText(source);
		view_date.setText(date);
		webView.loadDataWithBaseURL(base_path, html, "text/html", "utf-8", null);
	}
	
	public void fillData(){
		new Thread(){
			public void run(){
				webAttrs.parseItemUrl(link, screen_width);
				author = webAttrs.getAuthor();
				title = webAttrs.getTitle();
				date = webAttrs.getDate();
				source = webAttrs.getSrc();
				html = webAttrs.getContentHtml();
				base_path = webAttrs.getBasePath();
				Message msg = new Message();
				msg.what = 0;
				handler.sendMessage(msg);
			}
		}.start();
	}
	
	public void checkString(){
		if(author==null||author.equals("")){
			view_author.setHeight(0);
		}
		if(date==null||date.equals("")){
			view_date.setHeight(0);
		}
		if(source==null||source.equals("")){
			view_src.setHeight(0);
		}
	}
	
	public void showPopup(View v) {
	    PopupMenu popup = new PopupMenu(this, v);
	    MenuInflater inflater = popup.getMenuInflater();
	    Menu menu = popup.getMenu();
	    inflater.inflate(R.menu.popupmenu, menu);
//	    setIconEnable(menu, true);
	    popup.show();
	}
	
	@Override
	public boolean onMenuItemClick(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_share:
	            return true;
	        default:
	            return false;
	    }
	}
	
	public void wechatShare(View view){  
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = this.link;
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = this.title;
		msg.description = "";
		Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_240);
		msg.thumbData = Util.bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		req.scene = SendMessageToWX.Req.WXSceneTimeline;
//		: SendMessageToWX.Req.WXSceneSession;
		wxApi.sendReq(req);
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	
	public void back(View view){
		finish();
	}
	
}
