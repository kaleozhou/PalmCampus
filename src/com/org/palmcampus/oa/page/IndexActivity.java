package com.org.palmcampus.oa.page;
import java.util.List;



import com.org.palmcampus.R;
import com.org.palmcampus.email.Email_Home;
import com.org.palmcampus.email.MailBoxActivity;
import com.org.palmcampus.email.bean.Email;
import com.org.palmcampus.oa.custom.Config;
import com.org.palmcampus.oa.service.MailService;
import com.org.palmcampus.oa.service.SysExitService;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;
import com.readystatesoftware.viewbadger.BadgeView;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

public class IndexActivity extends TabActivity {

	// ���ݿ�
	private SQLiteDatabase db;
	private static EventData events;
	private String username;
	BroadcastMain broadcastMain;
	BroadcastMain2 broadcastMain2;
	public static final String BROADCASTACTION = "com.org.palmcampus.oa.broadcastMaun.broad3";

	private TabHost tabHost;
	private TabSpec spec;
	private Intent intent;
	// ��ȡ����δ���ʼ�����
	private int getNotReadCount() {
		int count = 0;
		Cursor c = getContentResolver().query(Config.ALLEMAILURI, null, "isNew=1",
				null, null);
		while (c.moveToNext()) {
			count++;
		}
		c.close();
		return count;
	}
	RadioButton rb, rb2, rb3;
	BadgeView badge;
	// ����δ���ʼ�����
	int not_read_count = 0;
	// ��ʾδ���ʼ����������غ�����ģ�
	int totalcount = 0;

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

		setContentView(R.layout.activity_index);
		// ��ȡ���ݿ�
		events = new EventData(IndexActivity.this);
		db = events.getReadableDatabase();
		// ȡ�����ݿ��û���
		Cursor c = db.rawQuery("select * from Sys_Users", null);
		// ����м�¼
		if (c.moveToNext()) {
			username = c.getString(c.getColumnIndex("userName"));

		}
		// �����ǲ��Ǵ��ʼ����淵��
		 boolean flag = getIntent().getBooleanExtra("frommail", false);
		 		// �����ʼ�����
				Intent intent = new Intent(this, MailService.class);
				// �����ʼ����񣬿�ʼ��̨��ʱ�������ʼ�
//				bindService(intent, conn,Context.BIND_AUTO_CREATE );
				startService(intent);
				broadcastMain = new BroadcastMain();
				IntentFilter filter = new IntentFilter();
				filter.addAction(MailService.BROADCASTACTION);
				// ע��㲥������
				registerReceiver(broadcastMain, filter);
				// //�ù㲥���������������ʼ�
				broadcastMain2 = new BroadcastMain2();
				IntentFilter filter2 = new IntentFilter();
				filter2.addAction(BROADCASTACTION);
				// ע��㲥������
				registerReceiver(broadcastMain2, filter2);
				// ��ȡ����δ���ʼ�����
				not_read_count = getNotReadCount();
		tabHost = getTabHost();

		// �ƶ��칫
		intent = new Intent(IndexActivity.this, PersonalActivity.class);
		spec = tabHost.newTabSpec("personal");
		spec.setIndicator("");
		spec.setContent(intent);
		tabHost.addTab(spec);

		// �ڲ��ʼ�
		intent = new Intent(IndexActivity.this, Email_Home.class);
		spec = tabHost.newTabSpec("email");
		spec.setIndicator("");
		spec.setContent(intent);
		tabHost.addTab(spec);

		// ϵͳ����
		intent = new Intent(IndexActivity.this, ConfigActivity.class);
		spec = tabHost.newTabSpec("config");
		spec.setIndicator("");
		spec.setContent(intent);
		tabHost.addTab(spec);
		tabHost.setCurrentTab(0);
		
		TabWidget tabs = (TabWidget) findViewById(android.R.id.tabs);
		View badge_new=(View)findViewById(R.id.home_letter_RB);
		badge = new BadgeView(this, badge_new);
		badge.setText(String.valueOf(not_read_count));
		badge.show();

		

		// ����ʹ��
		rb = (RadioButton) findViewById(R.id.home_letter_RB);
		rb2 = (RadioButton) findViewById(R.id.home_personal_RB);
		rb3 = (RadioButton) findViewById(R.id.home_config_RB);

		rb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				rb2.setChecked(false);

				rb3.setChecked(false);
				System.out.println("������ʷ�ٷ�ɭ����ٹ�");
				tabHost.setCurrentTab(1);

				rb.setChecked(true);

			}
		});
		rb2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				rb.setChecked(false);

				rb3.setChecked(false);

				rb2.setChecked(true);
				System.out.println("������ʷ�ٷ�ɭ����ٹ�");
				tabHost.setCurrentTab(0);

			}
		});
		rb3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				rb.setChecked(false);

				rb2.setChecked(false);
				System.out.println("������ʷ�ٷ�ɭ����ٹ�");
				tabHost.setCurrentTab(2);

				rb3.setChecked(true);

			}
		});

	}
	/**
	 * �Ƴ���ť
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
	}
	private void addTab(String label, int drawableId, Class cls) {
		Intent intent = new Intent(this, cls);
		TabHost.TabSpec spec = tabHost.newTabSpec(label);

		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab,
				getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(label);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
	// �㲥������
	class BroadcastMain extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			int count = intent.getIntExtra("newMailCount", 0);
			List<Email> localMailList = (List<Email>) intent
					.getSerializableExtra("mailList");
			// System.out.println("�����ʼ�����" + localMailList.size());

			System.out.println("+++++++" + count);
			Message message = new Message();
			message.what = 0;
			Bundle data = new Bundle();
			data.putInt("count", count);
			message.setData(data);
			handler.sendMessage(message);
			
		}

	}
	
	// handler���ܷ���ֵ�����½�����Ϣ����Ϣͼ�꣩
		Handler handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 0) {
					System.out.println("good!!!!!!!!!!!!");
					// ��ȡ���ʼ�����
					int count = msg.getData().getInt("count");
//					
					// int totalcount = 0;
					// ����Ѿ�������
					if (!badge.getText().toString().equals("")) {
						int oldcount = Integer.parseInt(badge.getText().toString());
						// ������+������
						totalcount = oldcount + count;
						badge.setText(String.valueOf(totalcount));
					} 
					else 
					{
						totalcount = count;
						badge.setText(String.valueOf(totalcount));
					}
					// ���ͼ������ʾ
					if (badge.isShown()) {
						System.out.println("increment");
						badge.setText(String.valueOf(totalcount));
				
					} 
					else 
					{// ���ͼ��û��ʾ�����������ʼ�������ʾͼ��
						if (!badge.getText().toString().equals(""))
							badge.show();
					}

				}
				
			};
		};
		
		// �㲥������,ÿ��һ���ʼ���֪ͨͼ������-1
		class BroadcastMain2 extends BroadcastReceiver {

			@Override
			public void onReceive(Context arg0, Intent intent) {
				System.out.println("++++++++++++++++�й㲥�ˣ�����������");
				int result = intent.getIntExtra("readone", 0);
				System.out.println("++++++++++" + result);
				// System.out.println("++++++++++"+totalcount);
				if (badge.isShown()) {

					int oldcount = Integer.parseInt(badge.getText().toString());
					badge.setText(String.valueOf(oldcount - result));
					 badge.setBadgePosition(BadgeView.POSITION_TOP_LEFT);
					 badge.show();
				} else {// ���ͼ��û��ʾ�����������ʼ�������ʾͼ��
					if (!badge.getText().toString().equals(""))
						badge.show();
				}
			}

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
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(IndexActivity.this, SysExitService.class);
		intent.putExtra("username", username);
		bindService(intent, conn, BIND_AUTO_CREATE);
		super.onDestroy();
	}

}