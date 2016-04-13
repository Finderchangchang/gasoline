package com.app.bulkgasoline.task;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import Decoder.BASE64Encoder;
import android.os.Message;

import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.model.CustomerModel;
import com.app.bulkgasoline.utils.DBHelper;

public class AddCustomerTask extends HttpTask {

	protected static String CUSTOMER_IMAGE_ID = "CustomerImageId";
	protected static String CUSTOMER_NAME = "CustomerName";
	protected static String CUSTOMER_INDENTITY_TYPE = "CustomerIdentityType";
	protected static String CUSTOMER_IDENTITYNUMBER = "CustomerIdentityNumber";
	protected static String CUSTOMER_LINKWAY = "CustomerLinkway";
	protected static String COUNT = "Count";
	protected static String PURPOSE = "Purpose";
	protected static String CUSTOMER_ADDRESS = "customerAddress";
	protected static String EMPOLYEE_ID = "EmployeeId";
	protected static String SUSPICIOUS = "Suspicious";
	protected static String SUSPICIOUS_RESON = "SuspiciousReson";

	private CustomerModel mCustomerModel;

	public AddCustomerTask(GlobalModel global, AuthorizeModel authorize,
			CustomerModel model) {
		super(global, authorize);

		mCustomerModel = model;
	}

	private AddCustomerListener mListener;

	public interface AddCustomerListener {
		void OnResult(boolean result, CustomerModel customer,
				String message);
	}

	public void setMobileLoginListener(AddCustomerListener listener) {
		mListener = listener;
	}

	public void start(AddCustomerListener listener) {
		mListener = listener;
		start();
	}

	@Override
	protected boolean isPost() {
		return true;
	}

	@Override
	public String getActionName() {
		return "AddCustomer";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}

	@Override
	public void prepareForms() {
		if (mCustomerModel != null) {
			try {
				super.addArgument("Model", new BASE64Encoder()
						.encode(mCustomerModel.Serialization()
								.getBytes("UTF-8")));
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
						mCustomerModel, strMessage);
			} else {
				mListener.OnResult(false, mCustomerModel, null);
			}
		}
	}
}
