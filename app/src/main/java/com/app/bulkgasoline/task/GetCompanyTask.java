package com.app.bulkgasoline.task;

import java.util.Map;
import android.os.Message;
import android.util.Log;

import com.app.bulkgasoline.model.AuthorizeModel;

public class GetCompanyTask extends HttpTask {

	private String COMPANY_ID = "CompanyId";

	public GetCompanyTask(GlobalModel global, AuthorizeModel authorize) {
		super(global, authorize);

	}

	private GetCompanyListener mListener;

	public interface GetCompanyListener {
		public void OnResult(boolean result, String strMessage, String strName,String strDepartmentId);
	}

	public void start(GetCompanyListener listener) {
		mListener = listener;
		start();
	}

	@Override
	public String getActionName() {
		return "GetCompany";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}

	@Override
	public void prepareForms() {
		super.addArgument(COMPANY_ID, mAuthorizeModel.UserId);
	}

	@Override
	public void onInvokeReturn(boolean result, Map<String, String> data,
			Message msg) {
		if (mListener != null) {
			if (result) {
				Log.i("jjccc", data.toString());
				String strSuccess = data.get("Success");
				String strMessage = data.get("Message");
				String strName = data.get("CompanyName");
				String strDepartmentId=data.get("DepartmentId");
				mListener.OnResult("true".equalsIgnoreCase(strSuccess),
						strMessage, strName,strDepartmentId);
			} else {
				mListener.OnResult(false, "", "","");
			}
		}
	}
}