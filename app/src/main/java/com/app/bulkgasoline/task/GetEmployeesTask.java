package com.app.bulkgasoline.task;

import java.util.HashMap;
import java.util.Map;
import android.os.Message;
import com.app.bulkgasoline.model.AuthorizeModel;

public class GetEmployeesTask extends HttpTask {

	protected static String COMPANYID = "CompanyId";
	private String mEmployeesXml;

	public GetEmployeesTask(GlobalModel global, AuthorizeModel authorize) {
		super(global, authorize);
	}

	private GetEmployeesListener mListener;

	public interface GetEmployeesListener {
		public void OnResult(boolean result, String xml, String strMessage);
	}

	public void start(GetEmployeesListener listener) {
		mListener = listener;
		start();
	}

	@Override
	public String getActionName() {
		return "GetEmployees";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}

	@Override
	protected void prepareForms() {
		super.addArgument(COMPANYID, mAuthorizeModel.UserId);
	}

	@Override
	public Map<String, String> parseXml(String content) {
		mEmployeesXml = content;

		Map<String, String> data = new HashMap<String, String>();
		Jxml xml = new Jxml();
		if (xml.SetDoc(content) && xml.FindElem("InvokeReturn")) {
			xml.IntoElem();
			while (xml.FindElem()) {
				String key, value;
				key = xml.GetTagName();
				if ("Object".compareToIgnoreCase(key) == 0) {

				} else {
					value = xml.GetData();
					data.put(key, value);
				}
			}
			xml.OutOfElem();
		}

		return data;
	}

	@Override
	public void onInvokeReturn(boolean result, Map<String, String> data,
							   Message msg) {
		if (mListener != null) {
			if (result) {
				String strSuccess = data.get("Success");
				String strMessage = data.get("Message");

				mListener.OnResult("true".compareToIgnoreCase(strSuccess) == 0,
						mEmployeesXml, strMessage);
			} else {
				mListener.OnResult(false, null, "网络错误！");
			}
		}
	}
}
