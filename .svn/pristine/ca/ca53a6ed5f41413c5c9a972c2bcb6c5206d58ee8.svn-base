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
import com.org.palmcampus.oa.pojo.ErpgongGao;
import com.org.palmcampus.oa.pojo.ErplanEmail;
import com.org.palmcampus.oa.pojo.wraper.JsonToPojo;
import com.org.palmcampus.oa.service.MailService.MyThread;
import com.org.palmcampus.oa.sql.EventData;




import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

//新闻通告服务，用来获取新通告并广播通知用户
public class NewsService extends Service {
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		flag = false;
	}
	String userName = null;
	
	private boolean flag = true;
	private HttpPost request;
	private	HttpClient httpclient;
	private HttpResponse httpResponse; 
	private String responseStr=null,result,strUrl;
	private int count = 0;
	private MyThread myThread;
	public static final String BROADCASTACTION = "com.org.palmcampus.oa.service.NewsService";
	//private Uri allEmailUri = Uri.parse("content://com.emailconstantprovider");
	// 最大新闻ID号
	int id = 0;
	// 新邮件集合
	private List<ErpgongGao> newNewslist = new ArrayList<ErpgongGao>();
	// 本地邮件集合
	private List<ErpgongGao> localNewsList = new ArrayList<ErpgongGao>();
	// 数据库
	private SQLiteDatabase db;
	private static EventData events;
	private static int TIME=60*1000;
	/**
	 * Handler接收返回值
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(msg.what==0)
			{
				
				newNewslist.clear();
			JSONArray jar=(JSONArray)msg.obj;
			for(int i = 0;i<jar.length();i++)
			{
				JsonToPojo jtop=new JsonToPojo();
				JSONObject obj;
				try {
					obj = (JSONObject) jar.get(i);
					ErpgongGao e=jtop.toErpgongGao(obj);
					newNewslist.add(e);
				} catch (JSONException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}

	
				if (newNewslist.size() > 0) {
					Intent intent = new Intent();
					intent.setAction(BROADCASTACTION);
					System.out.println("新通告个数" + newNewslist.size());
					intent.putExtra("newNewsCount", newNewslist.size());
					// 将新通告保存到本地数据库
					for (ErpgongGao e : newNewslist) {
						ContentValues values = new ContentValues();
						values.put("id", e.getId());
						values.put("titleStr", e.getTitleStr());
//						values.put("typeStr", e.getTypeStr());
						values.put("contentStr", e.getContentStr());
						values.put("userName", e.getUserName());
						values.put("yiJieShouRen", e.getYiJieShouRen());
						values.put("chuanYueYiJian", e.getChuanYueYiJian());
						//1代表是置顶的，0代表普通消息
						values.put("isTop", e.getIsTop());
						//1代表新通知，未读通知，
						values.put("isNew", 1);
						values.put("timeStr", e.getTimeStr().toString());
						events = new EventData(getApplicationContext());
						db = events.getReadableDatabase();
						db.insert("gongGao", null, values);
						db.close();
					}
					//将新邮件添加到本地邮件集合
					//localMailList.addAll(newMaillist);
					//保存数据，发送广播
					intent.putExtra("newsList", (Serializable)newNewslist);
					//intent.putExtra();
					sendBroadcast(intent);
					Intent intent1=new Intent();	
					//邮件
					intent1.putExtra("who", 0);
					intent1.putExtra("count", newNewslist.size());
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
	public void onCreate() {
		// TODO Auto-generated method stub
//		super.onCreate();
//		System.out.println("------新闻服务开始---");
//		strUrl=this.getResources().getString(R.string.hostedIp)+"/receiveGonggao_phonea";
//		new MyThread().t.start();
	}
	


	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		System.out.println("------新闻服务开始---");
		strUrl=this.getResources().getString(R.string.hostedIp)+"/receiveGonggao_phonea";
		new MyThread().t.start();
	}

	/**
	 * 查询所有本地新闻
	 * 
	 * 
	 * @return
	 */
	public void getLocalNews() {
		System.out.println("开始查询本地新闻");
		events = new EventData(getApplicationContext());
		db = events.getReadableDatabase();
		Cursor c = db.rawQuery("select * from gongGao order by isTop desc", null);
		// 如果邮件表里有新闻
		while (c.moveToNext()) {
			ErpgongGao e = new ErpgongGao();
			id = c.getInt(c.getColumnIndex("id"));
			String titleStr = c.getString(c.getColumnIndex("titleStr"));
			String typeStr = c.getString(c.getColumnIndex("typeStr"));
			String contentStr = c.getString(c.getColumnIndex("contentStr"));
			String userName = c.getString(c.getColumnIndex("userName"));
			String yiJieShouRen = c.getString(c.getColumnIndex("yiJieShouRen"));
			String chuanYueYiJian = c.getString(c.getColumnIndex("chuanYueYiJian"));
			String isTop=c.getString(c.getColumnIndex("isTop"));
			String timeStr=c.getString(c.getColumnIndex("timeStr"));
			// System.out.println(attachment);
			e.setId(id);
			e.setTitleStr(titleStr);
			e.setContentStr(contentStr);
			e.setUserName(userName);
			e.setContentStr(contentStr);
			e.setYiJieShouRen(yiJieShouRen);
			e.setChuanYueYiJian(chuanYueYiJian);
			e.setTimeStr(timeStr);
			localNewsList.add(e);
		}
		c.close();
		db.close();
	}

	/**
	 * 查询所有新闻
	 * 
	 * @return
	 */
	public void getAllNews() {
		getLocalNews();
		// 取出数据库的值
		events = new EventData(getApplicationContext());
		db = events.getReadableDatabase();
		// 取出数据库用户表
		Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);

		// 如果有记录
		if (ResultSet.moveToNext()) {
			userName = ResultSet.getString(ResultSet.getColumnIndex("trueName"));
			
			System.out.println("员工号是" + userName);
		}
		// 关闭数据库
		ResultSet.close();
		db.close();
		// c.close();
		if (id != 0) {
			id = localNewsList.get(0).getId();
			// 找出最大messageID
			for (int i = 0; i < localNewsList.size(); i++) {

				if (id < localNewsList.get(i).getId())
					id = localNewsList.get(i).getId();
			}
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
					while (flag) {
						getAllNews();
					//访问服务器的action
					  request = new HttpPost(strUrl);  
					  httpclient = new DefaultHttpClient(); 
					  Message message=Message.obtain();
					  	//封装一个json
					 	JSONObject logindate = new JSONObject(); 
							try {
								logindate.put("username",userName);
								logindate.put("id",id);
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


	
	

