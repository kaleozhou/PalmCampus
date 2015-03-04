/**  
 * @Title: DataServer.java 
 * @Package com.org.palmcampus.oa.custom 
 * @Description: TODO 
 * @author kaleo  
 * @date 2014-9-16 下午3:59:54 
 * @version V1.0  
 */

package com.org.palmcampus.oa.custom;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import net.sf.json.JSON;

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

import android.graphics.SweepGradient;
import android.os.Handler;
import android.os.Message;

/**
 * @ClassName: DataServer
 * @Description:
 * @author: kaleo
 * @date 2014-9-16 下午3:59:54
 * 
 */

public class DataThread extends Thread {
	private HttpPost request;
	private HttpClient httpclient;
	private HttpResponse httpResponse;
	private String responseStr = null;
	int methodName;
	private JSONObject data = null;
	private Handler handler = null;
	String strUrl;

	public DataThread(JSONObject data, String StrUrl, Handler handler,
			int methodName) {
		this.data = data;
		this.strUrl = StrUrl;
		this.handler = handler;
		this.methodName = methodName;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		// 访问服务器的action
		request = new HttpPost(strUrl);
		httpclient = new DefaultHttpClient();
		Message message = handler.obtainMessage();
		// 绑定到请求entry,设置中文支持
		StringEntity se;
		try {
			se = new StringEntity(data.toString(), HTTP.UTF_8);
			request.setEntity(se);
			httpResponse = httpclient.execute(request);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					try {
						responseStr = URLDecoder.decode(
								EntityUtils.toString(entity, HTTP.UTF_8),
								"utf-8");

						switch (methodName) {
						case MethodName.CHANGEPASSWORD:// 修改密码
							message.what = 0;
							JSONObject job=new JSONObject(responseStr);
							
							message.obj=job;
							break;

						default:
							break;
						}

					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						//修改密码的
						e.printStackTrace();
					}
				}
			}
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

		handler.sendMessage(message);
	}

}
