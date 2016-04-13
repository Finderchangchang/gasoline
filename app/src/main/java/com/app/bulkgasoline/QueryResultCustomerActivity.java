package com.app.bulkgasoline;

import java.util.ArrayList;
import com.app.bulkgasoline.model.CustomerModel;
import com.app.bulkgasoline.utils.DBHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class QueryResultCustomerActivity extends BaseActivity {

	private ArrayList<CustomerModel> mCustomers;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_customer_result);

		initTitleBarText();
		initListView();
	}

	private void initTitleBarText() {
		TextView title_text = (TextView) findViewById(R.id.id_title_bar_text);
		title_text.setText(R.string.text_customer_result);

		ImageView setting_back = (ImageView) findViewById(R.id.id_button_back);

		setting_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initListView() {
		mCustomers = new ArrayList<CustomerModel>();

		Intent intent = getIntent();
		String key = intent.getStringExtra("key");
		String value = intent.getStringExtra("value");
		System.out.println("key:"+key+"value:"+value);
		DBHelper.queryCustomers(this, key, value, mCustomers);
		
		ListView listview = (ListView) findViewById(R.id.id_list_view);
		listview.setAdapter(mBaseAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent intent = new Intent();
				intent.setClass(QueryResultCustomerActivity.this,
						DetailCustomerActivity.class);
				CustomerModel customer = (CustomerModel) mBaseAdapter
						.getItem(position);
				intent.putExtra("CustomerModel", customer);

				startActivity(intent);
			}
		});
	}

	@SuppressLint("InflateParams")
	private BaseAdapter mBaseAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return mCustomers.size();
		}

		@Override
		public Object getItem(int position) {
			return mCustomers.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup group) {
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.layout_customer_items, null);
			}

			CustomerModel customer = (CustomerModel) getItem(position);

			TextView customer_name = (TextView) view
					.findViewById(R.id.id_text_customer_name);
			customer_name.setText(customer.CustomerName);

			TextView text_liter = (TextView) view
					.findViewById(R.id.id_text_liter);
			text_liter.setText(customer.Count);

			TextView customer_purpose = (TextView) view
					.findViewById(R.id.id_text_customer_purpose);
			customer_purpose.setText(customer.Purpose);

			TextView text_datetime = (TextView) view
					.findViewById(R.id.id_text_datetime);
			text_datetime.setText(customer.CreateTime);

			return view;
		}

	};
}
