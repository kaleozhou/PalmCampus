/**  
* @Title: ShaShaBroad.java 
* @Package com.org.palmcampus.oa.broadcastReceiver 
* @Description: TODO 
* @author kaleo  
* @date 2014-8-27 下午1:28:34 
* @version V1.0  
*/ 

package com.org.palmcampus.oa.broadcastReceiver;

import com.org.palmcampus.R;
import com.org.palmcampus.email.MailBoxActivity;
import com.org.palmcampus.oa.page.IndexActivity;
import com.org.palmcampus.oa.page.MainActivity;
import com.org.palmcampus.oa.page.News_List;

import android.animation.AnimatorSet.Builder;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

/** 
 * @ClassName: ShaShaBroad 
 * @Description:  通知邮件的广播
 * @author: kaleo 
 * @date 2014-8-27 下午1:28:34 
 *  
 */

public class ShaShaBroad extends BroadcastReceiver {


	/* (非 Javadoc) 
	 * <p>Title: onReceive</p>
	 * <p>Description: </p>
	 * @param arg0
	 * @param arg1 
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent) 
	 */

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		
		int who=arg1.getIntExtra("who", 1);
		//1代表是邮件提醒，0是公告提醒
		if(who==1)
		{
		int count=arg1.getIntExtra("count", 2);
		
		//消息通知栏

        //定义NotificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager)arg0.getSystemService(ns);
		// 定义Notification的各种属性
		int icon = R.drawable.home_tab_letter; //通知图标
		CharSequence tickerText = "你有新的邮件"; //状态栏显示的通知文本提示
		long when = System.currentTimeMillis(); //通知产生的时间，会在通知信息里显示
		//用上面的属性初始化 Nofification
		Notification notification = new Notification(icon,tickerText,when);
		notification.defaults |=Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		/*
		* 添加声音
		* notification.defaults |=Notification.DEFAULT_SOUND;
		* 或者使用以下几种方式
		* notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
		* notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		* 如果想要让声音持续重复直到用户对通知做出反应，则可以在notification的flags字段增加"FLAG_INSISTENT"
		* 如果notification的defaults字段包括了"DEFAULT_SOUND"属性，则这个属性将覆盖sound字段中定义的声音
		*/
		
		//在通知栏上点击此通知后自动清除此通知
		/*
		* 添加振动
		* notification.defaults |= Notification.DEFAULT_VIBRATE;
		* 或者可以定义自己的振动模式：
		* long[] vibrate = {0,100,200,300}; //0毫秒后开始振动，振动100毫秒后停止，再过200毫秒后再次振动300毫秒
		* notification.vibrate = vibrate;
		* long数组可以定义成想要的任何长度
		* 如果notification的defaults字段包括了"DEFAULT_VIBRATE",则这个属性将覆盖vibrate字段中定义的振动
		*/
		/*
		* 添加LED灯提醒
		* notification.defaults |= Notification.DEFAULT_LIGHTS;
		* 或者可以自己的LED提醒模式:
		* notification.ledARGB = 0xff00ff00;
		* notification.ledOnMS = 300; //亮的时间
		* notification.ledOffMS = 1000; //灭的时间
		* notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		*/
		
		/*
		* 更多的特征属性
		* notification.flags |= FLAG_AUTO_CANCEL; //在通知栏上点击此通知后自动清除此通知
		* notification.flags |= FLAG_INSISTENT; //重复发出声音，直到用户响应此通知
		* notification.flags |= FLAG_ONGOING_EVENT; //将此通知放到通知栏的"Ongoing"即"正在运行"组中
		* notification.flags |= FLAG_NO_CLEAR; //表明在点击了通知栏中的"清除通知"后，此通知不清除，
		* //经常与FLAG_ONGOING_EVENT一起使用
		* notification.number = 1; //number字段表示此通知代表的当前事件数量，它将覆盖在状态栏图标的顶部
		* //如果要使用此字段，必须从1开始
		* notification.iconLevel = ; //
		*/
		
		//设置通知的事件消息
		Context context = arg0.getApplicationContext(); //上下文
		CharSequence contentTitle = "新的邮件"; //通知栏标题
		CharSequence contentText = "收到"+count+"封信邮件"; //通知栏内容
		Intent notificationIntent = new Intent(arg0,MailBoxActivity.class); //点击该通知后要跳转的Activity
		PendingIntent contentIntent = PendingIntent.getActivity(arg0,0,notificationIntent,0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		//把Notification传递给 NotificationManager
		mNotificationManager.notify(0,notification);
		}
		else
		{
			int count=arg1.getIntExtra("count", 2);
			
			//消息通知栏

	        //定义NotificationManager
			String ns = Context.NOTIFICATION_SERVICE;
			 NotificationManager mNotificationManager = (NotificationManager)arg0.getSystemService(ns);
			// 定义Notification的各种属性
			int icon = R.drawable.personal_notice; //通知图标
			CharSequence tickerText = "你有新的公告"; //状态栏显示的通知文本提示
			long when = System.currentTimeMillis(); //通知产生的时间，会在通知信息里显示
			//用上面的属性初始化 Nofification
			Notification notification = new Notification(icon,tickerText,when);
			notification.defaults |=Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			
			//设置通知的事件消息
			Context context = arg0.getApplicationContext(); //上下文
			CharSequence contentTitle = "新的公告"; //通知栏标题
			CharSequence contentText = "收到"+count+"封公告"; //通知栏内容
			Intent notificationIntent = new Intent(arg0,News_List.class); //点击该通知后要跳转的Activity
			PendingIntent contentIntent = PendingIntent.getActivity(arg0,0,notificationIntent,0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			//把Notification传递给 NotificationManager
			mNotificationManager.notify(1,notification);
		}
	
	
		


		

		
	}

}
