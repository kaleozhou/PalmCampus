package com.org.palmcampus.email;

import java.util.ArrayList;
import java.util.List;

import com.org.palmcampus.R;
import com.org.palmcampus.oa.custom.Config;
import com.org.palmcampus.oa.pojo.Employee;
import com.org.palmcampus.oa.util.SysExitUtil;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MailAddContact extends Activity {
	private static final String TAG = "====掌上校园搜索联系人====";
	private Handler handler;
	private static final int MSG_KEY = 0x1234;
	private ListView lv;
	private MyAdapter adapter;
	private List<Employee> localEmpList = new ArrayList<Employee>();
	private List<Employee> list;
	// 搜索框
	private EditText input;
	private List<String> chooseUsers = new ArrayList<String>();

	// 更新搜索人信息
	private void refreshListView(String value) {
		if (value == null || value.trim().length() == 0) {
			list = getAllConstacts();
			adapter = new MyAdapter();
			lv.setAdapter(adapter);
			return;
		}
		ArrayList<Employee> tmpList = new ArrayList<Employee>();
		for (Employee s : list) {
			if (s.getMobile().indexOf(value) >= 0||s.getEmail().indexOf(value)>= 0
					||s.getEmployeeStatus().indexOf(value)>=0||s.getEmpName().indexOf(value)>=0) {
				tmpList.add(s);
			}
		}
		if (tmpList.size() == 0)
			return;
		list = tmpList;
		adapter = new MyAdapter();
		lv.setAdapter(adapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		SysExitUtil.activityList.add(this);
		setContentView(R.layout.email_addconstact);
		// 获取所有联系人
		list = getAllConstacts();

		lv = (ListView) findViewById(R.id.show_constant);
		adapter = new MyAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Employee user = (Employee) parent.getItemAtPosition(position);
				CheckBox ck_box = (CheckBox) view.findViewById(R.id.ck_box);
				if (chooseUsers.contains(user.getEmpName())) {
					chooseUsers.remove(user.getEmpName());
					ck_box.setChecked(false);
				} else {
					chooseUsers.add(user.getEmpName());
					ck_box.setChecked(true);
				}
				// Toast.makeText(MailAddConstact.this, position+"",
				// Toast.LENGTH_SHORT).show();
			}

		});
		// 搜索联系人
		input = (EditText) findViewById(R.id.txt_input);
		input.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable editer) {
				Log.d(TAG, "afterTextChanged");
			}

			public void beforeTextChanged(CharSequence value, int arg0,
					int arg1, int arg2) {
				Log.d(TAG, "beforeTextChanged");
			}

			public void onTextChanged(CharSequence value, int arg0, int arg1,
					int arg2) {
				Log.d(TAG, "onTextChanged");
				Log.w(TAG, "input.text=" + value.toString());
				Message msg = new Message();
				msg.what = MSG_KEY;
				Bundle data = new Bundle();
				data.putString("value", value.toString());
				msg.setData(data);
				handler.sendMessage(msg);
			}
		});
		handler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case MSG_KEY:
					System.out.println("正在搜索。。。。。。。");
					refreshListView(msg.getData().get("value").toString());
				}
			}
		};
	}

	/**
	 * 获取所有联系人
	 * 
	 * @return
	 */
	private List<Employee> getAllConstacts() {
		Cursor c = getContentResolver().query(Config.CONTACT_URI, null, null, null, null);
		localEmpList.clear();
			// 如果联系人表里有联系人
			while (c.moveToNext()) {
				Employee e = new Employee();
				int empNo = c.getInt(c.getColumnIndex("empNo"));
				String empName = c.getString(c.getColumnIndex("empName"));
				String mobile=c.getString(c.getColumnIndex("mobile"));
				String jiaose=c.getString(c.getColumnIndex("jiaose"));
				String zhiwei=c.getString(c.getColumnIndex("zhiwei"));
				e.setEmpNo(empNo);
				e.setEmpName(empName);
				e.setMobile(mobile);
				e.setEmail(jiaose);
				e.setEmployeeStatus(zhiwei);
				localEmpList.add(e);
			}
		c.close();
		
		return localEmpList;
	}

	/**
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View item = View.inflate(MailAddContact.this,
					R.layout.email_addconstact_item, null);
			TextView name = (TextView) item.findViewById(R.id.add_name);
			TextView address = (TextView) item.findViewById(R.id.add_address);
			CheckBox ck_box = (CheckBox) item.findViewById(R.id.ck_box);

			Employee user = list.get(position);
			name.setText(user.getEmpName());
			//部门
			 address.setText("部门:"+user.getMobile()+" 角色:"+user.getEmail()+" 职位:"+user.getEmployeeStatus());

			if (chooseUsers.contains(user.getAddress())) {
				ck_box.setChecked(true);
			} else {
				ck_box.setChecked(false);
			}
			return item;
		}

	}

	/**
	 * 返回
	 * 
	 * @param v
	 */
	public void back(View v) {
		Intent data = new Intent();
		data.putStringArrayListExtra("chooseUsers",
				(ArrayList<String>) chooseUsers);
		setResult(2, data);
		finish();
	}

	/**
	 * 确定
	 * 
	 * @param v
	 */
	public void choose(View v) {
		Intent data = new Intent();
		data.putStringArrayListExtra("chooseUsers",
				(ArrayList<String>) chooseUsers);
		setResult(2, data);
		finish();
	}
}
