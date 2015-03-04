package com.org.palmcampus.email.adapter;

import java.util.List;

import com.org.palmcampus.R;
import com.org.palmcampus.email.bean.Email;



import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//邮件列表
public class EmailInBoxPagination extends BaseAdapter{
	
	private List<Email> List_Item;
	private LayoutInflater mInflater;

	public EmailInBoxPagination(Context context,List<Email> list_item){
		this.List_Item = list_item;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() {
		return List_Item.size();
	}

	@Override
	public Object getItem(int position) {
		return List_Item.get(position);
	}

	@Override
	public long getItemId(int position) {
		return List_Item.get(position).getEmailID();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.email_mailbox_item, null);
			holder = new ViewHolder();
			holder.tv_from = (TextView)convertView.findViewById(R.id.tv_from);
			holder.tv_sentdate = (TextView)convertView.findViewById(R.id.tv_sentdate);
			holder.tv_subject = (TextView) convertView.findViewById(R.id.tv_subject);
			holder.tv_new=(TextView) convertView.findViewById(R.id.tv_new);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		//发件人
        holder.tv_from.setText(List_Item.get(position).getFrom().toString());
        //发件日期
        if(List_Item.get(position).getSentdate()!=null)
        holder.tv_sentdate.setText(List_Item.get(position).getSentdate().toString());
        //发件主题
        holder.tv_subject.setText(List_Item.get(position).getTitle().toString());
        //如果是未读邮件，现实new图标
        if(List_Item.get(position).getIsnew()==1){
        	//System.out.println("这是新邮件");
        	//holder.tv_new.setVisibility(View.VISIBLE);
        	holder.tv_new.setBackgroundResource(R.drawable.new_email);
        }
        //否则图设为空
        else{
        	holder.tv_new.setBackgroundDrawable(null);
        }
		return convertView;
	}

	/** 
     * 添加数据列表项 
     * @param newsitem 
     */ 
    public void addNewsItem(Email item){
    	List_Item.add(item); 
    }
    
    static class ViewHolder{
    	public static final String title_TextView = null;
		long id;
		TextView tv_new;
    	TextView tv_from;
    	TextView tv_sentdate;
        TextView tv_subject;
    }
    
}