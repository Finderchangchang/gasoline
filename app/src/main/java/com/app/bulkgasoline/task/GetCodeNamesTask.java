package com.app.bulkgasoline.task;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import android.os.Message;

import com.app.bulkgasoline.model.AuthorizeModel;

public class GetCodeNamesTask extends HttpTask {

	private Set<String> mCodeNamesList;
	private String mCodeNamesXml;

	public GetCodeNamesTask(GlobalModel global, AuthorizeModel authorize) {
		super(global, authorize);
		mCodeNamesList = new LinkedHashSet<String>();
	}

	private GetCodeNamesListener mListener;

	public interface GetCodeNamesListener {
		public void OnResult(boolean result, String xml, String strMessage);
	}

	public void start(GetCodeNamesListener listener) {
		mListener = listener;
		start();
	}

	@Override
	public String getActionName() {
		return "GetCodeNames";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}

	@Override
	protected void prepareForms() {

	}

	@Override
	public Map<String, String> parseXml(String content) {
		mCodeNamesXml = content;
		Map<String, String> data = new HashMap<String, String>();
		Jxml xml = new Jxml();
		if (xml.SetDoc(content) && xml.FindElem("InvokeReturn")) {
			xml.IntoElem();
			while (xml.FindElem()) {
				String key, value;
				key = xml.GetTagName();
				if ("Object".compareToIgnoreCase(key) == 0) {
					if (mCodeNamesList != null)
						mCodeNamesList.clear();

					xml.IntoElem();
					while (xml.FindElem()) {
						value = xml.GetData();
						mCodeNamesList.add(value);
					}
					xml.OutOfElem();
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
						mCodeNamesXml, strMessage);
			} else {
				mListener.OnResult(false, null, "网络错误！");
			}
		}
	}
}
