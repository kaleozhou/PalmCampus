/**  
* @Title: SysExitService.java 
* @Package com.org.palmcampus.oa.service 
* @Description: TODO 
* @author kaleo  
* @date 2014-8-14 下午12:04:47 
* @version V1.0  
*/ 

package com.org.palmcampus.oa.service;

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

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/** 
 * @ClassName: SysExitService 
 * @Description:  
 * @author: kaleo 
 * @date 2014-8-14 下午12:04:47 
 *  
 */

public class SysExitService extends Service {
	private String tag="退出service";
	private HttpPost request;
	private	HttpClient httpclient;
	private HttpResponse httpResponse; 
	private String responseStr=null,result;
	private IBinder mybinder;
	private String username=null;
	/**  
	* @Fields strUrl : TODO(服务器地址)  
	*/ 
	String strUrl ;

	/* (非 Javadoc) 
	 * <p>Title: onBind</p>
	 * <p>Description: </p>
	 * @param arg0
	 * @return 
	 * @see android.app.Service#onBind(android.content.Intent) 
	 */

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		Log.i(tag, "onBind方法");
		username=arg0.getStringExtra("username");
		new MyThread().t.start();
		
		return null;
	}
	public class Mybinder extends Binder{
		public String getResualt(){
			
			return result;
			
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		username=intent.getStringExtra("username").toString();
		new MyThread().t.start();
		super.onStart(intent, startId);
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		strUrl=SysExitService.this.getResources().getString(R.string.hostedIp)+"/sysexit_phonea";
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
	
		super.onDestroy();
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
							logindate.put("username",username);
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
											result=job.toString();
											
									
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
