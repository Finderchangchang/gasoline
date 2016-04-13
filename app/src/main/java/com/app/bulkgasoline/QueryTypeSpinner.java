package com.app.bulkgasoline;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;

public class QueryTypeSpinner extends BaseSpinner {

	private ArrayAdapter<String> adapter;
	private String[] mKeys;
	private String[] mValues;
	private boolean mCustomer;

	public QueryTypeSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);

		mCustomer = false;
		initSpinner(context, true);
	}

	private String getString(int id) {
		return getContext().getResources().getString(id).replace("：", "");
	}

	public void initSpinner(Context context, boolean customer) {
		if (mCustomer == customer)
			return;
		mCustomer = customer;
		if (customer) {
			mKeys = new String[] { "CustomerName", "CustomerIdentityType",
					"CustomerIdentityNumber", "CustomerLinkway", "Count",
					"Purpose", "CustomerCompany", "EmployeeId", "Suspicious",
					"GasolineType", };

			mValues = new String[] { getString(R.string.text_customer_name),
					getString(R.string.text_certi_type),
					getString(R.string.text_certi_number),
					getString(R.string.text_tel_number),
					getString(R.string.text_liter),
					getString(R.string.text_buy_intent),
					getString(R.string.text_company_name),
					getString(R.string.text_operator),
					getString(R.string.text_doubt),
					getString(R.string.text_Gasoline_type),
					getString(R.string.text_Transport_type), };
		} else {
			mKeys = new String[] { "VehicleType", "VehicleColor",
					"VehicleNumber", "VehiclePersonCount", "VehicleDirection",
					"EmployeeId", };

			mValues = new String[] { getString(R.string.text_vehicle_type),
					getString(R.string.text_vehicle_color),
					getString(R.string.text_vehicle_number),
					getString(R.string.text_vehicle_peoples),
					getString(R.string.text_vehicle_direction),
					getString(R.string.text_operator), };
		}

		adapter = new ArrayAdapter<String>(getContext(),
				android.R.layout.simple_spinner_item, mValues);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		setAdapter(adapter);
	}

	public String getSelectedKey() {
		int position = getSelectedItemPosition();
		if (position != -1)
			return mKeys[position];

		return "";
	}

	public String getSelectedType() {
		int position = getSelectedItemPosition();
		if (position != -1)
			return mValues[position];

		return "";
	}
}
