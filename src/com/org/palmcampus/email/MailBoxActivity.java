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
	// �ʼ�״̬�����У��Ѷ���δ����
	private int status;
	private EmailInBoxPagination myAdapter;
	private ListView lv_box;
	

	// δ���ʼ�����
	private List<Email> mailslist = new ArrayList<Email>();
	// ���ʼ�����
	private List<Email> listFieldlist = new ArrayList<Email>();
	// �����ʼ�����
	private List<Email> localMailList = new ArrayList<Email>();
	private ProgressDialog progressDialog;

	private List<String> messageids;
	// ���ݿ�
	private SQLiteDatabase db;
	private static EventData events;
	private int visibleLastIndex = 0;
	private int visibleItemCount;
	// ����������
	String userName = "";
	// ����ʼ�ID��
	int messageId = 0;
	public static final String BROADCASTACTION = "com.sc.broad3";
	/**
	 * Handler���շ���ֵ
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			
			super.handleMessage(msg);
			Bundle bundle = msg.getData();
			progressDialog.dismiss();
			// ������ӷ���������
			if (bundle != null) {
				System.out.println("������������������");
				// List<Email> listFieldlist = new ArrayList<Email>();
				// ȡ�����ʼ�
				listFieldlist = (List<Email>) bundle
						.getSerializable("emailList");
				// �����ѯ����δ���ʼ������Ѷ��Ĺ��˵�
				if (status == 1) {
//					Toast.makeText(MailBoxActivity.this, "δ���ʼ�",
//							Toast.LENGTH_SHORT).show();
					myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
							mailslist);
					// ��δ���ʼ������洢���������ݿ���
					ContentValues values = new ContentValues();
					values.put("count", mailslist.size());
					System.out.println("δ���ʼ�����" + mailslist.size());
					getContentResolver().insert(Config.NOTREADEMAILURI, values);
					initView();
				} else if (status == 2) {
//					Toast.makeText(MailBoxActivity.this, "�Ѷ��ʼ�",
//							Toast.LENGTH_SHORT).show();
					myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
							mailslist);
					initView();
				}
				// ����ǲ�ѯ�����ʼ�
				else {
					// System.out.println("���ʼ�����" + listFieldlist.size());
					// Toast.makeText(MailBoxActivity.this,
					// "����" + listFieldlist.size() + "�����ʼ�",
					// Toast.LENGTH_SHORT).show();
					// // �����ʼ����浽�������ݿ�
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
					// // �������ʼ������ϲ��յ����ʼ��ϲ�
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
					System.out.println("��ѯ�����ʼ�" + localMailList.size());
					myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
							localMailList);
					initView();
				}
				// �˵�
				registerForContextMenu(lv_box);
				// ������������Ϣ��ͨ����ҳ�ࣩ
				// initView();
			} else { // ���ֻ��ȡ

			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(this);
	     //����NotificationManager
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager)getSystemService(ns);
		 //����ʼ�֪ͨ
		 mNotificationManager.cancel(0);
		 
		status = getIntent().getIntExtra("status", 0);
		System.out.println("%%%%%%%" + status + "%%%%%%%");
		setContentView(R.layout.email_mailbox);
		//��ȡ����Textview
		TextView title_textview=(TextView)findViewById(R.id.mail_box_title);
		// ȡ�����ݿ��ֵ
		events = new EventData(MailBoxActivity.this);
		db = events.getReadableDatabase();
		// ȡ�����ݿ��û���
		Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);

		// ����м�¼
		if (ResultSet.moveToNext()) {
			userName = ResultSet
					.getString(ResultSet.getColumnIndex("trueName"));
			System.out.println(userName);
		}
		// �ر����ݿ�
		ResultSet.close();
		db.close();
		// ȥ���ݿ��ѯ����ѯ���б����ʼ���id
		messageids = getAllMessageids();
		switch (status) {
		case 0:// ��ѯȫ��
				// getAllMails();
			getLocalMails();
			System.out.println("��ѯ�����ʼ���������" + localMailList.size());
			myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
					localMailList);
			initView();
			break;
		case 1:// δ���ʼ�
			title_textview.setText("δ���ʼ�");
			getLocalMails();
			getNotRead();			
			myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
					mailslist);
			// ��δ���ʼ������洢���������ݿ���
			ContentValues values = new ContentValues();
			values.put("count", mailslist.size());
			System.out.println("δ���ʼ�����" + mailslist.size());
			getContentResolver().insert(Config.NOTREADEMAILURI, values);
			initView();
			break;
		case 2:// ��ѯ�Ѷ�
			title_textview.setText("�Ѷ��ʼ�");
			getLocalMails();
			getYesRead();	
			myAdapter = new EmailInBoxPagination(MailBoxActivity.this,
					mailslist);
			initView();
			break;
		}
		//ע���ʼ��˵�
		registerForContextMenu(lv_box);

		

	}

	private void initView() {
		lv_box = (ListView) findViewById(R.id.lv_box);
		lv_box.setAdapter(myAdapter);
		lv_box.setOnItemClickListener(new ListViewItemSelectListener());
		lv_box.setOnScrollListener(this);

	}

	/**
	 * ��ѯ���б����ʼ�
	 * 
	 * @return
	 */
	public void getLocalMails() {
		System.out.println("��ʼ��ѯ�����ʼ�");
		Cursor c = getContentResolver().query(Config.ALLEMAILURI, null, null, null,
				"messageid desc");

		// ����ʼ��������ʼ�
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
	 * ��ѯ�����ʼ�
	 * 
	 * @return
	 */
	public void getAllMails() {
		getLocalMails();
		// c.close();
		if (messageId != 0) {
			messageId = localMailList.get(0).getEmailID();
			// �ҳ����messageID
			for (int i = 0; i < localMailList.size(); i++) {

				if (messageId < localMailList.get(i).getEmailID())
					messageId = localMailList.get(i).getEmailID();
			}
		}
		System.out.println("����ID��" + messageId);

	}

	/**
	 * ��ѯ���Ѷ��ʼ�
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
	 * List�����
	 */
	class ListViewItemSelectListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> listview, View view,
				int indexint, long indexlong) {
			Email item = (Email) listview.getAdapter().getItem(indexint);
			System.out.println("������¼�����");
			// �����δ���ʼ��������ʼ�Ϊ�Ѷ������ҷ��͹㲥��δ��ͼ��-1
			if (item.getIsnew() == 1) {
				ContentValues cv = new ContentValues();
				cv.put("isNew", 0);
				getContentResolver().update(Config.ALLEMAILURI, cv, "messageid=?",

				new String[] { String.valueOf(item.getEmailID()) });
				Intent intent = new Intent();
				intent.setAction(BROADCASTACTION);
				intent.putExtra("readone", 1);
				
				sendBroadcast(intent);
				System.out.println("�㲥���ͳɹ�");
			}
			// //�����ʾ�Ѷ���ID�������ݿ�
			String mailID = String.valueOf(item.getEmailID());
			if (!messageids.contains(mailID)) {
				ContentValues values = new ContentValues();
				values.put("mailfrom", item.getFrom());
				values.put("messageid", mailID);
				getContentResolver().insert(Config.EMAILSTATUS_URI, values);
				System.out.println("�ɹ�������ʼ������Ѷ��ʼ����ݱ���");

			}
			// �����ʼ���ϸ��Ϣ����
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
		int itemsLastIndex = myAdapter.getCount() - 1; // ���ݼ����һ�������
		int lastIndex = itemsLastIndex + 1;
		if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
				&& visibleLastIndex == lastIndex) {
			// ������Զ�����,��������������첽�������ݵĴ���
		}
	}


	/**
	 * ��ѯδ��
	 */
	private void getNotRead() {
		System.out.println("��ʼ����δ���ʼ�");
		System.out.println(messageids);
		progressDialog = new ProgressDialog(MailBoxActivity.this);
		progressDialog.setMessage("���ڶ�ȡ�����Ե�......");
		progressDialog.setIndeterminate(true);
		progressDialog.show();
		for (Email mailReceiver : localMailList) {
			System.out.println(mailReceiver.getEmailID());
			Email email = new Email();
			try {
				if (messageids.contains(String.valueOf(mailReceiver
						.getEmailID()))) {
					System.out.println("���˵���");
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
		System.out.println("������Ϣ�ɹ�");
	}

	//
	/**
	 * ��ѯ�Ѷ�
	 */
	private void getYesRead() {
		progressDialog = new ProgressDialog(MailBoxActivity.this);
		progressDialog.setMessage("���ڶ�ȡ�����Ե�......");
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
		System.out.println("������Ϣ�ɹ�");
	}

	// ����
	public void back(View v) {
		finish();
		Intent intent=new Intent(this, Email_Home.class);
		intent.putExtra("frommail", true);
		startActivity(intent);
//		Intent intent=new Intent(this, Email_Home.class);
//		intent.putExtra("frommail", true);
//		startActivity(intent);
		
	}

//	// �˵���ɾ��ĳһ���ʼ�
//	@Override
//	public void onCreateContextMenu(ContextMenu menu, View v,
//			ContextMenuInfo menuInfo) {
//		getMenuInflater().inflate(R.menu.mailbox_menu, menu);
//		super.onCreateContextMenu(menu, v, menuInfo);
//	}

//	/**
//	 * �����¼�
//	 */
//	@Override
//	public boolean onContextItemSelected(MenuItem item) {
////		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
////				.getMenuInfo();
////		int id = (int) info.id;
////		System.out.println("id" + id);
//		int selectedPosition = ((AdapterContextMenuInfo) item.getMenuInfo()).position;
//		System.out.println("�����˵�" + selectedPosition+"��");
//		switch (item.getItemId()) {
//
//		case R.id.delete:
//			System.out.println(localMailList.get(selectedPosition).getTitle());
//			deleteMail(localMailList.get(selectedPosition).getEmailID(),selectedPosition);
//			// break;
//		}
//		return super.onContextItemSelected(item);
//	}

//	// ɾ��ĳһ���ʼ�
//	private void deleteMail(final int id,final int position) {
//		AlertDialog.Builder builder = new Builder(MailBoxActivity.this);
//		builder.setMessage("��ȷ��Ҫɾ������");
//		builder.setPositiveButton("ȷ��", new OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				System.out.println("ɾ�����ʼ�ID"+id);
//				getContentResolver().delete(allEmailUri, "messageid=?",
//						new String[] { String.valueOf(id) });
//				
//				// list = getAllConstacts();
//				localMailList.remove(position);
//				myAdapter.notifyDataSetChanged();
//				//initView();
//				Toast.makeText(MailBoxActivity.this, "ɾ���ʼ��ɹ�",
//						Toast.LENGTH_SHORT).show();
//				//ɾ���������ϸ��ʼ���ʵ����ɾ�����ʼ���Ӧ����ϵ�ˣ�
//				Bundle bundle = new Bundle();
//				bundle.putInt("messageId", id);
//				bundle.putString("userName", userName);
//				Def_Thread thread = new Def_Thread(handler, bundle,
//						MethodName.DELETEMAIL);
//				thread.start();
//			}
//		});
//		builder.setNegativeButton("ȡ��", null);
//		builder.show();
//	}
	
}
