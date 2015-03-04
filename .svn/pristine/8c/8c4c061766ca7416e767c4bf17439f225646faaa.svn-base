package com.org.palmcampus.email;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore.Images.Media;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


import com.org.palmcampus.R;
import com.org.palmcampus.email.bean.Email;
import com.org.palmcampus.oa.util.SysExitUtil;

/* 邮件详细内容类
 * @author rolant
 */
public class MailContentActivity extends Activity {


	private TextView tv_addr, tv_mailsubject, tv_mailcontent;
	private ListView lv_mailattachment;
	private WebView wv_mailcontent;
	private Button btn_cancel, btn_relay;
	private ArrayList<InputStream> attachmentsInputStreams;
	private Email email;
	//发件人
	private String from;
	private Handler handler;
	//附件列表
	List<String> fileNameList = new ArrayList<String>();
	//附件保存位置
	String path=Environment.getExternalStorageDirectory()+"/"+"尚驰邮件附件/";
	//private ProgressDialog progressDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(this);
		setContentView(R.layout.email_mailcontent);

		email = (Email) getIntent().getSerializableExtra("EMAIL");
		from=email.getFrom();
		// attachmentsInputStreams = ((MyApplication)
		// getApplication()).getAttachmentsInputStreams();
		init();
	}

	private void init() {
		handler = new MyHandler(this);
		tv_addr = (TextView) findViewById(R.id.tv_addr);
		tv_mailsubject = (TextView) findViewById(R.id.tv_mailsubject);
		tv_mailcontent = (TextView) findViewById(R.id.tv_mailcontent);
		String attachments = email.getAttachments();
		//System.out.println(attachments);
		if(attachments!=null){
		String[] fileList = attachments.split(",");
		System.out.println(fileList[0]);
		for (int i = 0; i < fileList.length; i++) {
			System.out.println("开始遍历");
			String[] str = fileList[i].split("\\|");
			System.out.println(str[0]);
			fileNameList.add(str[0]);
		}
		}
		System.out.println("附件个数" + fileNameList.size());
		System.out.println(fileNameList);
		for (String s : fileNameList) {
			System.out.println(s);
		}
		//如果没有附件
		if(fileNameList.size() ==0){
			lv_mailattachment = (ListView) findViewById(R.id.lv_mailattachment);
			lv_mailattachment.setVisibility(View.VISIBLE);
			lv_mailattachment.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, new String[]{"没有附件"}));
		}
		//如果有附件
		if (fileNameList.size() > 0) {
			lv_mailattachment = (ListView) findViewById(R.id.lv_mailattachment);
			lv_mailattachment.setVisibility(View.VISIBLE);
			lv_mailattachment.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, fileNameList));
			lv_mailattachment.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position, long id) {
					new Thread(new Runnable() {
						// 下载附件的字节数组
						byte[] data;

						@Override
						public void run() {
							handler.obtainMessage(
									0,
									"开始下载\"" + fileNameList.get(position)
											+ "\"").sendToTarget();
							
							//Looper.prepare();
							try {
//								progressDialog = new ProgressDialog(
//										MailContentActivity.this);
//								progressDialog.setMessage("正在下载附件，请稍等......");
//								progressDialog.setIndeterminate(true);
//								progressDialog.show();
								System.out.println("发件人"+from);
								//调用webservice下载附件

								
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							// InputStream is =
							// attachmentsInputStreams.get(position);
							// String path = new IOUtil().stream2file(is,
							// Environment.getExternalStorageDirectory().toString()
							// + "/temp/" + fileNameList.get(position));
							if (data == null) {
								Toast.makeText(MailContentActivity.this, "下载失败，请检查网络", 6000).show();
//								handler.obtainMessage(0, "下载失败！")
//										.sendToTarget();
							} else {
//								BitmapFactory.Options options = new BitmapFactory.Options();
//								// 该值一定要设为false，设为true返回为null
//								options.inJustDecodeBounds = false;
//								options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//								// 将字节数组转换为Bitmap
//								Bitmap img = ImageUtil.getPicFromBytes(data,
//										options);
//								//将图片保存到图库里
//								saveToGallery(img, fileNameList.get(position));
								saveToSDCard(fileNameList.get(position), data);
								handler.obtainMessage(0, "下载附件成功,文件保存在"+path)
										.sendToTarget();
							}
							
						}
					}).start();
				}
			});
		}

		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_relay = (Button) findViewById(R.id.btn_relay);

		tv_addr.setText("发件人:"+email.getFrom());
		tv_mailsubject.setText("主题:"+email.getTitle());
		if (email.isHtml()) {
			wv_mailcontent = (WebView) findViewById(R.id.wv_mailcontent);
			wv_mailcontent.setVisibility(View.VISIBLE);
			wv_mailcontent.loadDataWithBaseURL(null, email.getContent(),
					"text/html", "utf-8", null);
			// wv_mailcontent.getSettings().setLoadWithOverviewMode(true);
			// wv_mailcontent.getSettings().setUseWideViewPort(true);
			// 设置缩放
			wv_mailcontent.getSettings().setBuiltInZoomControls(true);

			// 网页适配
			DisplayMetrics dm = getResources().getDisplayMetrics();
			int scale = dm.densityDpi;
			if (scale == 240) {
				wv_mailcontent.getSettings().setDefaultZoom(ZoomDensity.FAR);
			} else if (scale == 160) {
				wv_mailcontent.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
			} else {
				wv_mailcontent.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
			}
			wv_mailcontent.setWebChromeClient(new WebChromeClient());
			tv_mailcontent.setVisibility(View.GONE);
		} else {
			tv_mailcontent.setText("内容:"+email.getContent());
		}
		//返回按钮
		btn_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("返回");
				finish();
				//返回收件箱界面，更新已读图标
				Intent intent=new Intent(MailContentActivity.this, MailBoxActivity.class);
				intent.putExtra("status", 0);
				startActivity(intent);
			}
		});
		//邮件回复
		btn_relay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(MailContentActivity.this,
						MailEditActivity.class).putExtra("EMAIL", email)
						.putExtra("TYPE", 1));
			}
		});
		/*
		 * btn_relay.setOnLongClickListener(new OnLongClickListener() {
		 * 
		 * @Override public boolean onLongClick(View v) { startActivity(new
		 * Intent(MailContentActivity.this,
		 * MailEditActivity.class).putExtra("EMAIL", email).putExtra("type",
		 * 2)); return true; } });
		 */
	}

	class MyHandler extends Handler {

		private WeakReference<MailContentActivity> wrActivity;

		public MyHandler(MailContentActivity activity) {
			this.wrActivity = new WeakReference<MailContentActivity>(activity);
		}

		@Override
		public void handleMessage(android.os.Message msg) {
			final MailContentActivity activity = wrActivity.get();
			switch (msg.what) {
			case 0:
				//progressDialog.dismiss();
				Toast.makeText(activity.getApplicationContext(),
						msg.obj.toString(), Toast.LENGTH_LONG).show();
				break;
			default:
				break;
			}
		};
	};
	//将下载的附件保存到图库里
	public void saveToGallery(Bitmap image, String fileName) {
		// 创建ContentValues对象，准备插入数据
		ContentValues values = new ContentValues();
		values.put(Media.DISPLAY_NAME, fileName);
		// values.put(Media.DESCRIPTION , "金塔");
		values.put(Media.MIME_TYPE, "image/jpeg");
		// 插入数据，返回所插入数据对应的Uri
		Uri uri = getContentResolver().insert(Media.EXTERNAL_CONTENT_URI,
				values);
		// 加载应用程序下的jinta图片
		OutputStream os = null;
		try {
			// 获取刚插入的数据的Uri对应的输出流
			os = getContentResolver().openOutputStream(uri);
			// 将bitmap图片保存到Uri对应的数据节点中
			image.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//将下载的附件保存到SD卡指定位置
	public void saveToSDCard(String fileName,byte[] data){
		//String path=Environment.getExternalStorageDirectory()+"/"+"尚驰邮件附件/";
		File file=new File(path);
		if(!file.exists())
			file.mkdir();
		File f=new File(path+fileName);
		try {
			FileOutputStream fos=new FileOutputStream(f);
			fos.write(data);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
