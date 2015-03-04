package com.org.palmcampus.oa.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
//实现数据库操作的类
public class EventData extends SQLiteOpenHelper{
	//数据库名称
	private static final String DATABASE_NAME = "PalmCampus";
	//数据库版本
	private static final int DATABASE_VERSION = 24;
	
	public EventData(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
		System.out.println("EventData启动");
	}
	
	public EventData(Context context, String name, CursorFactory factory) {
		this(context, name, factory,DATABASE_VERSION);
	}
	
	public EventData(Context context,String name,int version) {
		this(context,name,null,DATABASE_VERSION);
	}
	
	public EventData(Context context) {
		
		this(context,DATABASE_NAME,DATABASE_VERSION);
		System.out.println("创建数据库成功");
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("开始创建数据表");
		//登陆用户信息表
		db.execSQL("CREATE TABLE Sys_Users"+
				"(keyID Integer," +
				"userName varchar(50) ,userPwd varchar(200) ,trueName varchar(50) ,serils varchar(50) ," +
				"department varchar(50) ,jiaoSe varchar(50) ,activeTime datetime NULL," +
				"zhiWei varchar(50) ,zaiGang varchar(50) ,emailStr varchar(50) ," +
				"IfLogin varchar(50) ,Sex varchar(50) ,BackInfo varchar(8000) ," +
				"birthDay varchar(50) ,mingZu varchar(50) ,sFZSerils varchar(50) ," +
				"hunYing varchar(50) ,zhengZhiMianMao varchar(50) ,jiGuan varchar(50) ," +
				"huKou varchar(500) ,xueLi varchar(50) ,zhiCheng varchar(50) ," +
				"biYeYuanXiao varchar(50) ,zhuanYe varchar(50) ,canJiaGongZuoTime varchar(50) ," +
				"jiaRuBenDanWeiTime varchar(50) ,jiaTingDianHua varchar(50) ,JiaTingAddress varchar(500) ," +
				"GangWeiBianDong text ,JiaoYueBeiJing text ,GongZuoJianLi text ," +
				"SheHuiGuanXi text ,JiangChengJiLu text ,ZhiWuQingKuang text ," +
				"PeiXunJiLu text ,DanBaoJiLu text ,NaoDongHeTong text ," +
				"SheBaoJiaoNa text ,TiJianJiLu text ,BeiZhuStr text ," +
				"FuJian varchar(5000) ,POP3UserName varchar(50) ,POP3UserPwd varchar(50) ," +
				"POP3Server varchar(50) ,POP3Port varchar(50) ,SMTPUserName varchar(50) ," +
				"SMTPUserPwd varchar(50) ,SMTPServer varchar(50) ,SMTPFromEmail varchar(50) ," +
				"TiXingTime varchar(50) ,IfTiXing varchar(50) ,isRecordPWD Integer,isAutoLogin Integer)");
		//公告表
		db.execSQL("CREATE TABLE gongGao"+
		"(keyID Integer PRIMARY KEY AUTOINCREMENT,id Integer,titleStr varchar(500) NULL,"+
				"timeStr varchar(500) NULL,userName varchar(50) NULL,userBuMen varchar(50) NULL,fuJian varchar(2000) NULL," +
				"contentStr text(2000) NULL,typeStr varchar(50) NULL,chuanYueYiJian text NULL,yiJieShouRen varchar(8000) NULL,isNew Integer,isTop varchar(10))");
		//新闻信息表
		db.execSQL("CREATE TABLE news"+
				"(keyID Integer PRIMARY KEY AUTOINCREMENT,id Integer,titleStr varchar(40),typeStr varchar(20),contentStr varchar(1000),userName varchar(30),sendee varchar(200),reader varchar(200),readerAdvice varchar(1000),isTop varchar(10),isNew Integer,publishDate varchar(100))");
		//通知信息表(是否开启新闻和邮件通知,0代表开启，1代表关闭)
	    db.execSQL("CREATE TABLE notification"+
						"(keyID Integer PRIMARY KEY AUTOINCREMENT,news_notify Integer,mail_notify Integer)");
	    ContentValues values=new ContentValues();
	    values.put("news_notify", 0);
	    values.put("mail_notify", 0);
	    db.insert("notification", null, values);
		System.out.println("创建数据表成功");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("更新表");
		db.execSQL("DROP TABLE IF EXISTS Sys_Users");
		db.execSQL("DROP TABLE IF EXISTS news");
		db.execSQL("DROP TABLE IF EXISTS notification");
		db.execSQL("DROP TABLE IF EXISTS gongGao");
		onCreate(db);
	}

}
