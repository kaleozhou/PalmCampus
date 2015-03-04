package com.org.palmcampus.oa.page;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.service.SysExitService;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;

import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ConfigActivity extends Activity {

	// ���ݿ�
	private SQLiteDatabase db;
	private static EventData events;
	private String username;

	// service ������
	ServiceConnection conn = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_config_home);
		// ��ȡ���ݿ�
		events = new EventData(ConfigActivity.this);
		db = events.getReadableDatabase();
		// ȡ�����ݿ��û���
		Cursor c = db.rawQuery("select * from Sys_Users", null);
		// ����м�¼
		if (c.moveToNext()) {
			username = c.getString(c.getColumnIndex("userName"));

		}
//		Button change_btn=(Button)findViewById(R.id.config_changepwd);
//		change_btn.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(ConfigActivity.this, Config_Password.class);
//				startActivity(intent);
//			}
//		});
		

	}
	/**
	 * �Ƴ���ť
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}

}
