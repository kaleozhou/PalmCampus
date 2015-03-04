package com.org.palmcampus.oa.pojo.adapter;

import java.util.List;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.util.StringUtil;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
//员工信息列表
public class NewsPagination2 extends BaseAdapter{
	
	private List<String> List_Item;
	private LayoutInflater mInflater;

	public NewsPagination2(Context context,List<String> list_item){
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
		return StringUtil.stringToLong(List_Item.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.flow_list_item2, null);
			holder = new ViewHolder();
			holder.title_TextView = (TextView)convertView.findViewById(R.id.flow_list_item_title_TV);
			holder.iconImageView = (ImageView)convertView.findViewById(R.id.flow_list_item_icon_IV);
			holder.content_EditText=(EditText)convertView.findViewById(R.id.flow_list_item_content_TV);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		
        holder.title_TextView.setText("内容：");
        holder.iconImageView.setBackgroundResource(R.drawable.list_item_icon);
		holder.content_EditText.setText(List_Item.get(position));
		return convertView;
	}

	/** 
     * 添加数据列表项 
     * @param newsitem 
     */ 
    public void addNewsItem(String item){
    	List_Item.add(item); 
    }
    
    static class ViewHolder{
    	long id;
    	ImageView iconImageView;
        TextView title_TextView;
        EditText content_EditText;
    }
    
}