package com.app.bulkgasoline.task;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import Decoder.BASE64Encoder;
import android.os.Message;

import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.model.VehicleModel;

public class AddAlarmVehicleTask extends HttpTask {

	protected static String VEHICLE_TYPE = "VehicleType";
	protected static String VEHICLE_COLOR = "VehicleColor";
	protected static String VEHICLE_NUMBER = "VehicleNumber";
	protected static String VEHICLE_PERSON_COUNT = "VehiclePersonCount";
	protected static String VEHICLE_DIRECTION = "VehicleDirection";
	protected static String EMPLOYEEID = "EmployeeId";

	private VehicleModel mVehicleModel;

	public AddAlarmVehicleTask(GlobalModel global, AuthorizeModel authorize,
			VehicleModel model) {
		super(global, authorize);

		mVehicleModel = model;
	}

	private AddAlarmVehicleListener mListener;

	public interface AddAlarmVehicleListener {
		public void OnResult(boolean result, VehicleModel vehicle,
				String message);
	}

	public void setListener(AddAlarmVehicleListener listener) {
		mListener = listener;
	}

	public void start(AddAlarmVehicleListener listener) {
		mListener = listener;
		start();
	}

	@Override
	protected boolean isPost() {
		return true;
	}

	@Override
	protected boolean isModelInfomation() {
		return true;
	}

	@Override
	public String getActionName() {
		return "AddAlarmVehicle";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}

	@Override
	public void prepareForms() {
		if (mVehicleModel != null) {
			try {
				super.addArgument("Model",
						new BASE64Encoder().encode(mVehicleModel
								.Serialization().getBytes("UTF-8")));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onInvokeReturn(boolean result, Map<String, String> data,
			Message msg) {
		if (mListener != null) {
			if (result) {
				String strSuccess = data.get("Success");
				String strMessage = data.get("Message");
				
				mListener.OnResult("true".compareToIgnoreCase(strSuccess) == 0,
						mVehicleModel, strMessage);
			} else {
				mListener.OnResult(false, mVehicleModel, null);
			}
		}
	}
}
