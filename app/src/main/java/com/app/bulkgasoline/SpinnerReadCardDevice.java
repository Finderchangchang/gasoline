package com.app.bulkgasoline;

import java.util.Set;

import com.app.bulkgasoline.utils.Utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class SpinnerReadCardDevice extends BaseSpinner {

	private ArrayAdapter<String> adapter;
	private String[] mKeys;
	private String[] mValues;

	public SpinnerReadCardDevice(Context context, AttributeSet attrs) {
		super(context, attrs);
		initSpinner(context);
	}
	
	public void initSpinner(Context context)
	{
		BluetoothAdapter mBtAdapter = BluetoothAdapter.getDefaultAdapter();
    	if(mBtAdapter == null)
    	{
    		return;
    	}
    	
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();
        if(pairedDevices.size() == 0)
        {
        	return;
        }

        mKeys = new String[pairedDevices.size()];
        mValues = new String[pairedDevices.size()];
        String address = Utils.ReadBlueToothAddress(context);
        int index = -1;
        
        int count = 0;
        for(BluetoothDevice device : pairedDevices)
        {
        	mKeys[count] = device.getAddress();
        	mValues[count] = device.getName();
        	if(mKeys[count].equalsIgnoreCase(address))
        		index = count;
        	
        	count++;
        }
        
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item, mValues);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
         
        setAdapter(adapter);
        setOnItemSelectedListener(new SpinnerSelectedListener());
        
        if(index != -1)
        	setSelection(index);
        else if(mValues.length > 0)
        {
        	Utils.WriteBlueToothAddress(context, mKeys[0]);
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
