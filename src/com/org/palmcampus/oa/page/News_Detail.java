package com.org.palmcampus.oa.page;



import java.io.IOException;
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
import org.json.JSONException;
import org.json.JSONObject;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.pojo.ErpgongGao;
import com.org.palmcampus.oa.pojo.adapter.NewsDetailsPagination;
import com.org.palmcampus.oa.service.NewsService;
import com.org.palmcampus.oa.service.SysExitService;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class News_Detail extends Activity {
	private HttpPost request;
	private	HttpClient httpclient;
	private HttpResponse httpResponse; 
	private String responseStr=null;
	boolean result;
	private String userName=null;
	private EditText text_advice;
	/**  
	* @Fields strUrl : TODO(服务器地址)  
	*/ 
	String strUrl ;

	private TextView title_TextView;
	private LinearLayout root_LinearLayout;
	private Button return_Button;
	private ProgressDialog progressDialog;
	// 新闻对象
	ErpgongGao n = null;
	// 数据库
	private SQLiteDatabase db;
	private static EventData events;
	private Handler handler=new Handler()
	{	public void handleMessage(Message msg) {
		if(msg.what==0)
		{
			events = new EventData(News_Detail.this);
			db = events.getWritableDatabase();
			db.delete("gongGao", "id>0",null);
			progressDialog.cancel();
		
			Intent intent1 = new Intent(News_Detail.this, IndexActivity.class);
			startActivity(intent1);
			finish();
		}
		
		
	}
		
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_detail);
		SysExitUtil.activityList.add(this);
		strUrl=		strUrl=News_Detail.this.getResources().getString(R.string.hostedIp)+"/gongGaoLiuYan_phonea";
		// 接收值
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle = intent.getBundleExtra("bundle");

		if (bundle != null) {
			n = (ErpgongGao) bundle.getSerializable("data");
			// 设置新闻标题
			title_TextView = (TextView) findViewById(R.id.title_Text_TV);
			title_TextView.setText(n.getTitleStr());
			root_LinearLayout = (LinearLayout) findViewById(R.id.view_detail_root_LL);
			NewsDetailsPagination getView = new NewsDetailsPagination(
					News_Detail.this, n, root_LinearLayout);
			root_LinearLayout = getView.getViewData();
			// 添加留言
			Button button_ok = (Button) this.findViewById(R.id.btn_ok);
			button_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					 text_advice = (EditText) findViewById(R.id.advice);
					if (text_advice.getText().toString().equals("")) {
						Toast.makeText(getApplicationContext(), "留言不能为空", 4000).show();
						return;
					}
					 userName = "";
					// 取出数据库的值
					events = new EventData(News_Detail.this);
					db = events.getReadableDatabase();
					// 取出数据库用户表
					Cursor ResultSet = db.rawQuery("select * from Sys_Users",
							null);
					if (ResultSet.moveToNext()) {
						userName = ResultSet.getString(ResultSet
								.getColumnIndex("trueName"));
						System.out.println(userName);
					}
					

					// 如果留言不为空
					if (text_advice.getText() != null
							&& !text_advice.getText().equals("")) {
						System.out.println("启动线程开始连接服务器");

//						// 启动线程添加留言
//						Def_Thread thread = new Def_Thread(handler, bundle,
//								MethodName.ADDADVICE);
						new MyThread().t.start();
						progressDialog = new ProgressDialog(News_Detail.this);
						progressDialog.setMessage("正在修改，请稍等......");
						progressDialog.setIndeterminate(true);
						progressDialog
								.setOnCancelListener(new DialogInterface.OnCancelListener() {
									public void onCancel(DialogInterface dialog) {

									}
								});
						progressDialog.show();
//						thread.start();
					}

				}
			});
		}
		//取消按钮
		Button cancel_button=(Button)findViewById(R.id.btn_cancel);
		cancel_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				News_Detail.this.finish();
			}

		});
		// 返回按钮
		return_Button = (Button) findViewById(R.id.title_setting_Btn);
		return_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				News_Detail.this.finish();
			}

		});



	}
	/**
	 * 退出按钮
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
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
				  	//封装一个json
				 	JSONObject logindate = new JSONObject(); 
					
				 
			
						
						try {
							logindate.put("newsId", n.getId());
						 	logindate.put("advice", userName + ":"
									+ text_advice.getText().toString());
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
											result=job.getBoolean("res");
											if(result)
											{
												Message message=Message.obtain();
												message.what=0;
												News_Detail.this.handler.sendMessage(message);
												
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
