package com.org.palmcampus.oa.page;

import java.util.ArrayList;
import java.util.List;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.pojo.ErpgongGao;
import com.org.palmcampus.oa.pojo.adapter.NewsPagination;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;



import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

//员工信息列表界面
public class News_List extends Activity implements OnScrollListener {

	private TextView title_TextView, hasdata_TextView;
	private ListView notice_ListView;
	private Button loadMoreButton;
	private NewsPagination adapter;
	private ProgressDialog progressDialog;
	private int visibleLastIndex = 0;
	private int visibleItemCount;
	// // 数据库
	private SQLiteDatabase db,db1;
	private static EventData events;
	private List<ErpgongGao> localNewsList = new ArrayList<ErpgongGao>();
	// private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_list);
		SysExitUtil.activityList.add(this);
		  //定义NotificationManager
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager)getSystemService(ns);
		 //清除邮件通知
		 mNotificationManager.cancel(1);
		// 返回按钮
		Button return_Button = (Button) findViewById(R.id.title_setting_Btn);
		return_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				News_List.this.finish();
			}

		});

		title_TextView = (TextView) findViewById(R.id.title_Text_TV);
		title_TextView.setText("通知公告");
		hasdata_TextView = (TextView) findViewById(R.id.tv_hasdata);
		notice_ListView = (ListView) findViewById(R.id.view_list_LV);
		
		// // 取出数据库的值
		events = new EventData(News_List.this);
//		
		getLocalNews();
		if (localNewsList.size() == 0) {
			hasdata_TextView.setVisibility(View.VISIBLE);
		}
		// System.out.println(listFieldlist.size());
		// 生成适配器信息（通过分页类）
		adapter = new NewsPagination(News_List.this, localNewsList);
		// 将信息填充到ListView上
		notice_ListView.setAdapter(adapter);
		notice_ListView
				.setOnItemClickListener(new ListViewItemSelectListener());
		notice_ListView.setOnScrollListener(this);

	}

	/**
	 * 查询所有本地新闻
	 * 
	 * @return
	 */
	public void getLocalNews() {
		db = events.getReadableDatabase();
		// 取出数据库用户表
		Cursor c = db.rawQuery("select * from gongGao ", null);
		// 如果有记录
		while (c.moveToNext()) {
			ErpgongGao n = new ErpgongGao();
			int id = c.getInt(c.getColumnIndex("id"));
			String titleStr = c.getString(c.getColumnIndex("titleStr"));
			String typeStr = c.getString(c.getColumnIndex("typeStr"));
			String contentStr = c.getString(c.getColumnIndex("contentStr"));
			String userName = c.getString(c.getColumnIndex("userName"));
			String yiJieShouRen = c.getString(c.getColumnIndex("yiJieShouRen"));
			String chuanYueYiJian = c.getString(c.getColumnIndex("chuanYueYiJian"));
			String  isTop = c.getString(c.getColumnIndex("isNew"));
			
		
			n.setId(id);
			n.setTitleStr(titleStr);
			n.setTypeStr(typeStr);
			n.setContentStr(contentStr);
			n.setUserName(userName);
			n.setYiJieShouRen(yiJieShouRen);
			n.setChuanYueYiJian(chuanYueYiJian);
			n.setIsTop(isTop);
			localNewsList.add(n);
		}
		c.close();
		db.close();
	}

	/**
	 * 加载更多触发类
	 */
	class loadMoreOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			loadMoreButton.setText("正在加载中...");
			Handler handlerload = new Handler();
			handlerload.postDelayed(loadThread, 3000);
		}

	}

	/**
	 * 加载更多线程
	 */
	Runnable loadThread = new Runnable() {

		@Override
		public void run() {

			adapter.notifyDataSetChanged();
			loadMoreButton.setText("点击查看更多...");
		}

	};
	/**
	 * 退出按钮
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
	}
	/**
	 * Handler接收返回值
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			progressDialog.dismiss();
			if (bundle != null) {
				// System.out.println("有消息");
				List<ErpgongGao> listFieldlist = new ArrayList<ErpgongGao>();
				// 取出员工信息
				listFieldlist = (List<ErpgongGao>) bundle.getSerializable("newsList");
				System.out.println("新闻个数" + listFieldlist.size());
				// 如果没有新闻，显示相应消息
				if (listFieldlist.size() == 0) {
					hasdata_TextView.setVisibility(View.VISIBLE);
				}
				// System.out.println(listFieldlist.size());
				// 生成适配器信息（通过分页类）
				adapter = new NewsPagination(News_List.this, listFieldlist);
				// 将信息填充到ListView上
				notice_ListView.setAdapter(adapter);
			} else {
			}
		}
	};

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + this.visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = adapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
		}
	}

	/**
	 * List项触发类
	 */
	class ListViewItemSelectListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> listview, View view,
				int indexint, long indexlong) {
//			 ListField Item = (ListField)
//			 listview.getAdapter().getItem(indexint);
			ErpgongGao Item = (ErpgongGao) listview.getAdapter().getItem(indexint);
			Bundle bundle = new Bundle();
			bundle.putSerializable("data", Item);
			Intent intent = new Intent();
			intent.putExtra("bundle", bundle);
			intent.setClass(News_List.this, News_Detail.class);
			News_List.this.startActivity(intent);
			db1=events.getWritableDatabase();
			ContentValues values=new ContentValues();
			values.put("isNew", 0);
			db1.update("gongGao", values, "id=?", new String[]{Item.getId().toString()});
			db1.close();
		}

	}

}
