package com.org.palmcampus.email;

import java.util.ArrayList;
import java.util.List;


import com.org.palmcampus.R;
import com.org.palmcampus.oa.custom.Config;
import com.org.palmcampus.oa.pojo.Employee;
import com.org.palmcampus.oa.util.SysExitUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MailContactsActivity extends Activity {
	private ListView lv;
	//private List<Employee> list;
	private ProgressDialog dialog;
	private List<Employee> localEmpList = new ArrayList<Employee>();
	private List<Employee> remoteEmpList = new ArrayList<Employee>();
	private Myadapter adapter;
	int empNo = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_constact);
		SysExitUtil.activityList.add(this);
		getAllConstacts();
	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		finish();
	}

	/**
	 * 查询所有的联系人
	 * 
	 * @return
	 */
	private void getAllConstacts() {
		Cursor c = getContentResolver().query(Config.CONTACT_URI, null, null, null,
				"empNo desc");

		// 如果联系人表里有联系人
		while (c.moveToNext()) {
			System.out.println("员工有记录");
			Employee e = new Employee();
			empNo = c.getInt(c.getColumnIndex("empNo"));
			String empName = c.getString(c.getColumnIndex("empName"));
			String mobile = c.getString(c.getColumnIndex("mobile"));
			String jiaose=c.getString(c.getColumnIndex("jiaose"));
			String zhiwei=c.getString(c.getColumnIndex("zhiwei"));
			e.setEmpNo(empNo);
			e.setEmpName(empName);
			e.setMobile(mobile);
			//角色
			e.setEmail(jiaose);
			//职位
			e.setEmployeeStatus(zhiwei);
			localEmpList.add(e);
		}
		c.close();
		init();
	}

	/**
	 * 删除数据
	 * 
	 * @param name
	 * @param address
	 */
	private void deleteAddress(final String empName) {
		System.out.println("要删除的员工姓名"+empName);
		AlertDialog.Builder builder = new Builder(MailContactsActivity.this);
		builder.setMessage("你确定要删除数据");
		builder.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				getContentResolver().delete(Config.CONTACT_URI, "empName=?",
						new String[] { empName });

				//getAllConstacts();
				//Myadapter ad=(Myadapter)lv.getAdapter();
				adapter.notifyDataSetChanged();
				Toast.makeText(MailContactsActivity.this, "删除数据成功",
						Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNegativeButton("取消", null);
		builder.show();
	}

	/**
	 * 初始化
	 */
	private void init() {
		lv = (ListView) findViewById(R.id.lv_constant);
		adapter = new Myadapter();
		lv.setAdapter(adapter);

	}

	/**
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class Myadapter extends BaseAdapter {
		@Override
		public int getCount() {
			return localEmpList.size();
		}

		@Override
		public Object getItem(int position) {
			return localEmpList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = View.inflate(MailContactsActivity.this,
					R.layout.email_constact_item, null);
			TextView name = (TextView) item.findViewById(R.id.tv_name);
			TextView address = (TextView) item.findViewById(R.id.tv_address);
			Employee user = localEmpList.get(position);
			name.setText(user.getEmpName());
			 address.setText("部门:"+user.getMobile()+" 角色:"+user.getEmail()+" 职位:"+user.getEmployeeStatus());
			return item;
		}

	}

}
