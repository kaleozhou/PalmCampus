package com.org.palmcampus.oa.page;
import org.json.JSONException;
import org.json.JSONObject;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.custom.DataThread;
import com.org.palmcampus.oa.custom.MethodName;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.SysExitUtil;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.JetPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/** 
* @ClassName: Config_Password 
* @Description:  修改密码的界面
* @author: kaleo 
* @date 2014-9-16 下午3:45:09 
*  
*/ 
public class Config_Password extends Activity {
	private TextView title_TextView;
	private EditText newpassword_EditText;
	private Button setting_Button;
	private EditText confirmpassword_EditText;
	private ProgressDialog progressDialog;
	private DataThread das;
	// 数据库
	private SQLiteDatabase db;
	private static EventData events;
	private int userId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_password);
		SysExitUtil.activityList.add(this);
		title_TextView = (TextView) findViewById(R.id.title_Text_TV);
		title_TextView.setText("密码修改");
		newpassword_EditText = (EditText) findViewById(R.id.new_password);
		confirmpassword_EditText = (EditText) findViewById(R.id.confirm_new_password);
		setting_Button = (Button) findViewById(R.id.title_setting_Btn);
		setting_Button.setText("保存修改");
	
		setting_Button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (newpassword_EditText.getText().toString().equals("")
						|| confirmpassword_EditText.getText().toString()
								.equals("")) {
					Toast.makeText(Config_Password.this, "密码或者确认密码不能为空", 4000)
							.show();
					return;
				}
				if (!newpassword_EditText.getText().toString()
						.equals(confirmpassword_EditText.getText().toString())) {
					Toast.makeText(Config_Password.this, "密码和确认密码不一致", 4000)
							.show();
					return;
				}
				// 取出数据库的值
				events = new EventData(Config_Password.this);
				db = events.getReadableDatabase();
				// 取出数据库用户表
				Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);

				// 如果有记录
				if (ResultSet.moveToNext()) {
					userId = ResultSet.getInt(ResultSet.getColumnIndex("keyID"));
					System.out.println("用户号" + userId);
				}
				ResultSet.close();
				JSONObject data = new JSONObject();
				
				try {
					data.put("userId", userId);
					data.put("newpassword", newpassword_EditText.getText()
							.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String StrUrl=Config_Password.this.getResources().getString(R.string.hostedIp)+
						"/changePwd_phonea";
				// Def_Thread thread = new Def_Thread(handler, bundle,
				// MethodName.CHANGEPASSWORD);
				// 新建json对象传递数据
			
				das=new DataThread(data, StrUrl,handler,MethodName.CHANGEPASSWORD);
				
				
				progressDialog = new ProgressDialog(Config_Password.this);
				progressDialog.setMessage("正在修改密码，请稍等......");
				progressDialog.setIndeterminate(true);
				progressDialog
						.setOnCancelListener(new DialogInterface.OnCancelListener() {
							public void onCancel(DialogInterface dialog) {

							}
						});
				progressDialog.show();
				das.start();
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
	 * Handler接收返回值（添加客户后结果）
	 */
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			progressDialog.dismiss();
			JSONObject job=(JSONObject)msg.obj;
			try {
				String data=job.getString("data");
			
			if (msg.what==0) {

				Toast.makeText(Config_Password.this, "密码修改成功"+data,
						Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(Config_Password.this, "网络连接失败，请重试",
						Toast.LENGTH_LONG).show();
			}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	
}
