package com.org.palmcampus.email;


import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.page.MainActivity;
import com.org.palmcampus.oa.pojo.ErpgongGao;
import com.org.palmcampus.oa.pojo.wraper.JsonToPojo;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;
import com.readystatesoftware.viewbadger.BadgeView;
import com.org.palmcampus.oa.custom.Config;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

/* ����������
 * @author rolant
 */
public class Email_Home extends Activity {
	private ExpandableListView expendView;
	private int[] group_click = new int[5];
	private long mExitTime = 0;
	private ProgressDialog progressDialog = null;
	private HttpPost request;
	private	HttpClient httpclient;
	private HttpResponse httpResponse; 
	private String responseStr=null,result,strUrl;
	
	
	// �㲥��Ϣ
	BroadcastMain broadcastMain;
	//BroadcastMain2 broadcastMain2;
	// ���ʼ�����
	int newcount = 0;
	public static final String BROADCASTACTION = "com.sc.broad2";
	public static final String BROADCASTACTION2 = "com.sc.broad3";
	/**
	 * Handler���շ���ֵ
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0)
			{
	
				try {
					JSONArray jar=new JSONArray(responseStr);
					Cursor c = getContentResolver().query(Config.CONTACT_URI, null, null,
							null, null);
					// ��ϵ�˸���
					int count = 0;
					while (c.moveToNext()) {
						count++;
					}
					c.close();
					if(jar.length()>count)
					{

					for(int i=count;i<jar.length();i++)
					{
						JSONObject job=jar.getJSONObject(i);
						insertAddress(job.getString("userName").trim(),
								job.getString("department").trim(),
								job.getString("jiaose").trim(),
								job.getString("zhiwei").trim());
					}
					
//					Toast.makeText(Email_Home.this, "��ϵ���Ѿ�����", Toast.LENGTH_SHORT)
//					.show();
					}
					progressDialog.cancel();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(this);
		strUrl=getResources().getString(R.string.hostedIp)+"/getEmailContact_phonea";
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_email_home);
		broadcastMain = new BroadcastMain();
		IntentFilter filter = new IntentFilter();
		filter.addAction(BROADCASTACTION);
		// ע��㲥������
		registerReceiver(broadcastMain, filter);

//		broadcastMain2 = new BroadcastMain2();
//		IntentFilter filter2 = new IntentFilter();
//		filter2.addAction(BROADCASTACTION2);
//		// ע��㲥������
//		registerReceiver(broadcastMain2, filter2);
		// ����������
		final MyExpendAdapter adapter = new MyExpendAdapter();

		expendView = (ExpandableListView) findViewById(R.id.list);
		// expendView.getChildAt(2).

		expendView.setGroupIndicator(null); // ����Ĭ��ͼ�겻��ʾ
		expendView.setAdapter(adapter);

		// һ������¼�
		expendView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				group_click[groupPosition] += 1;
				adapter.notifyDataSetChanged();
				return false;
			}
		});

		// ��������¼�
		expendView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// ��������������¼�
				if (groupPosition == 0 && childPosition == 1) {
					AlertDialog.Builder builder = new Builder(Email_Home.this);
					builder.setTitle("�����ϵ��");

					View view = getLayoutInflater().inflate(
							R.layout.email_add_address, null);
					final EditText name = (EditText) view
							.findViewById(R.id.name);
					final EditText addr = (EditText) view
							.findViewById(R.id.address);

					builder.setView(view);
					builder.setPositiveButton("ȷ��", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							insertAddress(name.getText().toString().trim(),
									addr.getText().toString().trim(),null,null);
							Toast.makeText(Email_Home.this, "������ݳɹ�", Toast.LENGTH_SHORT)
							.show();
						}
					});
					builder.setNegativeButton("ȡ��", null);
					builder.show();
				} else if (groupPosition == 0 && childPosition == 0) {
					Intent intent = new Intent(Email_Home.this,
							MailContactsActivity.class);
					startActivity(intent);
				} else if (groupPosition == 1 && childPosition == 0) {
					Intent intent = new Intent(Email_Home.this,
							MailEditActivity.class);
					startActivity(intent);
				} else if (groupPosition == 1 && childPosition == 1) {// �ݸ���
					Intent intent = new Intent(Email_Home.this,
							MailCaogaoxiangActivity.class);
					startActivity(intent);
				} else if (groupPosition == 2 && childPosition == 0) {// �����ʼ�
					Intent intent = new Intent(Email_Home.this,
							MailBoxActivity.class);
					intent.putExtra("status", 0);// ȫ��
					startActivity(intent);
				} else if (groupPosition == 2 && childPosition == 1) {// δ���ʼ�
					Intent intent = new Intent(Email_Home.this,
							MailBoxActivity.class);
					intent.putExtra("status", 1);// δ��
					startActivity(intent);
				} else if (groupPosition == 2 && childPosition == 2) {// �Ѷ��ʼ�
					Intent intent = new Intent(Email_Home.this,
							MailBoxActivity.class);
					intent.putExtra("status", 2);// �Ѷ�
					startActivity(intent);
				}
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		progressDialog = new ProgressDialog(Email_Home.this);
		progressDialog.setMessage("���ڸ�����ϵ�ˣ����Ե�......");
		progressDialog.setIndeterminate(true);
		progressDialog
				.setOnCancelListener(new DialogInterface.OnCancelListener() {
					public void onCancel(DialogInterface dialog) {

					}
				});

		progressDialog.show();


		new MyThread().t.start();
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// ע���㲥
		unregisterReceiver(broadcastMain);
	}

	@Override
	protected void onResume() {// ����������ʱ����֪ͨͼ��
		super.onResume();
		MyExpendAdapter adapter = new MyExpendAdapter();
		expendView.setAdapter(adapter);
	}

	/**
	 * �����ϵ��
	 */
	private void insertAddress(String user, String address, String jiaose, String zhiwei) {
		if (user == null) {
			Toast.makeText(Email_Home.this, "�û�������Ϊ��", Toast.LENGTH_SHORT)
					.show();
		} else {

			ContentValues values = new ContentValues();
			values.put("empName", user);
			values.put("mobile", address);
			values.put("jiaose", jiaose);
			values.put("zhiwei", zhiwei);
			
			getContentResolver().insert(Config.CONTACT_URI, values);



		}

	}


	/** 
	* @ClassName: MyExpendAdapter 
	* @Description:  ������
	* @author: kaleo 
	* @date 2014-8-18 ����1:59:51 
	*  
	*/ 
	private class MyExpendAdapter extends BaseExpandableListAdapter {

		/**
		 * pic state
		 */
		// int []group_state=new
		// int[]{R.drawable.group_right,R.drawable.group_down};

		/**
		 * group title
		 */
		String[] group_title = new String[] { "��ϵ��", "д�ʼ�", "�ռ���" };

		/**
		 * child text
		 */
		String[][] child_text = new String[][] { { "��ȡ��ϵ��", "�����ϵ��" },
				{ "���ʼ�", "�ݸ���" }, { "ȫ���ʼ�", "δ���ʼ�", "�Ѷ��ʼ�" }, };
		int[][] child_icons = new int[][] {
				{ R.drawable.listlianxiren, R.drawable.tianjia },
				{ R.drawable.xieyoujian, R.drawable.caogaoxiang },
				{ R.drawable.all, R.drawable.notread, R.drawable.hasread }, };

		/**
		 * ��ȡһ����ǩ�ж�����ǩ������
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return child_text[groupPosition][childPosition];
		}

		/**
		 * ��ȡ������ǩID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		/**
		 * ��һ����ǩ�µĶ�����ǩ��������
		 */
		@SuppressLint("SimpleDateFormat")
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.email_child,
					null);
			TextView tv = (TextView) convertView.findViewById(R.id.tv);
			LinearLayout childView = (LinearLayout) convertView
					.findViewById(R.id.email_child_LL);
			tv.setText(child_text[groupPosition][childPosition]);
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.child_icon);
			iv.setImageResource(child_icons[groupPosition][childPosition]);
			if (groupPosition == 0 && childPosition == 0) {//�������ϵ�ˣ���ʾ��ϵ������
				Cursor c = getContentResolver().query(Config.CONTACT_URI, null, null,
						null, null);
				// ��ϵ�˸���
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				tv.append("(" + count + ")");
			}
			if (groupPosition == 1 && childPosition == 1) {//����ǲݸ��䣬��ʾ�ݸ�����
				Cursor c = getContentResolver().query(Config.CAOGAO_URI, null, null,
						null, null);
				// �ݸ����
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				tv.append("(" + count + ")");
			}
			if (groupPosition == 2 && childPosition == 0) {// ����������ʼ�������ͼ�꣬��ʾ����
				Cursor c = getContentResolver().query(Config.ALLEMAILURI, null, null,
						null, null);
				// �����ʼ�����
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				// �ռ���������
				tv.append("(" + count + ")");
				if (newcount != 0) {
//					BadgeView badge = new BadgeView(getApplicationContext(),
//							childView);
//					// ���ʼ�����
//					badge.setText(String.valueOf(newcount));
//					badge.show();
				}
			}
			if (groupPosition == 2 && childPosition == 1) {// �����δ���ʼ�������ͼ�꣬��ʾ����
				Cursor c = getContentResolver().query(Config.ALLEMAILURI, null, "isNew=1",
						null, null);
				// δ���ʼ�����
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				// �ռ���������
				tv.append("(" + count + ")");

				
			}
			if (groupPosition == 2 && childPosition == 2) {// ������Ѷ��ʼ�������ͼ�꣬��ʾ����

				Cursor c = getContentResolver().query(Config.EMAILSTATUS_URI, null, null, null,
						null);
				// �Ѷ��ʼ�����
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				tv.append("(" + count + ")");
		
			}
			 if (groupPosition == 2 && childPosition == 1) {
				 //�����δ���ʼ�������ͼ�꣬��ʾ����
			 Cursor c = getContentResolver().query(Config.NOTREADEMAILURI, null,
			 null, null,
			 null);
			 //δ���ʼ�����
			 int count=0;
			 if(c.moveToNext()) {
			 count++;
			 }
			 c.close();

			 }
			return convertView;
		}

		/**
		 * һ����ǩ�¶�����ǩ������
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			return child_text[groupPosition].length;
		}

		/**
		 * ��ȡһ����ǩ����
		 */
		@Override
		public Object getGroup(int groupPosition) {
			return group_title[groupPosition];
		}

		/**
		 * һ����ǩ����
		 */
		@Override
		public int getGroupCount() {
			return group_title.length;
		}

		/**
		 * һ����ǩID
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		/**
		 * ��һ����ǩ��������
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			convertView = getLayoutInflater().inflate(R.layout.email_group,
					null);

			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
			ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
			TextView tv = (TextView) convertView.findViewById(R.id.iv_title);

			iv.setImageResource(R.drawable.group_right);
			tv.setText(group_title[groupPosition]);

			if (groupPosition == 0) {
				icon.setImageResource(R.drawable.constants);
			} else if (groupPosition == 1) {
				icon.setImageResource(R.drawable.mailto);
			} else if (groupPosition == 2) {
				icon.setImageResource(R.drawable.mailbox);
			}

			if (group_click[groupPosition] % 2 == 0) {
				iv.setImageResource(R.drawable.group_right);
			} else {
				iv.setImageResource(R.drawable.group_down);
			}

			return convertView;
		}

		/**
		 * ָ��λ����Ӧ������ͼ
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * ��ѡ���ӽڵ��ʱ�򣬵��ø÷���
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	// �㲥������
	class BroadcastMain extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			System.out.println("���ܹ㲥");
			newcount = intent.getIntExtra("newMailCount", 0);
		
			System.out.println("~~~~~~~~~~~~~~~~" + newcount);
			MyExpendAdapter adapter = new MyExpendAdapter();
			expendView.setAdapter(adapter);

		
		}

	}

	/** 
	* @ClassName: MyThread 
	* @Description: �ͷ������������߳���
	* @author: kaleo 
	* @date 2014-8-12 ����5:11:25 
	*  
	*/ 
	class MyThread extends Thread{
		
		Thread t=new Thread(new Runnable() {
			public void run() {

				//���ʷ�������action
				  request = new HttpPost(strUrl);  
				  httpclient = new DefaultHttpClient(); 
				  Message message=Message.obtain();
				  	//��װһ��json
				 	JSONObject logindate = new JSONObject(); 
						try {
							logindate.put("username","allUser");

							//�󶨵�����entry,��������֧��
							StringEntity se = new StringEntity(logindate.toString(),HTTP.UTF_8);  
							request.setEntity(se);
							
							httpResponse = httpclient.execute(request);	
							if(httpResponse.getStatusLine().getStatusCode()==200)		
							 {
								 responseStr = null;
								HttpEntity entity=httpResponse.getEntity();
								if(entity!=null)
								{
							
										try {
											responseStr =URLDecoder.decode(EntityUtils.toString(entity,HTTP.UTF_8), "utf-8");
											message.what=0;
											handler.sendMessage(message);
										
//											
										} catch (ParseException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										} catch (IOException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
								}
							 }		
							
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
			}
			
		});
		
}
}
