package com.org.palmcampus.oa.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.service.NewsService;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.DBUtil;
import com.org.palmcampus.oa.util.SysExitUtil;
import com.readystatesoftware.viewbadger.BadgeView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TabWidget;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

//移动办公主界面
public class PersonalActivity extends Activity {

	private TextView title_TextView;
	// 类似九宫格控件，显示文字和图片
	private GridView personal_GridView;
	// 接受广播新闻
	BroadcastMain broadcastMain;
	// 新通知数量
	int newscount = 0;
	// 本地未读新闻数量
	int not_read_count;
	int totalcount = 0;
	// 数据库
	private SQLiteDatabase db;
	private static EventData events;

	
	// 界面图片
	private int[] personal_GridView_img_item = { R.drawable.personal_notice,
			R.drawable.personal_journal, R.drawable.personal_agency,
			R.drawable.personal_mailist,

			R.drawable.personal_customer, R.drawable.personal_schedule,
			R.drawable.personal_camera,R.drawable.personal_studentloan,
			R.drawable.personal_flow,
			//R.drawable.personal_message,
			// ,
	// R.drawable.personal_email,
	// R.drawable.personal_journal
	};
	// private String[] personal_GridView_name_item = { "个人事项", "日程安排", "代办事宜",
	// "信息动态", "通知公告", "通讯录", "我的文件", "我的邮箱", "个人日记" };

	private String[] personal_GridView_name_item = { "通知公告", "日程安排", "课程表",
			"图书借阅", "网上选课", "成绩查询","我的作业" ,"助学信息","流程审批"
//			,"家校互动"
			};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("退出新闻消息广播");
		Intent intent = new Intent();
		intent.setClass(this, NewsService.class);
		// 停止掉邮件服务
		stopService(intent);
		// 注销广播
		unregisterReceiver(broadcastMain);

	}

	// 获取本地未新闻数量
	private int getNotReadCount() {
		int count = 0;
		// 取出数据库的值
		events = new EventData(PersonalActivity.this);
		db = events.getReadableDatabase();

		Cursor c = db.rawQuery("select * from gongGao where isNew=?",new String[]{"1"});

		while (c.moveToNext()) {
			count++;
		}
		c.close();
		db.close();
		return count;
	}

	/**
	 * 页面初始化
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_personal);
		// 启动新闻服务
		Intent intent = new Intent(this, NewsService.class);
		startService(intent);
		broadcastMain = new BroadcastMain();
		IntentFilter filter = new IntentFilter();
		filter.addAction(NewsService.BROADCASTACTION);
		// 注册广播接收器
		registerReceiver(broadcastMain, filter);
		// 获取本地未读新闻数量
		//not_read_count = getNotReadCount();
		title_TextView = (TextView) findViewById(R.id.title_Text_TV);
		title_TextView.setText("智慧校园");

		// BadgeView badge = new BadgeView(this, title_TextView);
		// badge.setText("1");
		// badge.show();
		personal_GridView = (GridView) findViewById(R.id.menu_personal_GV);
		personal_GridView.setAdapter(getGridViewAdapter(
				personal_GridView_name_item, personal_GridView_img_item));
		
		personal_GridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				switch (arg2) {
				// 通知公告
				case 0:
					Intent_Context(PersonalActivity.this, News_List.class);

					break;
	
				}
			}

		});
		
	}

	/**
	 * GridView适配器
	 */
	private MySimpleAdapter getGridViewAdapter(String[] menuNameArray,
			int[] menuimgArray) {
		ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < menuNameArray.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("itemImage", menuimgArray[i]);
			map.put("itemText", menuNameArray[i]);
			data.add(map);
		}
		MySimpleAdapter simperAdapter = new MySimpleAdapter(this, data,
				R.layout.menu_item, new String[] { "itemImage", "itemText" },
				new int[] { R.id.menu_item_image_IV, R.id.menu_item_text_TV });

		return simperAdapter;
	}

	@Override
	protected void onResume() {// 从其它界面返回时会调用onResume，更新GridView
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("返回主界面");
		personal_GridView.setAdapter(getGridViewAdapter(
				personal_GridView_name_item, personal_GridView_img_item));
	}
	/**
	 * 推出按钮
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
	}
	/**
	 * 页面跳转
	 */
	public void Intent_Context(Context front_context, Class<?> behind_context) {
		Intent intent = new Intent();
		intent.setClass(front_context, behind_context);
		// intent.putExtra("methodname", methodname);
		front_context.startActivity(intent);
	}

	// 重写SimpleAdapter，更新图标
	class MySimpleAdapter extends SimpleAdapter {
		public MySimpleAdapter(Context context,
				List<? extends Map<String, ?>> data, int resource,
				String[] from, int[] to) {
			super(context, data, resource, from, to);
			// TODO Auto-generated constructor stub
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.SimpleAdapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = super.getView(position, convertView, parent);
	
			if (position == 0) { // 通知公告
				// 获取通知公个数，并用数字方式提示
				// int not_read_count=getNotReadCount();

				
					View news_view = view.findViewById(R.id.menu_item_RL);
					BadgeView badge_news = new BadgeView(getApplicationContext(),
							news_view);
					not_read_count=getNotReadCount();
				// 如果有消息
				
					if (not_read_count != 0) {
						badge_news.setText(String.valueOf(not_read_count));
						badge_news.show();
					}
			}
			return view;
		}
	}

	// 广播接收器
	class BroadcastMain extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			System.out.println("开始接受新闻");
			int count = intent.getIntExtra("newNewsCount", 0);

			if (count > 0) {
				System.out.println("有新闻");
				newscount = count;
//				Toast.makeText(getApplicationContext(), "收到"+count+"条新通告", 5000).show();
				onResume();
//				 View view=(View)personal_GridView.getItemAtPosition(0);
//				 View myview=view.findViewById(R.id.menu_item_RL);
//				 System.out.println("&&&&&&&&&&&&&"+myview.getId());
//				 BadgeView badge = new BadgeView(getApplicationContext(),
//				 myview);
//				 badge.setText(String.valueOf(count));
//				 badge.show();
			}

		}

	}

}
