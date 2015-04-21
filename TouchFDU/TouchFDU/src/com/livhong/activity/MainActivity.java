package com.livhong.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ant.liao.GifView;
import com.ant.liao.GifView.GifImageType;
import com.livhong.ResideMenu.ResideMenu;
import com.livhong.ResideMenu.ResideMenuItem;
import com.livhong.base.BaseString;
import com.livhong.base.DensityUtil;
import com.livhong.fragment.MyFragment;
import com.livhong.fragment.MyFragmentListener;
import com.livhong.materialtabs.MaterialTab;
import com.livhong.materialtabs.MaterialTabHost;
import com.livhong.materialtabs.MaterialTabListener;
import com.livhong.netbase.ComContent;
import com.livhong.pulltorefresh.PullRefreshLayout;
import com.livhong.touchfdu.R;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Layout;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener,
		MaterialTabListener, MyFragmentListener, OnItemClickListener {

	ResideMenu resideMenu;
	String[] titles = { "Edit", "Star", "Exit" };

	// varible of tabhost
	MaterialTabHost tabHost;
	ViewPager pager;
	ViewPagerAdapter adapter;

	MyFragment[] frag = new MyFragment[3];// to hold news, jwc, fao;s contents
	ListView[] listViews = new ListView[3];
	List<Map<String, Object>>[] infoLists = new ArrayList[3];
	String[] listItem = new String[] { BaseString.LIST_TIME,
			BaseString.LIST_TITLE };
	SimpleAdapter[] listAdapters = new SimpleAdapter[3];
	ScrollView[] scrollViews = new ScrollView[3];
	GifView[] imgViews = new GifView[3];
	TextView[] textViews = new TextView[3];
	Thread[] loadThreads = new Thread[3];

	PullRefreshLayout layout;

	Thread checkInternet;
	
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			layout.setRefreshing(false);
			imgViews[msg.what].setVisibility(View.INVISIBLE);
			listAdapters[msg.what].notifyDataSetChanged();
			setListViewHeightBasedOnChildren(listViews[msg.what]);
			textViews[msg.what].setVisibility(View.VISIBLE);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		// SharedPreferences sp = getSharedPreferences(BaseString.PRE_NAME,
		// BaseString.MODEL);
		// SharedPreferences.Editor e = sp.edit();

		// the order of the following two functions can't be revorted!
		bindTabHost();
		bindSlideMenu();
		bindPullToRefresh();
//		beginToCheckInternet();
		// onTabCreate(0);
	}
	
	public void beginToCheckInternet(){
		checkInternet = new Thread(){
			public void run(){
				while(true){
					checkInternet();
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};
		checkInternet.start();
	}
	
	public void checkInternet(){
		ConnectivityManager con=(ConnectivityManager)getSystemService(Activity.CONNECTIVITY_SERVICE);  
		boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();  
		boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();  
		if(wifi|internet){  
		}else{  
			Toast.makeText(getApplicationContext(), "网络未连接！", Toast.LENGTH_LONG).show();
		} 
	}

	public void bindPullToRefresh() {
		int red = getResources().getColor(R.color.red);
		layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
		layout.setRefreshStyle(PullRefreshLayout.STYLE_CIRCLES);
		layout.setColorSchemeColors(new int[] { red, red, red, red });
		// listen refresh event
		layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				onTabCreate(pager.getCurrentItem());
			}
		});
		// layout.setEnabled(false);
	}

	protected void bindSlideMenu() {
		resideMenu = new ResideMenu(this);
		resideMenu.setBackground(R.drawable.am_girl);
		resideMenu.attachToActivity(this);

		int icon[] = { R.drawable.edit, R.drawable.star, R.drawable.exit };

		for (int i = 0; i < titles.length; i++) {
			ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
			item.setOnClickListener(this);
			item.setTag(i);
			resideMenu.addMenuItem(item, ResideMenu.DIRECTION_LEFT);
			resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT);
		}

	}

	protected void bindTabHost() {
		for (int i = 0; i < frag.length; i++) {
			if (i == 0) {
				frag[i] = new MyFragment(i, ComContent.TYPE_NEWS);
			} else if (i == 1) {
				frag[i] = new MyFragment(i, ComContent.TYPE_JWC);
			} else if (i == 2) {
				frag[i] = new MyFragment(i, ComContent.TYPE_FAO);
			}
			frag[i].setMylistener(this);
		}

		tabHost = (MaterialTabHost) this.findViewById(R.id.materialTabHost);
		pager = (ViewPager) this.findViewById(R.id.viewpager);

		// init view pager
		pager.setOffscreenPageLimit(3);
		adapter = new ViewPagerAdapter(getSupportFragmentManager());
		pager.setAdapter(adapter);
		pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// when user do a swipe the selected tab change
				tabHost.setSelectedNavigationItem(position);
				if (position != 0)
					resideMenu
							.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
				else
					resideMenu.setSwipeDirecionAble(ResideMenu.DIRECTION_LEFT);
			}

		});

		// insert all tabs from pagerAdapter data
		for (int i = 0; i < adapter.getCount(); i++) {
			tabHost.addTab(tabHost.newTab().setText(adapter.getPageTitle(i))
					.setTabListener(this));

		}
	}

	/*
	 * @onClick - to identify the activity user will start
	 */
	@Override
	public void onClick(View view) {
		int position = Integer.parseInt(view.getTag().toString());
		if (position == 2) {
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();
			return;
		}
		Toast.makeText(getApplicationContext(), titles[position],
				Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, ShowActivity.class);
		startActivity(intent);
//		resideMenu.closeMenu();
	}

	/*
	 * @End onClick
	 */

	public void openMenu(View view) {
		if (resideMenu.isOpened()) {
			resideMenu.closeMenu();
			return;
		} else {
			resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
		}
	}

	Thread[] createThreads = new Thread[3];

	private void onTabCreate(int position) {
		infoLists[position] = new ArrayList<Map<String, Object>>();
		listViews[position] = frag[position].getListView();
		listViews[position].setOnItemClickListener(this);
		scrollViews[position] = frag[position].getScrollView();
		imgViews[position] = (GifView) scrollViews[position]
				.findViewById(R.id.loadImg);
		imgViews[position].setGifImage(R.drawable.loading);
		textViews[position] = (TextView) scrollViews[position]
				.findViewById(R.id.loadText);
		textViews[position].setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int position = pager.getCurrentItem();
				textViews[position].setVisibility(View.INVISIBLE);
				imgViews[position].setVisibility(View.VISIBLE);
				startLoadThread(position);
			}
		});
		listAdapters[position] = new SimpleAdapter(this, infoLists[position],
				R.layout.activity_list_item, listItem, new int[] {
						R.id.list_time, R.id.list_title });
		listViews[position].setAdapter(listAdapters[position]);
		startCreateThread(position);
	}

	private void startCreateThread(int position) {
		if (position == 0) {
			createThreads[position] = new Thread() {
				public void run() {
					List list = new ArrayList();;
					try{
						list = frag[0].getFirstList();
					}catch(Exception e){
						
					}
					checkInternet(list);
					infoLists[0].addAll(list);
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}
			};
		} else if (position == 1) {
			createThreads[position] = new Thread() {
				public void run() {
					List list = new ArrayList();;
					try{
						list = frag[1].getFirstList();
					}catch(Exception e){
						
					}
					checkInternet(list);
					infoLists[1].addAll(list);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			};
		} else if (position == 2) {
			createThreads[position] = new Thread() {
				public void run() {
					List list = new ArrayList();;
					try{
						list = frag[2].getFirstList();
					}catch(Exception e){
						
					}
					checkInternet(list);
					infoLists[2].addAll(list);
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
			};
		}
		createThreads[position].start();
	}

	private void startLoadThread(int position) {
		if (position == 0) {
			loadThreads[position] = new Thread() {
				public void run() {
					try {
						sleep((long) 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List list = new ArrayList();;
					try{
						list = frag[0].getNextList();
					}catch(Exception e){
						
					}
					checkInternet(list);
					infoLists[0].addAll(list);
					Message msg = new Message();
					msg.what = 0;
					handler.sendMessage(msg);
				}
			};
		} else if (position == 1) {
			loadThreads[position] = new Thread() {
				public void run() {
					try {
						sleep((long) 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List list = new ArrayList();;
					try{
						list = frag[1].getNextList();
					}catch(Exception e){
						
					}
					checkInternet(list);
					infoLists[1].addAll(list);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				}
			};
		} else if (position == 2) {
			loadThreads[position] = new Thread() {
				public void run() {
					try {
						sleep((long) 1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List list = new ArrayList();;
					try{
						list = frag[2].getNextList();
					}catch(Exception e){
						
					}
					checkInternet(list);
					infoLists[2].addAll(list);
					Message msg = new Message();
					msg.what = 2;
					handler.sendMessage(msg);
				}
			};
		}
		loadThreads[position].start();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int id = pager.getCurrentItem();
		if (scrollViews[id] != null) {
			int item = scrollViews[id].getScrollY();
			if (item == 0) {
				layout.setEnabled(true);
			} else {
				layout.setEnabled(false);
			}
		}
		return resideMenu.dispatchTouchEvent(ev);
	}

	/*
	 * @onTabSelected - to decide how to to when user tap the TabHost
	 */
	@Override
	public void onTabSelected(MaterialTab tab) {
		pager.setCurrentItem(tab.getPosition());
		if (tab.getPosition() != 0)
			resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_LEFT);
		else
			resideMenu.setSwipeDirecionAble(ResideMenu.DIRECTION_LEFT);
	}

	@Override
	public void onTabReselected(MaterialTab tab) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTabUnselected(MaterialTab tab) {
		// TODO Auto-generated method stub
	}

	/*
	 * @End onTabSelected
	 */

	/*
	 * @onKeyDone - to verify wether the user is sure to exit the application
	 */

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (resideMenu.isOpened()) {
			resideMenu.closeMenu();
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// 创建退出对话框
			AlertDialog isExit = new AlertDialog.Builder(this).create();
			// 设置对话框标题
			isExit.setTitle("系统提示");
			// 设置对话框消息
			isExit.setMessage("确定要退出吗");
			// 添加选择按钮并注册监听
			isExit.setButton("确定", listener);
			isExit.setButton2("取消", listener);
			// 显示对话框
			isExit.show();

		}

		return false;

	}

	/** 监听对话框里面的button点击事件 */
	DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
				finish();
				break;
			case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
				break;
			default:
				break;
			}
		}
	};

	/*
	 * @End onKeyDone
	 */

	private class ViewPagerAdapter extends FragmentStatePagerAdapter {

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int num) {
			return frag[num];
		}

		@Override
		public int getCount() {
			return frag.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			String title = null;
			switch (position) {
			case 0:
				title = getResources().getString(R.string.tab_news);
				break;
			case 1:
				title = getResources().getString(R.string.tab_jwc);
				break;
			case 2:
				title = getResources().getString(R.string.tab_fao);
				break;
			default:
				title = "";
			}
			return title;
		}

	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	@Override
	public void onFragmentCreate(int num) {
		onTabCreate(num);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		int page = pager.getCurrentItem();
		String link = frag[page].getLink(position);
		Intent intent = new Intent(this, ContentActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(BaseString.INTENT_LINK, link);
		bundle.putInt(BaseString.INTENT_CONTENT_TYPE, frag[page].getType());
		intent.putExtras(bundle);
		startActivity(intent);
	}
	
	public void checkInternet(List list){
		if(list.size()==0){
			Toast.makeText(getApplicationContext(), "请检查网络连接！", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

//	private class MyTextView extends TextView {
//
//		public MyTextView(Context context) {
//			super(context);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//
//			Layout layout = getLayout();
//			if (layout != null) {
//				int height = (int) FloatMath.ceil(getMaxLineHeight(this
//						.getText().toString()))
//						+ getCompoundPaddingTop()
//						+ getCompoundPaddingBottom();
//				int width = getMeasuredWidth();
//				setMeasuredDimension(width, height);
//			}
//		}
//
//		private float getMaxLineHeight(String str) {
//			float height = 0.0f;
//			float screenW = ((Activity) getParent()).getWindowManager()
//					.getDefaultDisplay().getWidth();
//			float paddingLeft = ((RelativeLayout) this.getParent())
//					.getPaddingLeft();
//			float paddingReft = ((RelativeLayout) this.getParent())
//					.getPaddingRight();
//			// 这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，这个是拿TextView父控件的Padding的，为了更准确的算出换行
//			int line = (int) Math
//					.ceil((this.getPaint().measureText(str) / (screenW
//							- paddingLeft - paddingReft)));
//			height = (this.getPaint().getFontMetrics().descent - this
//					.getPaint().getFontMetrics().ascent) * line;
//			return height;
//		}
//
//	}

}
