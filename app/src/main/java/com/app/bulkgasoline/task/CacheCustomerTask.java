package com.app.bulkgasoline.task;

import com.app.bulkgasoline.model.CustomerModel;
import com.app.bulkgasoline.utils.DBHelper;

import android.content.Context;


public class CacheCustomerTask extends Thread{

	private Context mContext;
	private CustomerModel mCustomerModel;
	
	public CacheCustomerTask(Context context, CustomerModel model) {
		
		mContext = context;
		mCustomerModel = model;
	}

	@Override
	public void run() {	
		if(mContext != null && mCustomerModel != null)
		{
			DBHelper.insertCustomer(mContext, mCustomerModel);
		}
	}
}
