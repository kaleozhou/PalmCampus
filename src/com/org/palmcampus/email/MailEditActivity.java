package com.org.palmcampus.email;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

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
import org.kobjects.base64.Base64;

import com.org.palmcampus.R;
import com.org.palmcampus.email.adapter.GridViewAdapter;
import com.org.palmcampus.email.bean.Attachment;
import com.org.palmcampus.email.bean.Email;
import com.org.palmcampus.oa.custom.Config;
import com.org.palmcampus.oa.custom.MethodName;
import com.org.palmcampus.oa.sql.EventData;
import com.org.palmcampus.oa.util.IOUtil;
import com.org.palmcampus.oa.util.ImageUtil;
import com.org.palmcampus.oa.util.SysExitUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class MailEditActivity extends Activity implements OnClickListener {
	private EditText mail_to;
	private EditText mail_from;
	private EditText mail_cc;
	private EditText mail_topic;
	private EditText mail_content;

	private Button send;
	private ImageButton add_lianxiren;
	private ImageButton add_lianxiren2;
	private ImageButton attachment;
	private GridView gridView;
	private GridViewAdapter<Attachment> adapter = null;
	private ProgressDialog progressDialog;
	// 判断是否来自草稿箱的id，如果一直为-1，说明没有来自草稿箱
	private int mailid = -1;
	// 员工姓名
	String userName = "";
	// 员工号
	int empNo = 0;
	// 上传附件的字符串
	private String imageBuffer = null;
	// 上传附件的文件名
	private String fileName = null;
	// 数据库
	private SQLiteDatabase db;
	private static EventData events;
	private String strUrl;

	private static final int SUCCESS = 1;
	private static final int FAILED = -1;
	private boolean isCaogaoxiang = true;
	private ProgressDialog dialog;
	// HttpUtil util=new HttpUtil();
	private Handler handler = new Handler() {

		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		
			if (msg.what == 0) {
				progressDialog.dismiss();
			// 如果发送的邮件来自草稿箱，则删除草稿箱该邮件，以及附件表的内容
				if (mailid > 0) {
					;
					getContentResolver().delete(Config.CAOGAO_URI, "id=?",
							new String[] { mailid + "" });
					getContentResolver().delete(Config.ATTACHMENT, "mailid=?",
							new String[] { mailid + "" });
				}
				Toast.makeText(MailEditActivity.this, "邮件发送成功",
						Toast.LENGTH_LONG).show();
				// 修改成功后返回主界面
				// Intent intent = new Intent(MailEditActivity.this,
				// Email_Home.class);
				// startActivity(intent);
				finish();
			} else
				Toast.makeText(MailEditActivity.this, "邮件发送失败，请检查网络并重试",
						Toast.LENGTH_LONG).show();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(this);
		setContentView(R.layout.email_writer);
		strUrl = this.getResources().getString(R.string.hostedIp)
				+ "/sendEmail_phonea";
		init();
	}

	/**
	 * 初始化
	 */
	private void init() {
		mail_to = (EditText) findViewById(R.id.mail_to);
		mail_from = (EditText) findViewById(R.id.mail_from);
		mail_cc = (EditText) findViewById(R.id.mail_cc);
		mail_topic = (EditText) findViewById(R.id.mail_topic);
		mail_content = (EditText) findViewById(R.id.content);
		send = (Button) findViewById(R.id.send);
		attachment = (ImageButton) findViewById(R.id.add_att);
		add_lianxiren = (ImageButton) findViewById(R.id.add_lianxiren);
		add_lianxiren2 = (ImageButton) findViewById(R.id.add_lianxiren2);
		gridView = (GridView) findViewById(R.id.pre_view);

		// mail_from.setText(MyApplication.info.getUserName());
		send.setOnClickListener(this);
		attachment.setOnClickListener(this);
		add_lianxiren.setOnClickListener(this);
		add_lianxiren2.setOnClickListener(this);
		adapter = new GridViewAdapter<Attachment>(this);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new MyOnItemClickListener());

		// 取出数据库的值
		events = new EventData(MailEditActivity.this);
		db = events.getReadableDatabase();
		// 取出数据库用户表
		Cursor ResultSet = db.rawQuery("select * from Sys_Users", null);

		// 如果有记录
		if (ResultSet.moveToNext()) {
			userName = ResultSet
					.getString(ResultSet.getColumnIndex("trueName"));

		}
		// 关闭数据库
		ResultSet.close();
		db.close();
		mail_from.setText(userName);
		// 判断是否是回复邮件
		int type = getIntent().getIntExtra("TYPE", -1);
		System.out.println("-----type" + type);
		if (type == 1) {
			Email email = (Email) getIntent().getSerializableExtra("EMAIL");
			mail_to.setText(email.getFrom());
			mail_cc.setText(email.getCc());
			mail_content.setText("邮件原始内容" + email.getContent());
			mail_content.append("\n----------------------");
		}
		// 判断是否从草稿箱来的
		mailid = getIntent().getIntExtra("mailid", -1);
		if (mailid > -1) {

			Cursor c = getContentResolver().query(Config.CAOGAO_URI, null,
					"mailfrom=? and id=?",
					new String[] { userName, mailid + "" }, null);
			if (c.moveToNext()) {
				mail_to.setText(c.getString(2));
				mail_cc.setText(c.getString(3));
				mail_topic.setText(c.getString(4));
				mail_content.setText(c.getString(5));
			}
			// 读取草稿箱里的附件

			c = getContentResolver().query(Config.ATTACHMENT, null, "mailid=?",
					new String[] { mailid + "" }, null);
			List<Attachment> attachments = new ArrayList<Attachment>();
			// 如果有附件
			while (c.moveToNext()) {
				Attachment att = new Attachment(c.getString(2), c.getString(1),
						c.getLong(3));

				att.setImage(ImageUtil.getSmallBitmap(c.getString(2)));
				byte[] data = ImageUtil.Bitmap2Bytes(att.getImage());
				imageBuffer = new String(Base64.encode(data));
				attachments.add(att);
			}

			// 显示草稿箱里的附件
			if (attachments.size() > 0) {
				for (Attachment affInfos : attachments) {
					adapter.appendToList(affInfos);
					int a = adapter.getList().size();
					int count = (int) Math.ceil(a / 4.0);
					gridView.setLayoutParams(new LayoutParams(
							LinearLayout.LayoutParams.MATCH_PARENT,
							(int) (94 * 1.5 * count)));
				}
			}

		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.send:
			sendMail();
			break;
		case R.id.add_att:
			addAttachment();
			break;
		case R.id.add_lianxiren:
			Intent intent = new Intent(MailEditActivity.this,
					MailAddContact.class);
			startActivityForResult(intent, 2);
			break;
		case R.id.add_lianxiren2:
			Intent intent2 = new Intent(MailEditActivity.this,
					MailAddContact.class);
			startActivityForResult(intent2, 3);
			break;
		}

	};

	/**
	 * 添加附件
	 */
	private void addAttachment() {
		// 一次只能发送一个附件
		if (adapter.getList().size() >= 1) {
			Toast.makeText(MailEditActivity.this, "为了不影响网络传输，一次只能发送一个附件", 5000)
					.show();
			return;
		}
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		System.out.println("gogo");
		try {
			startActivityForResult(Intent.createChooser(intent, "请选择要上传的文件"), 1);
		} catch (android.content.ActivityNotFoundException ex) {
			Toast.makeText(this, "无法选择文件,请先安装文件管理器", Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 1:
				System.out.println("开始选择附件");
				Uri uri = null;
				if (data != null) {
					System.out.println("有内容");
					uri = data.getData();

				}
				// 获取附件的完整路径
				String path = IOUtil.getPath(this, uri);
				System.out.println(path);
				/**
				 * 这是之前邮件发送图片附件的代码 ContentResolver resolver =
				 * getContentResolver();
				 * 
				 * String[] proj = { MediaStore.Images.Media.DATA };
				 * 
				 * Cursor actualimagecursor = managedQuery(uri, proj, null,
				 * null, null);
				 * 
				 * int actual_image_column_index = actualimagecursor
				 * .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				 * 
				 * actualimagecursor.moveToFirst();
				 * 
				 * String img_path = actualimagecursor
				 * .getString(actual_image_column_index);
				 * System.out.println("--------" + img_path);
				 * 
				 * Bitmap image; Bitmap photo = null; try { image =
				 * MediaStore.Images.Media.getBitmap(resolver, uri); //
				 * 400*800为压缩图片后重新生成的宽高像素 photo = ImageUtil.zoomBitmap(image,
				 * 600, 800);
				 * 
				 * // 垃圾回收，回收原始图片，以免内存溢出 // image.recycle(); } catch
				 * (FileNotFoundException e) { // TODO Auto-generated catch
				 * block e.printStackTrace(); } catch (IOException e) { // TODO
				 * Auto-generated catch block e.printStackTrace(); }
				 **/
				// String path = uri.getPath();
				//
				// System.out.println("路径为"+path);
				Attachment affInfos = new Attachment();
				// 获取文件名
				fileName = Attachment.getNameFromFilepath(path);
				affInfos.setFileName(fileName);
				affInfos.setFilePath(path);
				// byte[] data2 = ImageUtil.Bitmap2Bytes(photo);
				byte[] data2 = null;
				try {
					data2 = IOUtil.getFileBytes(path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 将byte[]转换为可以用Ksoap2上传的字符串（此处必须转换，否则ksoap无法识别）
				imageBuffer = new String(Base64.encode(data2));
				// 设置文件大小
				affInfos.setFileSize(data2.length);
				// affInfos.setImage(photo);
				affInfos.setImageBuffer(imageBuffer);
				System.out.println(affInfos);
				adapter.appendToList(affInfos);
				int a = adapter.getList().size();
				System.out.println("附件个数" + a);
				int count = (int) Math.ceil(a / 4.0);
				gridView.setLayoutParams(new LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						(int) (94 * 1.5 * count)));
				break;
			}
		}

		/**
		 * 多个联系人
		 */
		if (requestCode == 2) {
			List<String> chooseUsers = data
					.getStringArrayListExtra("chooseUsers");
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < chooseUsers.size(); i++) {
				if (i == chooseUsers.size() - 1) {
					str.append(chooseUsers.get(i));
				} else {
					str.append(chooseUsers.get(i) + ",");
				}
			}
			mail_to.setText(str.toString());

		}
		if (requestCode == 3) {
			List<String> chooseUsers = data
					.getStringArrayListExtra("chooseUsers");
			StringBuilder str = new StringBuilder();
			for (int i = 0; i < chooseUsers.size(); i++) {
				if (i == chooseUsers.size() - 1) {
					str.append(chooseUsers.get(i));
				} else {
					str.append(chooseUsers.get(i) + ",");
				}
			}
			mail_cc.setText(str.toString());

		}
	}

	/**
	 * 发送邮件，设置邮件数据
	 */

	private void sendMail() {
		if (mail_topic.getText().toString().trim().equals("")) {
			Toast.makeText(MailEditActivity.this, "邮件主题必须设置",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (mail_to.getText().toString().trim().equals("")) {
			Toast.makeText(MailEditActivity.this, "收件人必须设置", Toast.LENGTH_SHORT)
					.show();
			return;
		}
		if (mail_content.getText().toString().trim().equals("")) {
			Toast.makeText(MailEditActivity.this, "邮件内容必须设置",
					Toast.LENGTH_SHORT).show();
			return;
		}
		// // 启动线程，开始发送邮件
		System.out.println("启动线程开始连接服务器");
		List<Attachment> attList = adapter.getList();
		System.out.println(attList.size());

		for (Attachment a : attList) {
			// 获取附件文件名
			fileName = a.getFileName();
		}

		new MyThread().t.start();

		 progressDialog = new ProgressDialog(MailEditActivity.this);
		 progressDialog.setMessage("正在发送邮件，请稍等......");
		 progressDialog.setIndeterminate(true);
		 progressDialog
		 .setOnCancelListener(new DialogInterface.OnCancelListener() {
		 public void onCancel(DialogInterface dialog) {
		
		 }
		 });
		 progressDialog.show();

	}

	/**
	 * 点击事件
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyOnItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
				long arg3) {
			Attachment infos = (Attachment) adapter.getItem(arg2);
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MailEditActivity.this);
			builder.setTitle(infos.getFileName());
			builder.setIcon(getResources()
					.getColor(android.R.color.transparent));
			builder.setMessage("是否删除当前附件");
			builder.setNegativeButton("确定",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							adapter.clearPositionList(arg2);
							int a = adapter.getList().size();
							int count = (int) Math.ceil(a / 4.0);
							gridView.setLayoutParams(new LayoutParams(
									LinearLayout.LayoutParams.MATCH_PARENT,
									(int) (94 * 1.5 * count)));
						}
					});
			builder.setPositiveButton("取消", null);
			builder.create().show();
		}
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		if (isCaogaoxiang && mail_to.getText().toString().trim() != null) {
			AlertDialog.Builder builder = new Builder(MailEditActivity.this);
			builder.setMessage("是否存入草稿箱");
			builder.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// 保存至数据库
							saveToCaogaoxiang();
							finish();
						}

					});
			builder.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						finish();
						}

					});
			builder.show();
		} else {
		finish();
		}

	}

	/**
	 * 返回按钮
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (isCaogaoxiang && mail_to.getText().toString().trim() != null) {
				AlertDialog.Builder builder = new Builder(MailEditActivity.this);
				builder.setMessage("是否存入草稿箱");
				builder.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 保存至数据库
								saveToCaogaoxiang();
								finish();
							}

						});
				builder.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								finish();
							}

						});
				builder.show();
			}
			else
			{
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 保存至草稿箱
	 */
	private void saveToCaogaoxiang() {
		ContentValues values = new ContentValues();
		values.put("mailfrom", userName);
		values.put("mailto", mail_to.getText().toString().trim());
		values.put("cc", mail_cc.getText().toString().trim());
		values.put("subject", mail_topic.getText().toString().trim());
		values.put("content", mail_content.getText().toString().trim());
		String url = getContentResolver().insert(Config.CAOGAO_URI, values).toString();
		System.out.println(url);
		int id = Integer.parseInt(url.substring(url.length() - 1));
		System.out.println("--------" + id);
		// 保存附件
		if (adapter.getList().size() > 0) {
			List<Attachment> attachments = adapter.getmList();
			values.clear();
			for (int i = 0; i < attachments.size(); i++) {
				Attachment att = attachments.get(i);
				values.put("filename", att.getFileName());
				values.put("filepath", att.getFilePath());
				values.put("filesize", att.getFileSize());
				values.put("mailid", id);
				getContentResolver().insert(Config.ATTACHMENT, values);
			}
		}
		Toast.makeText(MailEditActivity.this, "保存至草稿箱", Toast.LENGTH_SHORT)
				.show();
	};

	/**
	 * @ClassName: MyThread
	 * @Description: 和服务器交互的线程类
	 * @author: kaleo
	 * @date 2014-8-12 下午5:11:25
	 * 
	 */
	class MyThread extends Thread {
		private HttpPost request;
		private HttpClient httpclient;
		private HttpResponse httpResponse;
		private String responseStr = null;

		Thread t = new Thread(new Runnable() {
			public void run() {
				// 访问服务器的action
				request = new HttpPost(strUrl);
				httpclient = new DefaultHttpClient();
				// 封装一个json
				JSONObject logindate = new JSONObject();
				try {

					logindate.put("title", mail_topic.getText().toString());
					logindate.put("from", mail_from.getText().toString());
					logindate.put("to", mail_to.getText().toString());
					logindate.put("cc", mail_cc.getText().toString());
					logindate.put("content", mail_content.getText().toString());
					logindate.put("empNo", empNo);
					logindate.put("attachment", imageBuffer);
					logindate.put("fileName", fileName);

					// 绑定到请求entry,设置中文支持
					StringEntity se = new StringEntity(logindate.toString(),
							HTTP.UTF_8);
					request.setEntity(se);

					httpResponse = httpclient.execute(request);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						responseStr = null;
						HttpEntity entity = httpResponse.getEntity();
						if (entity != null) {

							try {
								responseStr = URLDecoder.decode(EntityUtils
										.toString(entity, HTTP.UTF_8), "utf-8");
								JSONObject obj = new JSONObject(responseStr);
								Boolean result = obj.getBoolean("res");
								if (result) {

									Message message = Message.obtain();
									message.what = 0;
									handler.sendMessage(message);

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
