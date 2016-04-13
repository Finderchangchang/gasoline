package com.app.bulkgasoline.task;

import java.util.Map;

import android.os.Message;
import android.util.Log;

import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.utils.Utils;

/**
 *
 *
 * @author 柳伟杰 E-mail:1031066280@qq.com
 * @version 创建时间：2015-7-6 下午3:25:02
 */
public class TestTask extends HttpTask{
	private String linkway;

	public TestTask(GlobalModel global, AuthorizeModel authorize,
					String linkway) {
		super(global, authorize);
		this.linkway = linkway;
	}

	private GetTestListener listener;

	public interface GetTestListener {
		public void getCompany(boolean result, String companyId);
	}

	public void onStart(GetTestListener companyListener) {
		listener = companyListener;
		start();
	}
	@Override
	protected boolean isPost() {
		return true;
	}
	@Override
	public String getActionName() {
		return "GetCompanyId";
	}

	@Override
	public String getDataType() {
		return REQUEST_TYPE_DATA;
	}

	@Override
	protected void prepareForms() {
		super.addArgument("linkWay", linkway);
	}


	@Override
	public void onInvokeReturn(boolean result, Map<String, String> data,
							   Message message) {
		// TODO Auto-generated method stub
		System.out.println(message);
		if (listener != null) {
			if (!result) {
				listener.getCompany(false, "网络错误");
			} else {
				Log.e("error",data.toString());
				String success = data.get("Success");
				String companyId = data.get("CompanyId");
				if (Utils.isEmpty(success)
						|| "true".compareToIgnoreCase(success) != 0

						|| companyId.isEmpty())
					listener.getCompany(false, "获取企业信息失败");
				else
					listener.getCompany(true, companyId);
			}
		}
	}
}
