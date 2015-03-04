package com.org.palmcampus.oa.pojo.adapter;

import java.util.List;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.pojo.ErpgongGao;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
//员工信息列表
public class NewsPagination extends BaseAdapter{
	
	private List<ErpgongGao> List_Item;
	private LayoutInflater mInflater;

	public NewsPagination(Context context,List<ErpgongGao> list_item){
		this.List_Item = list_item;
		
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(List_Item.size()==0){
			System.out.println("没有消息");
			View convertView = mInflater.inflate(R.layout.flow_list_item, null);
			TextView tv = (TextView)convertView.findViewById(R.id.flow_list_item_title_TV);
			tv.setText("暂时没有新闻公告");
		}
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
		return List_Item.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		System.out.println("))))))))))))))))))))))");
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.news_list_item, null);
			holder = new ViewHolder();
			
			holder.title_TextView = (TextView)convertView.findViewById(R.id.flow_list_item_title_TV);
			holder.iconImageView = (ImageView)convertView.findViewById(R.id.flow_list_item_icon_IV);
			holder.top_TextView=(TextView)convertView.findViewById(R.id.news_top);
			holder.publisher_TextView=(TextView)convertView.findViewById(R.id.news_publisher);
			holder.publisherDate_TextView=(TextView)convertView.findViewById(R.id.news_publisher_date);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag(); 
		}
		 if(List_Item.get(position).getTitleStr()!=null)
		 {
			 holder.title_TextView.setText(List_Item.get(position).getTitleStr().toString());
		
		 }
		 else
		 {
			   holder.title_TextView.setText("新闻测试");
		 }
        if(List_Item.get(position).getUserName()!=null)
        {
        holder.publisher_TextView.setText(List_Item.get(position).getUserName().toString());

        }else
        {
        	
        	holder.publisher_TextView.setText("管理员");
        }
        if( List_Item.get(position).getId()!=null)
        {
        	
        	  holder.top_TextView.setText( " "+(position+1) +".");
        }
      
     
		return convertView;
	}

	/** 
     * 添加数据列表项 
     * @param newsitem 
     */ 
    public void addNewsItem(ErpgongGao item){
    	List_Item.add(item); 
    }
    
    static class ViewHolder{
    	long id;
    	ImageView iconImageView;
    	TextView top_TextView;
        TextView title_TextView;
        TextView publisher_TextView;
        TextView publisherDate_TextView;
    }
    
}