package com.org.palmcampus.email;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;

import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DialerFilter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import com.org.palmcampus.R;
import com.org.palmcampus.email.adapter.EmailInBoxPagination;
import com.org.palmcampus.email.bean.Email;
import com.org.palmcampus.oa.custom.Config;
import com.org.palmcampus.oa.service.MailService;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;



public class MailBoxActivity extends Activity  implements OnScrollListener{

	// private ArrayList<Email> mailslist = new ArrayList<Email>();
	private ArrayList<ArrayList<InputStream>> attachmentsInputStreamsList = new ArrayList<ArrayList<InputStream>>();
	private String type;
	// 邮件状态（所有，已读和未读）
	private int status;
	private EmailInBoxPagination myAdapter;
	private ListView lv_box;
	

	// 未读邮件集合
	private List<Email> mailslist = new ArrayList<Email>();
	// 新邮件集合
	private List<Email> listFieldlist = new ArrayList<Email>();
	// 本地邮件集合
	private List<Email> localMailList = new ArrayList<Email>();
	private ProgressDialog progressDialog;

	private List<String> messageids;
	// 数据库
	private SQLiteDatabase db;
	private static EventData events;
	private int visibleLastIndex = 0;
	private int visibleItemCount;
	// 发件人姓名
	String userName = "";
	// 最大邮件ID号
	int messageId = 0;
	public static final String BROADCASTACTION = "com.sc.broad3";
	/**
	 * Handler接收返回值
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			progressDialog.dismiss();
			// 如果连接服务器正常
			if (bundle != null) {
				System.out.println("连接正常。。。。。");
				// List<Email> listFieldlist = new ArrayList<Email>();
				// 取出新邮件
				listFieldlist = (List<Email>) bundle
						.getSerializable("emailList");
				// 如果查询的是未读邮件，把已读的过滤掉
				if (status == 1) {
//					Toast.makeText(MailBoxActivity.this, "未读邮件",
//							Toast.LENGTH_SHORT).show();
					myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
							mailslist);
					// 将未读邮件个数存储到本地数据库里
					ContentValues values = new ContentValues();
					values.put("count", mailslist.size());
					System.out.println("未读邮件个数" + mailslist.size());
					getContentResolver().insert(Config.NOTREADEMAILURI, values);
					initView();
				} else if (status == 2) {
//					Toast.makeText(MailBoxActivity.this, "已读邮件",
//							Toast.LENGTH_SHORT).show();
					myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
							mailslist);
					initView();
				}
				// 如果是查询所有邮件
				else {
					// System.out.println("新邮件个数" + listFieldlist.size());
					// Toast.makeText(MailBoxActivity.this,
					// "你有" + listFieldlist.size() + "封新邮件",
					// Toast.LENGTH_SHORT).show();
					// // 将新邮件保存到本地数据库
					// for (Email e : listFieldlist) {
					// ContentValues values = new ContentValues();
					// values.put("messageid", e.getEmailID());
					// values.put("title", e.getTitle());
					// values.put("mailfrom", e.getFrom());
					// values.put("mailto", e.getTo());
					// values.put("cc", e.getCc());
					// values.put("sendDate", e.getSentdate());
					// values.put("content", e.getContent());
					// values.put("attachment", e.getAttachments());
					// getContentResolver().insert(allEmailUri, values);
					// }
					// // 将本地邮件和网上查收的新邮件合并
					// // localMailList.addAll(listFieldlist);
					// listFieldlist.addAll(localMailList);
					// for(Email ee:localMailList){
					// System.out.println(ee.getEmailID());
					// System.out.println(ee.getFrom());
					// System.out.println(ee.getSentdate());
					// System.out.println(ee.getTitle());
					// System.out.println(ee.getTo());
					// //System.out.println(ee.getAttachments());
					// }
					System.out.println("查询所有邮件" + localMailList.size());
					myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
							localMailList);
					initView();
				}
				// 菜单
				registerForContextMenu(lv_box);
				// 生成适配器信息（通过分页类）
				// initView();
			} else { // 如果只是取

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(this);
	     //定义NotificationManager
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager)getSystemService(ns);
		 //清除邮件通知
		 mNotificationManager.cancel(0);
		 
		status = getIntent().getIntExtra("status", 0);
		System.out.println("%%%%%%%" + status + "%%%%%%%");
		setContentView(R.layout.email_mailbox);
		//获取标题Textview
		TextView title_textview=(TextView)findViewById(R.id.mail_box_title);
		// 取出数据库的值
		events = new EventData(MailBoxActivity.this);
		db = events.getReadableDatabase();
		// 取出数据库用户表
		Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);

		// 如果有记录
		if (ResultSet.moveToNext()) {
			userName = ResultSet
					.getString(ResultSet.getColumnIndex("trueName"));
			System.out.println(userName);
		}
		// 关闭数据库
		ResultSet.close();
		db.close();
		// 去数据库查询，查询所有本地邮件的id
		messageids = getAllMessageids();
		switch (status) {
		case 0:// 查询全部
				// getAllMails();
			getLocalMails();
			System.out.println("查询所有邮件咯咯咯咯" + localMailList.size());
			myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
					localMailList);
			initView();
			break;
		case 1:// 未读邮件
			title_textview.setText("未读邮件");
			getLocalMails();
			getNotRead();			
			myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
					mailslist);
			// 将未读邮件个数存储到本地数据库里
			ContentValues values = new ContentValues();
			values.put("count", mailslist.size());
			System.out.println("未读邮件个数" + mailslist.size());
			getContentResolver().insert(Config.NOTREADEMAILURI, values);
			initView();
			break;
		case 2:// 查询已读
			title_textview.setText("已读邮件");
			getLocalMails();
			getYesRead();	
			myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
					mailslist);
			initView();
			break;
		}
		//注册邮件菜单
		registerForContextMenu(lv_box);

		

	}

	private void initView() {
		lv_box = (ListView) findViewById(R.id.lv_box);
		lv_box.setAdapter(myAdapter);
		lv_box.setOnItemClickListener(new ListViewItemSelectListener());
		lv_box.setOnScrollListener(this);

	}

	/**
	 * 查询所有本地邮件
	 * 
	 * @return
	 */
	public void getLocalMails() {
		System.out.println("开始查询本地邮件");
		Cursor c = getContentResolver().query(Config.ALLEMAILURI, null, null, null,
				"messageid desc");

		// 如果邮件表里有邮件
		while (c.moveToNext()) {
			Email e = new Email();
			messageId = c.getInt(c.getColumnIndex("messageid"));
			String title = c.getString(c.getColumnIndex("title"));
			String mailfrom = c.getString(c.getColumnIndex("mailfrom"));
			String mailto = c.getString(c.getColumnIndex("mailto"));
			String cc = c.getString(c.getColumnIndex("cc"));
			String sendDate = c.getString(c.getColumnIndex("sendDate"));
			String content = c.getString(c.getColumnIndex("content"));
			String attachment = c.getString(c.getColumnIndex("attachment"));
			int isnew = c.getInt(c.getColumnIndex("isNew"));
			// System.out.println(attachment);
			e.setEmailID(messageId);
			e.setTitle(title);
			e.setFrom(mailfrom);
			e.setTo(mailto);
			e.setCc(cc);
			e.setSentdate(sendDate);
			e.setContent(content);
			e.setAttachments(attachment);
			e.setIsnew(isnew);
			localMailList.add(e);
		
		}
		c.close();
	}

	/**
	 * 查询所有邮件
	 * 
	 * @return
	 */
	public void getAllMails() {
		getLocalMails();
		// c.close();
		if (messageId != 0) {
			messageId = localMailList.get(0).getEmailID();
			// 找出最大messageID
			for (int i = 0; i < localMailList.size(); i++) {

				if (messageId < localMailList.get(i).getEmailID())
					messageId = localMailList.get(i).getEmailID();
			}
		}
		System.out.println("最大的ID是" + messageId);

	}

	/**
	 * 查询出已读邮件
	 * 
	 * @return
	 */
	private List<String> getAllMessageids() {
		List<String> messageids = new ArrayList<String>();
		Cursor c = getContentResolver().query(Config.EMAILSTATUS_URI, null, null, null, null);
		while (c.moveToNext()) {
			messageids.add(c.getString(2));
		}
		c.close();
		return messageids;
	}

	/**
	 * List项触发类
	 */
	class ListViewItemSelectListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> listview, View view,
				int indexint, long indexlong) {
			Email item = (Email) listview.getAdapter().getItem(indexint);
			System.out.println("点击项事件触发");
			// 如果是未读邮件，更新邮件为已读，并且发送广播，未读图标-1
			if (item.getIsnew() == 1) {
				ContentValues cv = new ContentValues();
				cv.put("isNew", 0);
				getContentResolver().update(Config.ALLEMAILURI, cv, "messageid=?",

				new String[] { String.valueOf(item.getEmailID()) });
				Intent intent = new Intent();
				intent.setAction(BROADCASTACTION);
				intent.putExtra("readone", 1);
				
				sendBroadcast(intent);
				System.out.println("广播发送成功");
			}
			// //点击表示已读把ID存入数据库
			String mailID = String.valueOf(item.getEmailID());
			if (!messageids.contains(mailID)) {
				ContentValues values = new ContentValues();
				values.put("mailfrom", item.getFrom());
				values.put("messageid", mailID);
				getContentResolver().insert(Config.EMAILSTATUS_URI, values);
				System.out.println("成功将点击邮件放入已读邮件数据表里");

			}
			// 进入邮件详细信息界面
			final Intent intent = new Intent(MailBoxActivity.this,
					MailContentActivity.class).putExtra("EMAIL", item);
			startActivity(intent);

		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.visibleItemCount = visibleItemCount;
		visibleLastIndex = firstVisibleItem + this.visibleItemCount - 1;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		int itemsLastIndex = myAdapter.getCount() - 1; // 数据集最后一项的索引
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// 如果是自动加载,可以在这里放置异步加载数据的代码
		}
	}


	/**
	 * 查询未读
	 */
	private void getNotRead() {
		System.out.println("开始过滤未读邮件");
		System.out.println(messageids);
		progressDialog = new ProgressDialog(MailBoxActivity.this);
		progressDialog.setMessage("正在读取，请稍等......");
		progressDialog.setIndeterminate(true);
		progressDialog.show();
		for (Email mailReceiver : localMailList) {
			System.out.println(mailReceiver.getEmailID());
			Email email = new Email();
			try {
				if (messageids.contains(String.valueOf(mailReceiver
						.getEmailID()))) {
					System.out.println("过滤到了");
					continue;
				}
				email.setEmailID(mailReceiver.getEmailID());
				email.setFrom(mailReceiver.getFrom());
				email.setTo(mailReceiver.getTo());
				email.setCc(mailReceiver.getCc());
				email.setBcc(mailReceiver.getBcc());
				email.setTitle(mailReceiver.getTitle());
				email.setSentdate(mailReceiver.getSentdate());
				email.setContent(mailReceiver.getContent());
				email.setAttachments(mailReceiver.getAttachments());
				email.setCharset(mailReceiver.getCharset());
				email.setIsnew(mailReceiver.getIsnew());
				mailslist.add(email);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		handler.sendEmptyMessage(0);
		System.out.println("发送消息成功");
	}

	//
	/**
	 * 查询已读
	 */
	private void getYesRead() {
		progressDialog = new ProgressDialog(MailBoxActivity.this);
		progressDialog.setMessage("正在读取，请稍等......");
		progressDialog.setIndeterminate(true);
		progressDialog.show();
		for (Email mail : localMailList) {
			Email email = new Email();
			try {
				if (messageids.contains(String.valueOf(mail.getEmailID()))) {
					email.setEmailID(mail.getEmailID());
					email.setFrom(mail.getFrom());
					email.setTo(mail.getTo());
					email.setCc(mail.getCc());
					email.setBcc(mail.getBcc());
					email.setTitle(mail.getTitle());
					email.setSentdate(mail.getSentdate());
					email.setContent(mail.getContent());
					email.setAttachments(mail.getAttachments());
				
					mailslist.add(email);

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		handler.sendEmptyMessage(0);
		System.out.println("发送消息成功");
	}

	// 返回
	public void back(View v) {
		finish();
		Intent intent=new Intent(this, Email_Home.class);
		intent.putExtra("frommail", true);
		startActivity(intent);
//		Intent intent=new Intent(this, Email_Home.class);
//		intent.putExtra("frommail", true);
//		startActivity(intent);
		
	}

//	// 菜单，删除某一个邮件
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		getMenuInflater().inflate(R.menu.mailbox_menu, menu);
//		super.onCreateContextMenu(menu, v, menuInfo);
//	}

//	/**
//	 * 长按事件
//	 */
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
////		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
////				.getMenuInfo();
////		int id = (int) info.id;
////		System.out.println("id" + id);
//		int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
//		System.out.println("您点了第" + selectedPosition+"项");
//		switch (item.getItemId()) {
//
//		case R.id.delete:
//			System.out.println(localMailList.get(selectedPosition).getTitle());
//			deleteMail(localMailList.get(selectedPosition).getEmailID(),selectedPosition);
//			// break;
//		}
//		return super.onContextItemSelected(item);
//	}

//	// 删除某一封邮件
//	private void deleteMail(final int id,final int position) {
//		AlertDialog.Builder builder = new Builder(MailBoxActivity.this);
//		builder.setMessage("你确定要删除数据");
//		builder.setPositiveButton("确定", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				System.out.println("删除的邮件ID"+id);
//				getContentResolver().delete(allEmailUri, "messageid=?",
//						new String[] { String.valueOf(id) });
//				
//				// list = getAllConstacts();
//				localMailList.remove(position);
//				myAdapter.notifyDataSetChanged();
//				//initView();
//				Toast.makeText(MailBoxActivity.this, "删除邮件成功",
//						Toast.LENGTH_SHORT).show();
//				//删除服务器上该邮件（实际是删除该邮件对应的联系人）
//				Bundle bundle = new Bundle();
//				bundle.putInt("messageId", id);
//				bundle.putString("userName", userName);
//				Def_Thread thread = new Def_Thread(handler, bundle,
//						MethodName.DELETEMAIL);
//				thread.start();
//			}
//		});
//		builder.setNegativeButton("取消", null);
//		builder.show();
//	}
	
}
