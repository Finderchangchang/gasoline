package com.app.bulkgasoline.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.os.Message;

import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.utils.Utils;

public class GetCodesTask extends HttpTask {

	protected static String CODENAME = "CodeName";

	private ArrayList<String> mCodeNames;
	private ArrayList<String> mCodesValues;
	private ArrayList<String> mCodesKeys;

	private String mCodeXml;

	private String mCodeName;

	public GetCodesTask(GlobalModel global, AuthorizeModel authorize,
			ArrayList<String> codeNames) {
		super(global, authorize);

		mCodeXml = "";
		mCodeNames = codeNames;
		mCodesKeys = new ArrayList<String>();
		mCodesValues = new ArrayList<String>();
	}

	private GetCodesListener mListener;

	public interface GetCodesListener {
		public void OnResult(boolean result);

		public void OnProgress(boolean result, String name, int index,
				int count, String xml, String strMessage);
	}

	public void setMobileLoginListener(GetCodesListener listener) {
		mListener = listener;
	}

	public void start(GetCodesListener listener) {
		mListener = listener;
		start();
	}

	@Override
	public String getActionName() {
		return "GetCodes";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}

	@Override
	public void prepareForms() {
		super.addArgument(CODENAME, mCodeName);
	}

	@Override
	public void run() {

		ArrayList<Message> msgList = new ArrayList<Message>();
		int index = 1;
		for (String key : mCodeNames) {
			mCodeName = key;
			prepareData();

			Bundle data = new Bundle();
			data.putInt("index", index++);
			data.putString("name", mCodeName);

			Message msg = new Message();
			msg.obj = httpGet(mHttpUrl);
			msg.setData(data);

			msgList.add(msg);
		}

		for (Message msg : msgList) {
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public Map<String, String> parseXml(String content) {
		mCodeXml = content;
		System.out.println("cc:"+mCodeXml);
		Map<String, String> data = new HashMap<String, String>();
		Jxml xml = new Jxml();
		if (xml.SetDoc(content) && xml.FindElem("InvokeReturn")) {
			xml.IntoElem();
			while (xml.FindElem()) {
				String key, value;
				key = xml.GetTagName();
				if ("Object".compareToIgnoreCase(key) == 0) {
					mCodesKeys.clear();
					mCodesValues.clear();

					xml.IntoElem();
					while (xml.FindElem("CodeModel")) {
						xml.IntoElem();
						while (xml.FindElem()) {
							key = xml.GetTagName();
							value = xml.GetData();
							if (key.compareToIgnoreCase("Key") == 0) {
								mCodesKeys.add(value);
							} else if (key.compareToIgnoreCase("Value") == 0) {
								mCodesValues.add(value);
							}
						}
						xml.OutOfElem();
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
				boolean success = false;
				if (!Utils.isEmpty(strSuccess))
					success = ("true".compareToIgnoreCase(strSuccess) == 0);

				Bundle bundle = msg.getData();
				int index = bundle.getInt("index");
				String name = bundle.getString("name");

				mListener.OnProgress(success, name, index, mCodeNames.size(),
						mCodeXml, strMessage);
				if (index >= mCodeNames.size())
					mListener.OnResult(true);
			} else {
				mListener.OnResult(false);
			}
		}
	}
}
