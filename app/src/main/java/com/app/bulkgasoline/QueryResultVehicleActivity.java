package com.app.bulkgasoline;

import java.util.ArrayList;

import com.app.bulkgasoline.model.VehicleModel;
import com.app.bulkgasoline.utils.DBHelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class QueryResultVehicleActivity extends BaseActivity {

	private ArrayList<VehicleModel> mVehicles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_vehicle_result);

		initTitleBarText();
		initListView();
	}

	private void initTitleBarText() {
		TextView title_text = (TextView) findViewById(R.id.id_title_bar_text);
		title_text.setText(R.string.text_vehicle_result);

		ImageView setting_back = (ImageView) findViewById(R.id.id_button_back);

		setting_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initListView() {
		mVehicles = new ArrayList<VehicleModel>();

		Intent intent = getIntent();
		String key = intent.getStringExtra("key");
		String value = intent.getStringExtra("value");

		DBHelper.queryVehicles(this, key, value, mVehicles);

		ListView listview = (ListView) findViewById(R.id.id_list_view);
		listview.setAdapter(mBaseAdapter);

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				Intent intent = new Intent();
				intent.setClass(QueryResultVehicleActivity.this,
						DetailVehicleActivity.class);

				VehicleModel vehicle = (VehicleModel) mBaseAdapter
						.getItem(position);
				intent.putExtra("VehicleModel", vehicle);

				startActivity(intent);
			}

		});
	}

	@SuppressLint("InflateParams")
	private BaseAdapter mBaseAdapter = new BaseAdapter() {

		@Override
		public int getCount() {
			return mVehicles.size();
		}

		@Override
		public Object getItem(int position) {
			return mVehicles.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup group) {
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.layout_vehicle_item, null);
			}

			VehicleModel vehicle = (VehicleModel) getItem(position);

			TextView vehicle_type = (TextView) view
					.findViewById(R.id.id_text_vehicle_type);
			vehicle_type.setText(vehicle.VehicleType);

			TextView vehicle_number = (TextView) view
					.findViewById(R.id.id_text_vehicle_number);
			vehicle_number.setText(vehicle.VehicleNumber);

			TextView vehicle_direction = (TextView) view
					.findViewById(R.id.id_text_vehicle_direction);
			vehicle_direction.setText(vehicle.VehicleDirection);

			TextView text_datetime = (TextView) view
					.findViewById(R.id.id_text_datetime);
			text_datetime.setText(vehicle.CreateTime);

			return view;
		}

	};
}
