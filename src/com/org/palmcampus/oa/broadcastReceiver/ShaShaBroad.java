/**  
* @Title: ShaShaBroad.java 
* @Package com.org.palmcampus.oa.broadcastReceiver 
* @Description: TODO 
* @author kaleo  
* @date 2014-8-27 ����1:28:34 
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
 * @Description:  ֪ͨ�ʼ��Ĺ㲥
 * @author: kaleo 
 * @date 2014-8-27 ����1:28:34 
 *  
 */

public class ShaShaBroad extends BroadcastReceiver {


	/* (�� Javadoc) 
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
		//1�������ʼ����ѣ�0�ǹ�������
		if(who==1)
		{
		int count=arg1.getIntExtra("count", 2);
		
		//��Ϣ֪ͨ��

        //����NotificationManager
		String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager)arg0.getSystemService(ns);
		// ����Notification�ĸ�������
		int icon = R.drawable.home_tab_letter; //֪ͨͼ��
		CharSequence tickerText = "�����µ��ʼ�"; //״̬����ʾ��֪ͨ�ı���ʾ
		long when = System.currentTimeMillis(); //֪ͨ������ʱ�䣬����֪ͨ��Ϣ����ʾ
		//����������Գ�ʼ�� Nofification
		Notification notification = new Notification(icon,tickerText,when);
		notification.defaults |=Notification.DEFAULT_SOUND;
		notification.defaults |= Notification.DEFAULT_VIBRATE;
		/*
		* �������
		* notification.defaults |=Notification.DEFAULT_SOUND;
		* ����ʹ�����¼��ַ�ʽ
		* notification.sound = Uri.parse("file:///sdcard/notification/ringer.mp3");
		* notification.sound = Uri.withAppendedPath(Audio.Media.INTERNAL_CONTENT_URI, "6");
		* �����Ҫ�����������ظ�ֱ���û���֪ͨ������Ӧ���������notification��flags�ֶ�����"FLAG_INSISTENT"
		* ���notification��defaults�ֶΰ�����"DEFAULT_SOUND"���ԣ���������Խ�����sound�ֶ��ж��������
		*/
		
		//��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
		/*
		* �����
		* notification.defaults |= Notification.DEFAULT_VIBRATE;
		* ���߿��Զ����Լ�����ģʽ��
		* long[] vibrate = {0,100,200,300}; //0�����ʼ�񶯣���100�����ֹͣ���ٹ�200������ٴ���300����
		* notification.vibrate = vibrate;
		* long������Զ������Ҫ���κγ���
		* ���notification��defaults�ֶΰ�����"DEFAULT_VIBRATE",��������Խ�����vibrate�ֶ��ж������
		*/
		/*
		* ���LED������
		* notification.defaults |= Notification.DEFAULT_LIGHTS;
		* ���߿����Լ���LED����ģʽ:
		* notification.ledARGB = 0xff00ff00;
		* notification.ledOnMS = 300; //����ʱ��
		* notification.ledOffMS = 1000; //���ʱ��
		* notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		*/
		
		/*
		* �������������
		* notification.flags |= FLAG_AUTO_CANCEL; //��֪ͨ���ϵ����֪ͨ���Զ������֪ͨ
		* notification.flags |= FLAG_INSISTENT; //�ظ�����������ֱ���û���Ӧ��֪ͨ
		* notification.flags |= FLAG_ONGOING_EVENT; //����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
		* notification.flags |= FLAG_NO_CLEAR; //�����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������
		* //������FLAG_ONGOING_EVENTһ��ʹ��
		* notification.number = 1; //number�ֶα�ʾ��֪ͨ����ĵ�ǰ�¼�����������������״̬��ͼ��Ķ���
		* //���Ҫʹ�ô��ֶΣ������1��ʼ
		* notification.iconLevel = ; //
		*/
		
		//����֪ͨ���¼���Ϣ
		Context context = arg0.getApplicationContext(); //������
		CharSequence contentTitle = "�µ��ʼ�"; //֪ͨ������
		CharSequence contentText = "�յ�"+count+"�����ʼ�"; //֪ͨ������
		Intent notificationIntent = new Intent(arg0,MailBoxActivity.class); //�����֪ͨ��Ҫ��ת��Activity
		PendingIntent contentIntent = PendingIntent.getActivity(arg0,0,notificationIntent,0);
		notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
		//��Notification���ݸ� NotificationManager
		mNotificationManager.notify(0,notification);
		}
		else
		{
			int count=arg1.getIntExtra("count", 2);
			
			//��Ϣ֪ͨ��

	        //����NotificationManager
			String ns = Context.NOTIFICATION_SERVICE;
			 NotificationManager mNotificationManager = (NotificationManager)arg0.getSystemService(ns);
			// ����Notification�ĸ�������
			int icon = R.drawable.personal_notice; //֪ͨͼ��
			CharSequence tickerText = "�����µĹ���"; //״̬����ʾ��֪ͨ�ı���ʾ
			long when = System.currentTimeMillis(); //֪ͨ������ʱ�䣬����֪ͨ��Ϣ����ʾ
			//����������Գ�ʼ�� Nofification
			Notification notification = new Notification(icon,tickerText,when);
			notification.defaults |=Notification.DEFAULT_SOUND;
			notification.defaults |= Notification.DEFAULT_VIBRATE;
			
			//����֪ͨ���¼���Ϣ
			Context context = arg0.getApplicationContext(); //������
			CharSequence contentTitle = "�µĹ���"; //֪ͨ������
			CharSequence contentText = "�յ�"+count+"�⹫��"; //֪ͨ������
			Intent notificationIntent = new Intent(arg0,News_List.class); //�����֪ͨ��Ҫ��ת��Activity
			PendingIntent contentIntent = PendingIntent.getActivity(arg0,0,notificationIntent,0);
			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
			//��Notification���ݸ� NotificationManager
			mNotificationManager.notify(1,notification);
		}
	
	
		


		

		
	}

}
