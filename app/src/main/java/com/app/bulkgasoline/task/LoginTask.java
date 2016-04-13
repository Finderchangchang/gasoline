package com.app.bulkgasoline.task;

import java.util.Map;
import android.os.Message;

import com.app.bulkgasoline.http.Cryptography;
import com.app.bulkgasoline.http.HTTPResponse;
import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.utils.DBHelper;
import com.app.bulkgasoline.utils.Utils;

public class LoginTask extends HttpTask {

	private boolean mSameUserId;

	public LoginTask(GlobalModel global, AuthorizeModel authorize,
					 boolean sameid) {
		super(global, authorize);

		mSameUserId = sameid;
	}

	private LoginListener mListener;

	public interface LoginListener {
		void OnResult(boolean result, AuthorizeModel model,
							 String message);
	}

	public void start(LoginListener listener) {
		mListener = listener;
		start();
	}

	// 访问后台的方法
	@Override
	public String getActionName() {
		return "Login";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}
	@Override
	protected boolean isPost() {
		return true;
	}
	protected void onRunEnd(Message message) {
		HTTPResponse resp = (HTTPResponse) message.obj;
		if (resp != null && resp.code == 200) {
			// if(!mSameUserId)
			if (true) {
				// 使用不同UserId登录成功时，清除缓存
				String content = Cryptography.DecryptString(resp.content, "");
				System.out.println("content:" + content);
				Map<String, String> data = parseXml(content);
				String strSuccess = data.get("Success");
				if ("true".equalsIgnoreCase(strSuccess)) {
					DBHelper.deleteCache();
				}
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
				if (Utils.isEmpty(strSuccess)
						|| "true".compareToIgnoreCase(strSuccess) != 0) {
					mListener.OnResult(false, null, strMessage);
				} else {
					mAuthorizeModel.SessionId = data.get("SessionId");
					mListener.OnResult(true, mAuthorizeModel, null);
				}
			} else {
				mListener.OnResult(false, null, "网络错误！");
			}
		}
	}
}
