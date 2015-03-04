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
* <P>��Ŀ���ƣ�PalmCampus<P>   
* <P>�����ƣ�MainActivity<P>   
* <P>��������   ��¼����<P>
* <P>�����ˣ�kaleo<P>   
* <P>����ʱ�䣺2014-8-7 ����5:43:10<P>   
* <P>�޸��ˣ�kaleo<P>   
* <P>�޸�ʱ�䣺2014-8-7 ����5:43:10<P>   
* <P>�޸ı�ע��<P>   
* <P>@version 1.0<P>    
*    
*/

public class MainActivity extends Activity {
	private static final String SDSAVE_LOCATION = "/PalmCampusOA/myFile";
	/**
	* @Fields mian_checkbox_autologin : TODO(�Զ���¼��ѡ��)
	* @Fields mian_checkbox_rempwd : TODO(��ס���븴ѡ��)
	*/
	private CheckBox main_checkbox_rempwd,main_checkbox_autologin;
	private Button main_button_login,main_button_exit;
	private EditText main_edt_username=null,main_edt_password;
	
	private HttpPost request;
	private	HttpClient httpclient;
	private HttpResponse httpResponse; 
	private String responseStr=null;
	private int isRecordPWD,isAutoLogin;

	//���ݿ�
	private SQLiteDatabase db;
	private static EventData events;
	//�Ƿ�����
	private boolean isNetworktrue=false;
	
	/**  
	* @Fields strUrl : TODO(��������ַ)  
	*/ 
	String strUrl ;
	String tag="MainActivity  ��¼";
	// ����������
	private ProgressDialog progressDialog = null;
	/* (�� Javadoc) 
	* <p>Title: onCreate</p>
	* <p>Description: </p>
	* @param savedInstanceState 
	* @see android.app.Activity#onCreate(android.os.Bundle) 
	*/ 
	/* (�� Javadoc) 
	* <p>Title: onCreate</p>
	* <p>Description: </p>
	* @param savedInstanceState 
	* @see android.app.Activity#onCreate(android.os.Bundle) 
	*/ 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// ���ⲿ�洢���ϴ����ļ�
		File catalog = new File(Environment.getExternalStorageDirectory()
				+ SDSAVE_LOCATION);
		if (!catalog.exists()) {
			if (catalog.mkdirs()) {
				Log.i("TAG", "�����ɹ�");
			} else {
				Log.i("TAG", "����ʧ��");
			}
		}
		setContentView(R.layout.activity_main);
		SysExitUtil.activityList.add(this);
		//��ȡ�ؼ�
		strUrl =MainActivity.this.getResources().getString(R.string.hostedIp)+"/login_phonea";
		main_button_login=(Button)findViewById(R.id.mian_button_login);
		main_edt_username=(EditText)findViewById(R.id.mian_edt_username);
		main_edt_password=(EditText)findViewById(R.id.mian_edt_password);
	
		main_checkbox_rempwd=(CheckBox)findViewById(R.id.mian_checkbox_rempwd);
		main_checkbox_autologin=(CheckBox)findViewById(R.id.mian_checkbox_autologin);
		
		//�ж��Ƿ�����
		ConnectivityManager cManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cManager.getActiveNetworkInfo(); 
		  if (info != null && info.isAvailable()){ 
		       //do something 
		       //������ 
			  isNetworktrue= true; 
		  }else{ 
		       //do something 
		       //�������� 
			  isNetworktrue= false; 
		  } 
		

		//��ȡ���ݿ�
		events = new EventData(MainActivity.this);
		db = events.getReadableDatabase();
		// ȡ�����ݿ��û���
		Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);
		// ����м�¼
				if (ResultSet.moveToNext()) {
					// ȡ���û���
					main_edt_username.setText(ResultSet.getString(
							ResultSet.getColumnIndex("userName")).toString());
					// ���ѡ���ס���룬��ȡ������
					if (ResultSet.getInt(ResultSet.getColumnIndex("isRecordPWD"))==1) {
						main_edt_password.setText(ResultSet.getString(
								ResultSet.getColumnIndex("userPwd")).toString());
					}
					if (ResultSet.getInt(ResultSet.getColumnIndex("isAutoLogin"))==1) {
						if(isNetworktrue)
						{
						progressDialog = new ProgressDialog(MainActivity.this);
						progressDialog.setMessage("���ڵ�¼�����Ե�......");
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
							Toast.makeText(this, "�����ֻ���δ����", 4000).show();
						}
						
					}
				
				}else {
					main_edt_username.setText("");
					main_edt_password.setText("");
				}
				ResultSet.close();
				db.close();
		
		//���õ�¼��ť�ļ����¼�
		main_button_login.setOnClickListener(new LoginListener());

	}

	/** 
	* @ClassName: ExitListener 
	* @Description:  �˳���ť�Ĵ�����
	* @author: kaleo 
	* @date 2014-8-12 ����5:21:30 
	*  
	*/ 
	class ExitListener implements OnClickListener{

		/* (�� Javadoc) 
		* <p>Title: onClick</p>
		* <p>Description: �˳���ť����¼�</p>
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
	* @Description: ��¼��ť�Ĵ����� 
	* @author: kaleo 
	* @date 2014-8-12 ����5:16:28 
	*  
	*/ 
	class LoginListener implements OnClickListener{

		/* (�� Javadoc) 
		* <p>Title: onClick</p>
		* <p>Description:��¼��ť�ĵ���¼� </p>
		* @param arg0 
		* @see android.view.View.OnClickListener#onClick(android.view.View) 
		*/ 
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if (main_edt_username.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this, "�û�������Ϊ��", Toast.LENGTH_SHORT)
						.show();
			} else if (main_edt_password.getText().toString().equals("")) {
				Toast.makeText(MainActivity.this, "����������", Toast.LENGTH_SHORT).show();
			} else {
				progressDialog = new ProgressDialog(MainActivity.this);
				progressDialog.setMessage("���ڵ�¼�����Ե�......");
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
	* @Fields handler : TODO(�����������̷߳��͹�������Ϣ)  
	*/ 
	Handler handler=new Handler(){
		/**
		 * <p>Title: handleMessage</p>
		 * <p>Description: ������Ϣ����ִ�в���
		 * 	0����¼�ɹ� 
		 * 	1���û���������
		 * 	2���������
		 * 	3���Ѿ���¼�������ظ���¼
		 * 	4����������ʧ��</p>
		 * @param msg
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		public void handleMessage(Message msg){
		
			switch (msg.what) {
			case 0:
			
					JSONObject user=(JSONObject)msg.obj;
					//��ȡ����
					
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
					// ������ݿ����м�¼�����µ�½�ɹ����û���Ϣ
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
					Toast.makeText(MainActivity.this, "��ȡ�û�����ʧ��", 4000).show();
				}
					Intent intent=new Intent(MainActivity.this, IndexActivity.class);
					startActivity(intent);
					progressDialog.cancel();
					MainActivity.this.finish();
					
					Toast.makeText(MainActivity.this, "��¼�ɹ�", 4000).show();
				
			
				break;
			case 1:
				Toast.makeText(MainActivity.this, "�û���������", 4000).show();
				progressDialog.cancel();
				break;
			case 2:
				Toast.makeText(MainActivity.this, "�������", 4000).show();
				progressDialog.cancel();
			
				break;
			case 3:
				Toast.makeText(MainActivity.this, "���û�����ֹ��½", 4000).show();
				progressDialog.cancel();
			
				break;
			default:
				Toast.makeText(MainActivity.this, "�����û���������", 4000).show();
				progressDialog.cancel();
				break;
			}
		}
	};
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
						
						logindate.put("username", main_edt_username.getText().toString());
						logindate.put("password", main_edt_password.getText().toString()); 
						logindate.put("rempwd", main_checkbox_rempwd.isChecked());
						logindate.put("autologin", main_checkbox_autologin.isChecked());
						
						//�󶨵�����entry,��������֧��
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
										 *������Ϣ�����߳�
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
