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

/* 邮箱主界面
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
	
	
	// 广播消息
	BroadcastMain broadcastMain;
	//BroadcastMain2 broadcastMain2;
	// 新邮件数量
	int newcount = 0;
	public static final String BROADCASTACTION = "com.sc.broad2";
	public static final String BROADCASTACTION2 = "com.sc.broad3";
	/**
	 * Handler接收返回值
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
					// 联系人个数
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
					
//					Toast.makeText(Email_Home.this, "联系人已经更新", Toast.LENGTH_SHORT)
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
		// 注册广播接收器
		registerReceiver(broadcastMain, filter);

//		broadcastMain2 = new BroadcastMain2();
//		IntentFilter filter2 = new IntentFilter();
//		filter2.addAction(BROADCASTACTION2);
//		// 注册广播接收器
//		registerReceiver(broadcastMain2, filter2);
		// 定义适配器
		final MyExpendAdapter adapter = new MyExpendAdapter();

		expendView = (ExpandableListView) findViewById(R.id.list);
		// expendView.getChildAt(2).

		expendView.setGroupIndicator(null); // 设置默认图标不显示
		expendView.setAdapter(adapter);

		// 一级点击事件
		expendView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				group_click[groupPosition] += 1;
				adapter.notifyDataSetChanged();
				return false;
			}
		});

		// 二级点击事件
		expendView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// 可在这里做点击事件
				if (groupPosition == 0 && childPosition == 1) {
					AlertDialog.Builder builder = new Builder(Email_Home.this);
					builder.setTitle("添加联系人");

					View view = getLayoutInflater().inflate(
							R.layout.email_add_address, null);
					final EditText name = (EditText) view
							.findViewById(R.id.name);
					final EditText addr = (EditText) view
							.findViewById(R.id.address);

					builder.setView(view);
					builder.setPositiveButton("确定", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							insertAddress(name.getText().toString().trim(),
									addr.getText().toString().trim(),null,null);
							Toast.makeText(Email_Home.this, "添加数据成功", Toast.LENGTH_SHORT)
							.show();
						}
					});
					builder.setNegativeButton("取消", null);
					builder.show();
				} else if (groupPosition == 0 && childPosition == 0) {
					Intent intent = new Intent(Email_Home.this,
							MailContactsActivity.class);
					startActivity(intent);
				} else if (groupPosition == 1 && childPosition == 0) {
					Intent intent = new Intent(Email_Home.this,
							MailEditActivity.class);
					startActivity(intent);
				} else if (groupPosition == 1 && childPosition == 1) {// 草稿箱
					Intent intent = new Intent(Email_Home.this,
							MailCaogaoxiangActivity.class);
					startActivity(intent);
				} else if (groupPosition == 2 && childPosition == 0) {// 所有邮件
					Intent intent = new Intent(Email_Home.this,
							MailBoxActivity.class);
					intent.putExtra("status", 0);// 全部
					startActivity(intent);
				} else if (groupPosition == 2 && childPosition == 1) {// 未读邮件
					Intent intent = new Intent(Email_Home.this,
							MailBoxActivity.class);
					intent.putExtra("status", 1);// 未读
					startActivity(intent);
				} else if (groupPosition == 2 && childPosition == 2) {// 已读邮件
					Intent intent = new Intent(Email_Home.this,
							MailBoxActivity.class);
					intent.putExtra("status", 2);// 已读
					startActivity(intent);
				}
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		progressDialog = new ProgressDialog(Email_Home.this);
		progressDialog.setMessage("正在更新联系人，请稍等......");
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
		// 注销广播
		unregisterReceiver(broadcastMain);
	}

	@Override
	protected void onResume() {// 返回主界面时更新通知图标
		super.onResume();
		MyExpendAdapter adapter = new MyExpendAdapter();
		expendView.setAdapter(adapter);
	}

	/**
	 * 添加联系人
	 */
	private void insertAddress(String user, String address, String jiaose, String zhiwei) {
		if (user == null) {
			Toast.makeText(Email_Home.this, "用户名不能为空", Toast.LENGTH_SHORT)
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
	* @Description:  适配器
	* @author: kaleo 
	* @date 2014-8-18 下午1:59:51 
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
		String[] group_title = new String[] { "联系人", "写邮件", "收件箱" };

		/**
		 * child text
		 */
		String[][] child_text = new String[][] { { "获取联系人", "添加联系人" },
				{ "新邮件", "草稿箱" }, { "全部邮件", "未读邮件", "已读邮件" }, };
		int[][] child_icons = new int[][] {
				{ R.drawable.listlianxiren, R.drawable.tianjia },
				{ R.drawable.xieyoujian, R.drawable.caogaoxiang },
				{ R.drawable.all, R.drawable.notread, R.drawable.hasread }, };

		/**
		 * 获取一级标签中二级标签的内容
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return child_text[groupPosition][childPosition];
		}

		/**
		 * 获取二级标签ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		/**
		 * 对一级标签下的二级标签进行设置
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
			if (groupPosition == 0 && childPosition == 0) {//如果是联系人，显示联系人数量
				Cursor c = getContentResolver().query(Config.CONTACT_URI, null, null,
						null, null);
				// 联系人个数
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				tv.append("(" + count + ")");
			}
			if (groupPosition == 1 && childPosition == 1) {//如果是草稿箱，显示草稿数量
				Cursor c = getContentResolver().query(Config.CAOGAO_URI, null, null,
						null, null);
				// 草稿个数
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				tv.append("(" + count + ")");
			}
			if (groupPosition == 2 && childPosition == 0) {// 如果是所有邮件，更新图标，显示数量
				Cursor c = getContentResolver().query(Config.ALLEMAILURI, null, null,
						null, null);
				// 所有邮件个数
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				// 收件箱总数量
				tv.append("(" + count + ")");
				if (newcount != 0) {
//					BadgeView badge = new BadgeView(getApplicationContext(),
//							childView);
//					// 新邮件数量
//					badge.setText(String.valueOf(newcount));
//					badge.show();
				}
			}
			if (groupPosition == 2 && childPosition == 1) {// 如果是未读邮件，更新图标，显示数量
				Cursor c = getContentResolver().query(Config.ALLEMAILURI, null, "isNew=1",
						null, null);
				// 未读邮件个数
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				// 收件箱总数量
				tv.append("(" + count + ")");

				
			}
			if (groupPosition == 2 && childPosition == 2) {// 如果是已读邮件，更新图标，显示数量

				Cursor c = getContentResolver().query(Config.EMAILSTATUS_URI, null, null, null,
						null);
				// 已读邮件个数
				int count = 0;
				while (c.moveToNext()) {
					count++;
				}
				c.close();
				tv.append("(" + count + ")");
		
			}
			 if (groupPosition == 2 && childPosition == 1) {
				 //如果是未读邮件，更新图标，显示数量
			 Cursor c = getContentResolver().query(Config.NOTREADEMAILURI, null,
			 null, null,
			 null);
			 //未读邮件个数
			 int count=0;
			 if(c.moveToNext()) {
			 count++;
			 }
			 c.close();

			 }
			return convertView;
		}

		/**
		 * 一级标签下二级标签的数量
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			return child_text[groupPosition].length;
		}

		/**
		 * 获取一级标签内容
		 */
		@Override
		public Object getGroup(int groupPosition) {
			return group_title[groupPosition];
		}

		/**
		 * 一级标签总数
		 */
		@Override
		public int getGroupCount() {
			return group_title.length;
		}

		/**
		 * 一级标签ID
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		/**
		 * 对一级标签进行设置
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
		 * 指定位置相应的组视图
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}

		/**
		 * 当选择子节点的时候，调用该方法
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	// 广播接收器
	class BroadcastMain extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			System.out.println("接受广播");
			newcount = intent.getIntExtra("newMailCount", 0);
		
			System.out.println("~~~~~~~~~~~~~~~~" + newcount);
			MyExpendAdapter adapter = new MyExpendAdapter();
			expendView.setAdapter(adapter);

		
		}

	}

	/** 
	* @ClassName: MyThread 
	* @Description: 和服务器交互的线程类
	* @author: kaleo 
	* @date 2014-8-12 下午5:11:25 
	*  
	*/ 
	class MyThread extends Thread{
		
		Thread t=new Thread(new Runnable() {
			public void run() {

				//访问服务器的action
				  request = new HttpPost(strUrl);  
				  httpclient = new DefaultHttpClient(); 
				  Message message=Message.obtain();
				  	//封装一个json
				 	JSONObject logindate = new JSONObject(); 
						try {
							logindate.put("username","allUser");

							//绑定到请求entry,设置中文支持
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
