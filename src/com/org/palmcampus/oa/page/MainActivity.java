package com.org.palmcampus.oa.page;



import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;


import com.org.palmcampus.R;
import com.org.palmcampus.oa.sql.EventData;

import com.org.palmcampus.oa.util.SysExitUtil;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;


/**   
*    
* <P>项目名称：PalmCampus<P>   
* <P>类名称：MainActivity<P>   
* <P>类描述：   登录界面<P>
* <P>创建人：kaleo<P>   
* <P>创建时间：2014-8-7 下午5:43:10<P>   
* <P>修改人：kaleo<P>   
* <P>修改时间：2014-8-7 下午5:43:10<P>   
* <P>修改备注：<P>   
* <P>@version 1.0<P>    
*    
*/

public class MainActivity extends Activity {
	private static final String SDSAVE_LOCATION = "/PalmCampusOA/myFile";
	/**
	* @Fields mian_checkbox_autologin : TODO(自动登录复选框)
	* @Fields mian_checkbox_rempwd : TODO(记住密码复选框)
	*/
	private CheckBox main_checkbox_rempwd,main_checkbox_autologin;
	private Button main_button_login,main_button_exit;
	private EditText main_edt_username=null,main_edt_password;
	
	private HttpPost request;
	private	HttpClient httpclient;
	private HttpResponse httpResponse; 
	private String responseStr=null;
	private int isRecordPWD,isAutoLogin;

	//数据库
	private SQLiteDatabase db;
	private static EventData events;
	//是否联网
	private boolean isNetworktrue=false;
	
	/**  
	* @Fields strUrl : TODO(服务器地址)  
	*/ 
	String strUrl ;
	String tag="MainActivity  登录";
	// 滚动进度条
	private ProgressDialog progressDialog = null;
	/* (非 Javadoc) 
	* <p>Title: onCreate</p>
	* <p>Description: </p>
	* @param savedInstanceState 
	* @see android.app.Activity#onCreate(android.os.Bundle) 
	*/ 
	/* (非 Javadoc) 
	* <p>Title: onCreate</p>
	* <p>Description: </p>
	* @param savedInstanceState 
	* @see android.app.Activity#onCreate(android.os.Bundle) 
	*/ 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在外部存储卡上创建文件
		File catalog = new File(Environment.getExternalStorageDirectory()
				+ SDSAVE_LOCATION);
		if (!catalog.exists()) {
			if (catalog.mkdirs()) {
				Log.i("TAG", "创建成功");
			} else {
				Log.i("TAG", "创建失败");
			}
		}
		setContentView(R.layout.activity_main);
		SysExitUtil.activityList.add(this);
		//获取控件
		strUrl =MainActivity.this.getResources().getString(R.string.hostedIp)+"/login_phonea";
		main_button_login=(Button)findViewById(R.id.mian_button_login);
		main_edt_username=(EditText)findViewById(R.id.mian_edt_username);
		main_edt_password=(EditText)findViewById(R.id.mian_edt_password);
	
		main_checkbox_rempwd=(CheckBox)findViewById(R.id.mian_checkbox_rempwd);
		main_checkbox_autologin=(CheckBox)findViewById(R.id.mian_checkbox_autologin);
		
		//判断是否联网
		ConnectivityManager cManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo(); 
		  if (info != null && info.isAvailable()){ 
		       //do something 
		       //能联网 
			  isNetworktrue= true; 
		  }else{ 
		       //do something 
		       //不能联网 
			  isNetworktrue= false; 
		  } 
		

		//获取数据库
		events = new EventData(MainActivity.this);
		db = events.getReadableDatabase();
		// 取出数据库用户表
		Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);
		// 如果有记录
				if (ResultSet.moveToNext()) {
					// 取出用户名
					main_edt_username.setText(ResultSet.getString(
							ResultSet.getColumnIndex("userName")).toString());
					// 如果选择记住密码，则取出密码
					if (ResultSet.getInt(ResultSet.getColumnIndex("isRecordPWD"))==1) {
						main_edt_password.setText(ResultSet.getString(
								ResultSet.getColumnIndex("userPwd")).toString());
					}
					if (ResultSet.getInt(ResultSet.getColumnIndex("isAutoLogin"))==1) {
						if(isNetworktrue)
						{
						progressDialog = new ProgressDialog(MainActivity.this);
						progressDialog.setMessage("正在登录，请稍等......");
						progressDialog.setIndeterminate(true);
						progressDialog
								.setOnCancelListener(new DialogInterface.OnCancelListener() {
									public void onCancel(DialogInterface dialog) {

									}
								});

						progressDialog.show();
						new MyThread().t.start();
						}else
						{
							Toast.makeText(this, "您的手机尚未联网", 4000).show();
						}
						
					}
				
				}else {
					main_edt_username.setText("");
					main_edt_password.setText("");
				}
				ResultSet.close();
				db.close();
		
		//设置登录按钮的监听事件
		main_button_login.setOnClickListener(new LoginListener());

	}

	/** 
	* @ClassName: ExitListener 
	* @Description:  退出按钮的触发类
	* @author: kaleo 
	* @date 2014-8-12 下午5:21:30 
	*  
	*/ 
	class ExitListener implements OnClickListener{

		/* (非 Javadoc) 
		* <p>Title: onClick</p>
		* <p>Description: 退出按钮点击事件</p>
		* @param arg0 
		* @see android.view.View.OnClickListener#onClick(android.view.View) 
		*/ 
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub

			
			SysExitUtil.exit();
		}
		
	}
	/** 
	* @ClassName: LoginListener 
	* @Description: 登录按钮的触发类 
	* @author: kaleo 
	* @date 2014-8-12 下午5:16:28 
	*  
	*/ 
	class LoginListener implements OnClickListener{

		/* (非 Javadoc) 
		* <p>Title: onClick</p>
		* <p>Description:登录按钮的点击事件 </p>
		* @param arg0 
		* @see android.view.View.OnClickListener#onClick(android.view.View) 
		*/ 
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (main_edt_username.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this, "用户名不能为空", Toast.LENGTH_SHORT)
						.show();
			} else if (main_edt_password.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
			} else {
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("正在登录，请稍等......");
				progressDialog.setIndeterminate(true);
				progressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {

							}
						});

				progressDialog.show();
				new MyThread().t.start();
		
			}
		}
		
	}


	/**  
	* @Fields handler : TODO(用来接收子线程发送过来的消息)  
	*/ 
	Handler handler=new Handler(){
		/**
		 * <p>Title: handleMessage</p>
		 * <p>Description: 返回消息，并执行操作
		 * 	0、登录成功 
		 * 	1、用户名不存在
		 * 	2、密码错误
		 * 	3、已经登录不允许重复登录
		 * 	4、网络连接失败</p>
		 * @param msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg){
		
			switch (msg.what) {
			case 0:
			
					JSONObject user=(JSONObject)msg.obj;
					//获取数据
					
				try {int keyid=user.getInt("id");
					String userName,trueName;
					userName = user.getString("trueName");
					trueName = user.getString("userName");
					String userPwd=main_edt_password.getText().toString();
					isRecordPWD = main_checkbox_rempwd.isChecked() ? 1 : 0;
					isAutoLogin = main_checkbox_autologin.isChecked() ? 1 : 0;
					
					events = new EventData(MainActivity.this);
					db = events.getReadableDatabase();
					Cursor ResultSet = db.rawQuery("select * from Sys_Users",
							null);
					// 如果数据库里有记录，更新登陆成功的用户信息
					if(ResultSet.moveToNext()){
						String sqlStr = "Update Sys_Users set userName = '"+userName+"'"
										+",userPwd='"+userPwd+"'"
										+",trueName='"+trueName+"'"
										+",isRecordPWD="+isRecordPWD+",isAutoLogin="+isAutoLogin;
						db = events.getWritableDatabase();
						db.execSQL(sqlStr);
					}
					else
					{
						String sqlStr="insert into Sys_Users (keyID,userName,userPwd,isRecordPWD,isAutoLogin,trueName) values ("+keyid+",'"
								+userName+"','"+userPwd+"',"+isRecordPWD+","+isAutoLogin+",'"+trueName+"')";
								
								
						db = events.getWritableDatabase();
						db.execSQL(sqlStr);
					}
					
					ResultSet.close();
					db.close();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(MainActivity.this, "读取用户数据失败", 4000).show();
				}
					Intent intent=new Intent(MainActivity.this, IndexActivity.class);
					startActivity(intent);
					progressDialog.cancel();
					MainActivity.this.finish();
					
					Toast.makeText(MainActivity.this, "登录成功", 4000).show();
				
			
				break;
			case 1:
				Toast.makeText(MainActivity.this, "用户名不存在", 4000).show();
				progressDialog.cancel();
				break;
			case 2:
				Toast.makeText(MainActivity.this, "密码错误", 4000).show();
				progressDialog.cancel();
			
				break;
			case 3:
				Toast.makeText(MainActivity.this, "该用户被禁止登陆", 4000).show();
				progressDialog.cancel();
			
				break;
			default:
				Toast.makeText(MainActivity.this, "请检查用户名或网络", 4000).show();
				progressDialog.cancel();
				break;
			}
		}
	};
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
						
						logindate.put("username", main_edt_username.getText().toString());
						logindate.put("password", main_edt_password.getText().toString()); 
						logindate.put("rempwd", main_checkbox_rempwd.isChecked());
						logindate.put("autologin", main_checkbox_autologin.isChecked());
						
						//绑定到请求entry,设置中文支持
						StringEntity se = new StringEntity(logindate.toString(),HTTP.UTF_8);  
						request.setEntity(se);
						
						httpResponse = httpclient.execute(request);	
						sleep(1000);
						if(httpResponse.getStatusLine().getStatusCode()==200)		
						 {
							 responseStr = null;
							HttpEntity entity=httpResponse.getEntity();
							if(entity!=null)
							{
						
									try {
										responseStr =URLDecoder.decode(EntityUtils.toString(entity,HTTP.UTF_8), "utf-8");
										JSONObject result=new JSONObject(responseStr);
										
										/**
										 *发送消息给主线程
										 */
									
										message.what=result.getInt("i");
										if(message.what==0||message.what==3)
										{
//											if(message.what==3)
//											{
//												message.what=0;
//											}
										JSONObject user=result.getJSONObject("user");
										message.obj=user;
										}


										
									} catch (ParseException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
							}
						 }		
						else
						{
							message.what=4;
						}
		
					} catch (JSONException e) {
						// TODO Auto-generated catch block	
						e.printStackTrace();
						message.what=4;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						message.what=4;	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();				
						message.what=4;
					}
					MainActivity.this.handler.sendMessage(message);
			}
		});
	}

}
