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

//�ƶ��칫������
public class PersonalActivity extends Activity {

	private TextView title_TextView;
	// ���ƾŹ���ؼ�����ʾ���ֺ�ͼƬ
	private GridView personal_GridView;
	// ���ܹ㲥����
	BroadcastMain broadcastMain;
	// ��֪ͨ����
	int newscount = 0;
	// ����δ����������
	int not_read_count;
	int totalcount = 0;
	// ���ݿ�
	private SQLiteDatabase db;
	private static EventData events;

	
	// ����ͼƬ
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
	// private String[] personal_GridView_name_item = { "��������", "�ճ̰���", "��������",
	// "��Ϣ��̬", "֪ͨ����", "ͨѶ¼", "�ҵ��ļ�", "�ҵ�����", "�����ռ�" };

	private String[] personal_GridView_name_item = { "֪ͨ����", "�ճ̰���", "�γ̱�",
			"ͼ�����", "����ѡ��", "�ɼ���ѯ","�ҵ���ҵ" ,"��ѧ��Ϣ","��������"
//			,"��У����"
			};

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("�˳�������Ϣ�㲥");
		Intent intent = new Intent();
		intent.setClass(this, NewsService.class);
		// ֹͣ���ʼ�����
		stopService(intent);
		// ע���㲥
		unregisterReceiver(broadcastMain);

	}

	// ��ȡ����δ��������
	private int getNotReadCount() {
		int count = 0;
		// ȡ�����ݿ��ֵ
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
	 * ҳ���ʼ��
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_personal);
		// �������ŷ���
		Intent intent = new Intent(this, NewsService.class);
		startService(intent);
		broadcastMain = new BroadcastMain();
		IntentFilter filter = new IntentFilter();
		filter.addAction(NewsService.BROADCASTACTION);
		// ע��㲥������
		registerReceiver(broadcastMain, filter);
		// ��ȡ����δ����������
		//not_read_count = getNotReadCount();
		title_TextView = (TextView) findViewById(R.id.title_Text_TV);
		title_TextView.setText("�ǻ�У԰");

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
				// ֪ͨ����
				case 0:
					Intent_Context(PersonalActivity.this, News_List.class);

					break;
	
				}
			}

		});
		
	}

	/**
	 * GridView������
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
	protected void onResume() {// ���������淵��ʱ�����onResume������GridView
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println("����������");
		personal_GridView.setAdapter(getGridViewAdapter(
				personal_GridView_name_item, personal_GridView_img_item));
	}
	/**
	 * �Ƴ���ť
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
	}
	/**
	 * ҳ����ת
	 */
	public void Intent_Context(Context front_context, Class<?> behind_context) {
		Intent intent = new Intent();
		intent.setClass(front_context, behind_context);
		// intent.putExtra("methodname", methodname);
		front_context.startActivity(intent);
	}

	// ��дSimpleAdapter������ͼ��
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
	
			if (position == 0) { // ֪ͨ����
				// ��ȡ֪ͨ���������������ַ�ʽ��ʾ
				// int not_read_count=getNotReadCount();

				
					View news_view = view.findViewById(R.id.menu_item_RL);
					BadgeView badge_news = new BadgeView(getApplicationContext(),
							news_view);
					not_read_count=getNotReadCount();
				// �������Ϣ
				
					if (not_read_count != 0) {
						badge_news.setText(String.valueOf(not_read_count));
						badge_news.show();
					}
			}
			return view;
		}
	}

	// �㲥������
	class BroadcastMain extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			System.out.println("��ʼ��������");
			int count = intent.getIntExtra("newNewsCount", 0);

			if (count > 0) {
				System.out.println("������");
				newscount = count;
//				Toast.makeText(getApplicationContext(), "�յ�"+count+"����ͨ��", 5000).show();
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