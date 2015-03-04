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
	* @Fields strUrl : TODO(��������ַ)  
	*/ 
	String strUrl ;

	private TextView title_TextView;
	private LinearLayout root_LinearLayout;
	private Button return_Button;
	private ProgressDialog progressDialog;
	// ���Ŷ���
	ErpgongGao n = null;
	// ���ݿ�
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
		// ����ֵ
		Intent intent = getIntent();
		Bundle bundle = new Bundle();
		bundle = intent.getBundleExtra("bundle");

		if (bundle != null) {
			n = (ErpgongGao) bundle.getSerializable("data");
			// �������ű���
			title_TextView = (TextView) findViewById(R.id.title_Text_TV);
			title_TextView.setText(n.getTitleStr());
			root_LinearLayout = (LinearLayout) findViewById(R.id.view_detail_root_LL);
			NewsDetailsPagination getView = new NewsDetailsPagination(
					News_Detail.this, n, root_LinearLayout);
			root_LinearLayout = getView.getViewData();
			// �������
			Button button_ok = (Button) this.findViewById(R.id.btn_ok);
			button_ok.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					 text_advice = (EditText) findViewById(R.id.advice);
					if (text_advice.getText().toString().equals("")) {
						Toast.makeText(getApplicationContext(), "���Բ���Ϊ��", 4000).show();
						return;
					}
					 userName = "";
					// ȡ�����ݿ��ֵ
					events = new EventData(News_Detail.this);
					db = events.getReadableDatabase();
					// ȡ�����ݿ��û���
					Cursor ResultSet = db.rawQuery("select * from Sys_Users",
							null);
					if (ResultSet.moveToNext()) {
						userName = ResultSet.getString(ResultSet
								.getColumnIndex("trueName"));
						System.out.println(userName);
					}
					

					// ������Բ�Ϊ��
					if (text_advice.getText() != null
							&& !text_advice.getText().equals("")) {
						System.out.println("�����߳̿�ʼ���ӷ�����");

//						// �����߳��������
//						Def_Thread thread = new Def_Thread(handler, bundle,
//								MethodName.ADDADVICE);
						new MyThread().t.start();
						progressDialog = new ProgressDialog(News_Detail.this);
						progressDialog.setMessage("�����޸ģ����Ե�......");
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
		//ȡ����ť
		Button cancel_button=(Button)findViewById(R.id.btn_cancel);
		cancel_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				News_Detail.this.finish();
			}

		});
		// ���ذ�ť
		return_Button = (Button) findViewById(R.id.title_setting_Btn);
		return_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				News_Detail.this.finish();
			}

		});



	}
	/**
	 * �˳���ť
	 * @param 
	 */
	public void SysExit(View v){
		
		SysExitUtil.exit();
		
		
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
				  	//��װһ��json
				 	JSONObject logindate = new JSONObject(); 
					
				 
			
						
						try {
							logindate.put("newsId", n.getId());
						 	logindate.put("advice", userName + ":"
									+ text_advice.getText().toString());
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
