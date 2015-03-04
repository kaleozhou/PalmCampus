package com.org.palmcampus.oa.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
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
import com.org.palmcampus.oa.page.MainActivity;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Message;

public class SysExitUtil {
	
	//建立一个public static的list用来放activity 
    public static List activityList = new ArrayList(); 

	
    
      //finish所有list中的activity 
    public static void exit(){    
        int siz=activityList.size();     
        for(int i=0;i<siz;i++){        
            if(activityList.get(i)!=null){            
                ((Activity) activityList.get(i)).finish(); 
               	
                }     
            } 
    }
    
  

    
}
