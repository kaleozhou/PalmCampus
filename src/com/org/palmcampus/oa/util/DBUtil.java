package com.org.palmcampus.oa.util;




import com.org.palmcampus.oa.sql.EventData;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBUtil {
	// ���ݿ�
	private static SQLiteDatabase db;
	private static EventData events;
	public static int getCount(Context context,String tableName){
		events = new EventData(context);
		db = events.getReadableDatabase();
		int count=0;
		Cursor ResultSet = db.rawQuery("select count(*) from "+tableName, null);
		if (ResultSet.moveToNext()) {
			count = Integer.parseInt(ResultSet.getString(0));
			System.out.println("��¼����"+count);
		}
		// �ر����ݿ�
		ResultSet.close();
		db.close();
		return count;
	}
}
