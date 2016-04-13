package com.app.bulkgasoline;

import com.app.bulkgasoline.model.CustomerModel;
import com.app.bulkgasoline.utils.DBHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 销售详细信息
 * */
public class DetailCustomerActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_customer_detail);

		initTitleBarText();
		initCustomerDetail();
	}

	private void initTitleBarText() {
		TextView title_text = (TextView) findViewById(R.id.id_title_bar_text);
		title_text.setText(R.string.text_customer_detail);

		ImageView setting_back = (ImageView) findViewById(R.id.id_button_back);

		setting_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void initCustomerDetail() {
		Intent intent = getIntent();
		CustomerModel customer = (CustomerModel) intent
				.getSerializableExtra("CustomerModel");
		if (customer == null)
			return;

		PhotosLinearLayout photos = (PhotosLinearLayout) findViewById(R.id.id_customer_photos);
		photos.setReadOnly(true);
		photos.setHeader(DBHelper.loadBitmap(customer.CustomerImage));
		photos.setPhotos(DBHelper.loadBitmaps(customer.CustomerImages));

		TextView customer_name = (TextView) findViewById(R.id.id_text_customer_name);
		customer_name.setText(customer.CustomerName);

		TextView certi_type = (TextView) findViewById(R.id.id_text_certi_type);
		certi_type.setText(customer.CustomerIdentityType);

		TextView certi_number = (TextView) findViewById(R.id.id_text_certi_number);
		certi_number.setText(customer.CustomerIdentityNumber);

		TextView tel_number = (TextView) findViewById(R.id.id_text_tel_number);
		tel_number.setText(customer.CustomerLinkway);

		TextView count = (TextView) findViewById(R.id.id_text_liter);
		count.setText(customer.Count);

		TextView purpose = (TextView) findViewById(R.id.id_text_customer_purpose);
		purpose.setText(customer.Purpose);

		TextView company_name = (TextView) findViewById(R.id.id_text_company_name);
		company_name.setText(customer.CustomerCompany);

		TextView address = (TextView) findViewById(R.id.id_text_address);
		address.setText(customer.customerAddress);

		TextView operator = (TextView) findViewById(R.id.id_text_operator);
		operator.setText(customer.EmployeeId);

		TextView is_doubt = (TextView) findViewById(R.id.id_text_doubt);
		is_doubt.setText(customer.Suspicious);

		if (customer.Suspicious.equalsIgnoreCase("是")) {
			View doubt_desc_layout = findViewById(R.id.id_doubt_desc_layout);
			doubt_desc_layout.setVisibility(View.VISIBLE);

			TextView doubt_desc = (TextView) findViewById(R.id.id_text_doubt_desc);
			doubt_desc.setText(customer.SuspiciousReson);
		}

		TextView datetime = (TextView) findViewById(R.id.id_text_datetime);
		datetime.setText(customer.CreateTime);
	}
}
