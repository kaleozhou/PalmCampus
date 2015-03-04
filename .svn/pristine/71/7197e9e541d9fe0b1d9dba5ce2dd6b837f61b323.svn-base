package com.org.palmcampus.oa.service;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
import com.org.palmcampus.email.bean.Email;
import com.org.palmcampus.oa.custom.Config;
import com.org.palmcampus.oa.pojo.ErplanEmail;
import com.org.palmcampus.oa.pojo.wraper.JsonToPojo;
import com.org.palmcampus.oa.sql.EventData;



import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

//邮件服务，用来获取新邮件并广播通知用户
public class MailService extends Service {
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
		
	}

	private boolean flag = true;
	private int count = 0;
	private static final int TIME=60*1000;
	private MyThread myThread;
	public static final String BROADCASTACTION = "com.org.palmcampus.oa.broadcastMaun.broad";
	private HttpPost request;
	private	HttpClient httpclient;
	private HttpResponse httpResponse; 
	private String responseStr=null,result,strUrl;
	
	// 最大邮件ID号
	int messageId = 0;
	// 新邮件集合
	private List<ErplanEmail> newMaillist = new ArrayList<ErplanEmail>();
	// 本地邮件集合
	private List<ErplanEmail> localMailList = new ArrayList<ErplanEmail>();
	// 数据库
	private SQLiteDatabase db;
	private static EventData events;
	// 发件人姓名
	String userName = "";
	/**
	 * Handler接收返回值
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			// 如果有新邮件
			if(msg.what==0)
			{
				newMaillist.clear();
			JSONArray jar=(JSONArray)msg.obj;
			for(int i = 0;i<jar.length();i++)
			{
				JsonToPojo jtop=new JsonToPojo();
				JSONObject obj;
				try {
					obj = (JSONObject) jar.get(i);
					ErplanEmail e=jtop.toErplanEmail(obj);
					newMaillist.add(e);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
				if (newMaillist.size() > 0) {
					Intent intent = new Intent();
					intent.setAction(BROADCASTACTION);
					System.out.println("新邮件个数" + newMaillist.size());
					intent.putExtra("newMailCount", newMaillist.size());
					// 将新邮件保存到本地数据库
					for (ErplanEmail e : newMaillist) {
						ContentValues values = new ContentValues();
						values.put("messageid", e.getId());
						values.put("title", e.getEmailTitle());
						values.put("mailfrom", e.getFromUser());
						values.put("mailto", e.getToUser());
						values.put("cc", e.getCc());
						values.put("sendDate", e.getTimeStr().toString());
						values.put("content", e.getEmailContent());
						values.put("attachment", e.getFuJian());
						//1代表新邮件，未读邮件
						values.put("isNew", 1);
						getContentResolver().insert(Config.ALLEMAILURI, values);
					}
//					//将新邮件添加到本地邮件集合
//					//localMailList.addAll(newMaillist);
					//保存数据，发送广播
					intent.putExtra("mailList", (Serializable)newMaillist);
					//intent.putExtra();
					sendBroadcast(intent);
					Intent intent1=new Intent();	
					//邮件
					intent1.putExtra("who", 1);
					intent1.putExtra("count", newMaillist.size());
					intent1.setAction("com.org.palmcampus.oa.broadcastReceiver.SHASHABROAD");
					sendBroadcast(intent1);
				}
			}
		}

	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		strUrl=this.getResources().getString(R.string.hostedIp)+"/receiveEmail_phonea";
		new MyThread().t.start();
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
			ErplanEmail e = new ErplanEmail();
			messageId = c.getInt(c.getColumnIndex("messageid"));
			String title = c.getString(c.getColumnIndex("title"));
			String mailfrom = c.getString(c.getColumnIndex("mailfrom"));
			String mailto = c.getString(c.getColumnIndex("mailto"));
			String cc = c.getString(c.getColumnIndex("cc"));
			String sendDate = c.getString(c.getColumnIndex("sendDate"));
			String content = c.getString(c.getColumnIndex("content"));
			String attachment = c.getString(c.getColumnIndex("attachment"));
			// System.out.println(attachment);
			e.setId(messageId);
			e.setEmailTitle(title);
			e.setFromUser(mailfrom);
			e.setToUser(mailto);
			e.setCc(cc);
			e.setTimeStr(Timestamp.valueOf(sendDate));
			e.setEmailContent(content);
			e.setFuJian(attachment);
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
		// 取出数据库的值
		events = new EventData(getApplicationContext());
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
		// c.close();
		if (messageId != 0) {
			messageId = localMailList.get(0).getId();
			// 找出最大messageID
			for (int i = 0; i < localMailList.size(); i++) {

				if (messageId < localMailList.get(i).getId())
					messageId = localMailList.get(i).getId();
			}
		}
		System.out.println("最大的ID是" + messageId);
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
				while (flag) {
					getAllMails();
				//访问服务器的action
				  request = new HttpPost(strUrl);  
				  httpclient = new DefaultHttpClient(); 
				  Message message=Message.obtain();
				  	//封装一个json
				 	JSONObject logindate = new JSONObject(); 
						try {
							logindate.put("username",userName);
							logindate.put("messageId",messageId);
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
											JSONObject job=new JSONObject(responseStr);
											boolean res=job.getBoolean("res");
											if(res)
											{
											message.obj=job.getJSONArray("ls");
											message.what=0;
											}else
											{
											message.what=1;
											
											}
											handler.sendMessage(message);
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
						try {
							//休眠一分钟
							Thread.sleep(TIME);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
			}
			}
		});
		
	}
}

