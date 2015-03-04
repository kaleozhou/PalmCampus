package com.org.palmcampus.email;

import java.util.ArrayList;
import java.util.List;

import com.org.palmcampus.R;
import com.org.palmcampus.email.bean.EmailCaogao;
import com.org.palmcampus.oa.custom.Config;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MailCaogaoxiangActivity extends Activity {
	private ListView lv;
	private List<EmailCaogao> allcaogaos;
	private MyAdapter adapter;
	// 员工姓名
	String userName = "";

	// 数据库
	private SQLiteDatabase db;
	private static EventData events;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(this);
		setContentView(R.layout.email_caogaoxiang);
		allcaogaos = getAllcaogaos();
		lv = (ListView) findViewById(R.id.caogaoxiang);
		// 取出数据库的值
		events = new EventData(MailCaogaoxiangActivity.this);
		db = events.getReadableDatabase();
		// 取出数据库用户表
		Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);

		// 如果有记录
		if (ResultSet.moveToNext()) {
			userName = ResultSet
					.getString(ResultSet.getColumnIndex("userName"));
			System.out.println(userName);
		}
		db.close();
		adapter = new MyAdapter();
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EmailCaogao caogao = allcaogaos.get(position);
				Intent intent = new Intent(MailCaogaoxiangActivity.this,
						MailEditActivity.class);
				intent.putExtra("mailid", caogao.getId());
				startActivity(intent);
				finish();

			}

		});
	}

	/**
	 * 获取所有草稿
	 * 
	 * @return
	 */
	private List<EmailCaogao> getAllcaogaos() {
		List<EmailCaogao> caogaos = new ArrayList<EmailCaogao>();
		System.out.println("开始查询草稿箱");
		Cursor c = getContentResolver().query(Config.CAOGAO_URI, null, null,
				null, null);
		//System.out.println(c.moveToNext());
		while (c.moveToNext()) {
			System.out.println("草稿箱有内容");
			EmailCaogao caogao = new EmailCaogao(c.getInt(0), c.getString(1),
					c.getString(2), c.getString(3), c.getString(4),c.getString(5));
			caogaos.add(caogao);
		}
		c.close();
		return caogaos;
	};

	/**
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return allcaogaos.size();
		}

		@Override
		public Object getItem(int position) {
			return allcaogaos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = View.inflate(MailCaogaoxiangActivity.this,
					R.layout.email_caogaoxiang_item, null);
			TextView mailto = (TextView) item.findViewById(R.id.tv_mailto);
			TextView mailsubject = (TextView) item
					.findViewById(R.id.tv_mailsubject);

			EmailCaogao caogao = allcaogaos.get(position);
			mailto.setText(caogao.getMailto());
			mailsubject.setText(caogao.getSubject());

			return item;
		}

	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}
	//清空草稿箱所有内容
	public void clearAll(View v){
	
		getContentResolver().delete(Config.CAOGAO_URI, null,
				null);

		getContentResolver().delete(Config.ATTACHMENT, null,
				null);
		Toast.makeText(this, "草稿箱已经全部清空", Toast.LENGTH_LONG).show();
		finish();
	}

}
