package com.org.palmcampus.oa.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
//email数据库
public class DButil extends SQLiteOpenHelper{
	public DButil(Context context) {
		super(context, "emailconstants.db", null, 32);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//邮件表,isNew代表是否是新邮件，0代表已读邮件，1代表未读
		db.execSQL("create table email(id INTEGER PRIMARY KEY AUTOINCREMENT,messageid INTEGER,title varchar(20),mailfrom varchar(20),mailto varchar(100),cc varchar(100),sendDate varchar(100),content text,attachment varchar(200),isNew INTEGER)");
		//草稿箱表
		db.execSQL("create table caogaoxiang(id INTEGER PRIMARY KEY AUTOINCREMENT,mailfrom varchar(20),mailto varchar(100),cc varchar(100),subject varchar(30),content text)");
		//附件表
		db.execSQL("create table attachment(id INTEGER PRIMARY KEY AUTOINCREMENT,filename varchar(20),filepath varchar(100),filesize varchar(20),mailid varchar(20))");
		//已读邮件
		db.execSQL("create table emailstatus(id INTEGER PRIMARY KEY AUTOINCREMENT,mailfrom varchar(20),messageid varchar(100))");
		//未读邮件（存储数量）
		db.execSQL("create table emailnotread(id INTEGER PRIMARY KEY AUTOINCREMENT,count INTEGER)");
		//联系人表（员工）
		db.execSQL("create table emp(id INTEGER PRIMARY KEY AUTOINCREMENT,empNo INTEGER,empName varchar(20),mobile varchar(20),jiaose varchar(20),zhiwei varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("更新表");
		db.execSQL("DROP TABLE IF EXISTS email");
		db.execSQL("DROP TABLE IF EXISTS caogaoxiang");
		db.execSQL("DROP TABLE IF EXISTS attachment");
		db.execSQL("DROP TABLE IF EXISTS emailstatus");
		db.execSQL("DROP TABLE IF EXISTS emp");
		db.execSQL("DROP TABLE IF EXISTS emailnotread");
		onCreate(db);
	}

}
