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

//Ա����Ϣ�б����
public class News_List extends Activity implements OnScrollListener {

	private TextView title_TextView, hasdata_TextView;
	private ListView notice_ListView;
	private Button loadMoreButton;
	private NewsPagination adapter;
	private ProgressDialog progressDialog;
	private int visibleLastIndex = 0;
	private int visibleItemCount;
	// // ���ݿ�
	private SQLiteDatabase db,db1;
	private static EventData events;
	private List<ErpgongGao> localNewsList = new ArrayList<ErpgongGao>();
	// private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_list);
		SysExitUtil.activityList.add(this);
		  //����NotificationManager
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager)getSystemService(ns);
		 //����ʼ�֪ͨ
		 mNotificationManager.cancel(1);
		// ���ذ�ť
		Button return_Button = (Button) findViewById(R.id.title_setting_Btn);
		return_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				News_List.this.finish();
			}

		});

		title_TextView = (TextView) findViewById(R.id.title_Text_TV);
		title_TextView.setText("֪ͨ����");
		hasdata_TextView = (TextView) findViewById(R.id.tv_hasdata);
		notice_ListView = (ListView) findViewById(R.id.view_list_LV);
		
		// // ȡ�����ݿ��ֵ
		events = new EventData(News_List.this);
//		
		getLocalNews();
		if (localNewsList.size() == 0) {
			hasdata_TextView.setVisibility(View.VISIBLE);
		}
		// System.out.println(listFieldlist.size());
		// ������������Ϣ��ͨ����ҳ�ࣩ
		adapter = new NewsPagination(News_List.this, localNewsList);
		// ����Ϣ��䵽ListView��
		notice_ListView.setAdapter(adapter);
		notice_ListView
				.setOnItemClickListener(new ListViewItemSelectListener());
		notice_ListView.setOnScrollListener(this);

	}

	/**
	 * ��ѯ���б�������
	 * 
	 * @return
	 */
	public void getLocalNews() {
		db = events.getReadableDatabase();
		// ȡ�����ݿ��û���
		Cursor c = db.rawQuery("select * from gongGao ", null);
		// ����м�¼
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
	 * ���ظ��ഥ����
	 */
	class loadMoreOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			loadMoreButton.setText("���ڼ�����...");
			Handler handlerload = new Handler();
			handlerload.postDelayed(loadThread, 3000);
		}

	}

	/**
	 * ���ظ����߳�
	 */
	Runnable loadThread = new Runnable() {

		@Override
		public void run() {

			adapter.notifyDataSetChanged();
			loadMoreButton.setText("����鿴����...");
		}

	};
	/**
	 * �˳���ť
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
	}
	/**
	 * Handler���շ���ֵ
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			progressDialog.dismiss();
			if (bundle != null) {
				// System.out.println("����Ϣ");
				List<ErpgongGao> listFieldlist = new ArrayList<ErpgongGao>();
				// ȡ��Ա����Ϣ
				listFieldlist = (List<ErpgongGao>) bundle.getSerializable("newsList");
				System.out.println("���Ÿ���" + listFieldlist.size());
				// ���û�����ţ���ʾ��Ӧ��Ϣ
				if (listFieldlist.size() == 0) {
					hasdata_TextView.setVisibility(View.VISIBLE);
				}
				// System.out.println(listFieldlist.size());
				// ������������Ϣ��ͨ����ҳ�ࣩ
				adapter = new NewsPagination(News_List.this, listFieldlist);
				// ����Ϣ��䵽ListView��
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
		int itemsLastIndex = adapter.getCount() - 1; // ���ݼ����һ�������
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// ������Զ�����,��������������첽�������ݵĴ���
		}
	}

	/**
	 * List�����
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
