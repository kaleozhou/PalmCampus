package com.org.palmcampus.oa.pojo.adapter;



import com.org.palmcampus.R;
import com.org.palmcampus.oa.pojo.ErpgongGao;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

//新闻详细信息
public class NewsDetailsPagination {
	private LayoutInflater mInflater;
	private Context context;
	ErpgongGao List_Item;
	private LinearLayout root_LinearLayout;

	public NewsDetailsPagination(Context context, ErpgongGao list_item,
			LinearLayout root_LinearLayout) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
		this.List_Item = list_item;
		this.root_LinearLayout = root_LinearLayout;
	}

	public LinearLayout getViewData() {

		View convertView = new View(context);
		ViewHolder holder = new ViewHolder();
		convertView = mInflater.inflate(R.layout.show_news2, null);
		// 新闻标题
		holder.title_editText = (EditText) convertView.findViewById(R.id.title);
		String title = List_Item.getTitleStr();
		holder.title_editText.setText(title);
		// 新闻内容

		holder.content_webView = (WebView) convertView.findViewById(R.id.content);
		holder.content_webView.setVisibility(View.VISIBLE);
		holder.content_webView.loadDataWithBaseURL(null, List_Item.getContentStr(),
				"text/html", "utf-8", null);
		// wv_mailcontent.getSettings().setLoadWithOverviewMode(true);
		// wv_mailcontent.getSettings().setUseWideViewPort(true);
		// 设置缩放
		holder.content_webView.getSettings().setBuiltInZoomControls(true);

		// 网页适配
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int scale = dm.densityDpi;
		if (scale == 240) {
			holder.content_webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		} else if (scale == 160) {
			holder.content_webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
		} else {
			holder.content_webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
		}
		holder.content_webView.setWebChromeClient(new WebChromeClient());
		// 发布人
		holder.publisher_editText = (EditText) convertView
				.findViewById(R.id.publisher);
		String publisher = List_Item.getUserName();
		holder.publisher_editText.setText(publisher);
		// 接收人
		holder.sendee_editText = (EditText) convertView
				.findViewById(R.id.sendee);
		String sendee = List_Item.getYiJieShouRen();
		holder.sendee_editText.setText(sendee);
//		//留言
//		holder.oldadvice_editText=(WebView)convertView.findViewById(R.id.old_advice);
//		String advice=List_Item.getChuanYueYiJian();
//		holder.oldadvice_editText.setText(advice);
		
		//留言内容

				holder.oldadvice_editText = (WebView) convertView.findViewById(R.id.old_advice);
				holder.oldadvice_editText.setVisibility(View.VISIBLE);
				holder.oldadvice_editText.loadDataWithBaseURL(null, List_Item.getChuanYueYiJian(),
						"text/html", "utf-8", null);
				// wv_mailcontent.getSettings().setLoadWithOverviewMode(true);
				// wv_mailcontent.getSettings().setUseWideViewPort(true);
				// 设置缩放
				holder.content_webView.getSettings().setBuiltInZoomControls(true);

				// 网页适配
				DisplayMetrics dm1 = context.getResources().getDisplayMetrics();
				int scale1 = dm.densityDpi;
				if (scale == 240) {
					holder.content_webView.getSettings().setDefaultZoom(ZoomDensity.FAR);
				} else if (scale == 160) {
					holder.content_webView.getSettings().setDefaultZoom(ZoomDensity.MEDIUM);
				} else {
					holder.content_webView.getSettings().setDefaultZoom(ZoomDensity.CLOSE);
				}
				holder.content_webView.setWebChromeClient(new WebChromeClient());
		// 已阅人
		holder.spinner_reader = (Spinner) convertView
				.findViewById(R.id.spinner_reader);
		String reader = List_Item.getYiJieShouRen();
		String[] readers=null;
		if(reader!=null&&!reader.equals(""))
		{
			readers = reader.split(",");
		}
		else
		{
			readers=new String[1];
			readers[0]="还没有人阅读";
		}
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				context, android.R.layout.simple_spinner_item, readers);
		//adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		holder.spinner_reader.setAdapter(adapter);
		root_LinearLayout.addView(convertView);

		return root_LinearLayout;
	}

	static class ViewHolder {
		TextView title_TextView;
		TextView Constant_TextView;
		EditText content_editText;
		WebView content_webView;
		EditText title_editText;
		EditText publisher_editText;
		EditText sendee_editText;
		WebView oldadvice_editText;
		Spinner spinner_reader;
	}

}