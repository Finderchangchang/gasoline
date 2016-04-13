package com.app.bulkgasoline;

import java.util.ArrayList;


import com.app.bulkgasoline.utils.Utils;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * 加油员下拉菜单加载
 * */
public class SpinnerOperator extends BaseSpinner {

	private ArrayAdapter<String> adapter;
	private String[] mKeys;
	private String[] mValues;

	public SpinnerOperator(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSpinner(context);
	}

	public void initSpinner(Context context)
	{
		ArrayList<String> operators = new ArrayList<String>();
		ArrayList<String> operatorIds = new ArrayList<String>();

		if(Utils.loadOperators(context, operatorIds, operators))//判断是否已存储加油员信息
		{
			mKeys = operatorIds.toArray(new String[operatorIds.size()]);
			mValues = operators.toArray(new String[operators.size()]);
			adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, mValues);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			setAdapter(adapter);
			setOnItemSelectedListener(new SpinnerSelectedListener());
		}
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
