package com.app.bulkgasoline;

import java.util.ArrayList;

import com.app.bulkgasoline.utils.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class SpinnerVehicleColor extends BaseSpinner {

	private ArrayAdapter<String> adapter;
	private static String[] mKeys={"红","橙","黄","绿","青","蓝","紫"};
	private static String[] mValues={"红","橙","黄","绿","青","蓝","紫"};

	public SpinnerVehicleColor(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSpinner(context);
	}

	public void initSpinner(Context context)
	{
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> codes = new ArrayList<String>();
		if(Utils.loadCodes(context, "Code_VehicleColor", keys, codes))
		{
			mKeys = keys.toArray(new String[keys.size()]);
			mValues = codes.toArray(new String[codes.size()]);

			adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, mValues);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			setAdapter(adapter);

			setOnItemSelectedListener(new SpinnerSelectedListener());
		}
	}

	public void setDefaultSelect(){
		if(mValues != null && mValues.length > 0)
			this.setSelection(0);
	}

	public String getSelectedKey()
	{
		int position = getSelectedItemPosition();
		if(position != -1)
			return mKeys[position];

		return "";
	}

	public String getSelectedText()
	{
		int position = getSelectedItemPosition();
		if(position != -1)
			return mValues[position];

		return "";
	}

	class SpinnerSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
								   long arg3) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {
			// TODO Auto-generated method stub

		}
	}


}
