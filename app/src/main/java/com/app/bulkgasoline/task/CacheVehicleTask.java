package com.app.bulkgasoline.task;

import com.app.bulkgasoline.model.VehicleModel;
import com.app.bulkgasoline.utils.DBHelper;

import android.content.Context;


public class CacheVehicleTask extends Thread{

	private Context mContext;
	private VehicleModel mVehicleModel;
	
	public CacheVehicleTask(Context context, VehicleModel model) {
		
		mContext = context;
		mVehicleModel = model;
	}

	@Override
	public void run() {	
		if(mContext != null && mVehicleModel != null)
		{
			DBHelper.insertVehicle(mContext, mVehicleModel);
		}
	}
}
