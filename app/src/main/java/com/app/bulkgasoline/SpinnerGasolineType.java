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
 * 油种类下拉菜单Code_TransportType
 * */
public class SpinnerGasolineType extends BaseSpinner {

	private ArrayAdapter<String> adapter;
	private String[] mKeys;
	private String[] mValues;

	public SpinnerGasolineType(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSpinner(context);
	}

	public void initSpinner(Context context)
	{
		ArrayList<String> keys = new ArrayList<String>();
		ArrayList<String> codes = new ArrayList<String>();
		System.out.println("------------Code_GasolineType-------------------");
		if(Utils.loadCodes(context, "Code_GasolineType", keys, codes))
		{
			System.out.println("end!!!!!!!!!");
			mKeys = keys.toArray(new String[keys.size()]);
			mValues = codes.toArray(new String[codes.size()]);
			Log.v(mKeys.toString(),mValues.toString());
			adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, mValues);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			setAdapter(adapter);

			setOnItemSelectedListener(new SpinnerSelectedListener());

			setDefaultSelect();
		}else
		{
			System.out.println("false");
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

	public void setDefaultSelect(){
		if(mValues == null)
			return;

		String default_text = getContext().getResources().getString(R.string.text_default_certi_type);
		for(int i = 0; i < mValues.length; i++)
		{
			if(default_text.equalsIgnoreCase(mValues[i]))
			{
				this.setSelection(i);
				break;
			}
		}
	}

	class SpinnerSelectedListener implements OnItemSelectedListener{

		@Override
		public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
								   long arg3) {

		}

		@Override
		public void onNothingSelected(AdapterView<?> arg0) {

		}
	}


}
