package com.app.bulkgasoline;
import com.app.bulkgasoline.http.Cryptography;
import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.task.GlobalModel;
import com.app.bulkgasoline.utils.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;

public class BaseFragment extends Fragment {
	protected Activity mContext;
	protected View mContentView;
	private ProgressDialog mProgressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	protected int getLayoutResourceId() {
		return 0;
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		FrameLayout fl = new FrameLayout(getActivity());
		fl.setLayoutParams(params);

		View content = inflater.inflate(getLayoutResourceId(), null);

		fl.addView(content);
		return fl;
	}

	protected void initFragment(Bundle savedInstanceState) {
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		mContext = getActivity();
		initFragment(savedInstanceState);
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {

		mContentView = view;
		super.onViewCreated(view, savedInstanceState);
	}

	protected void showProgressDialog(String text) {
		if (mProgressDialog != null)
			mProgressDialog.cancel();

		mProgressDialog = ProgressDialog
				.show(mContext, null, text, true, false);
	}

	protected void showProgressDialog(int id) {
		String text = this.getResources().getString(id);
		this.showProgressDialog(text);
	}

	protected void hideProgressDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.cancel();
			mProgressDialog = null;
		}
	}

	protected AuthorizeModel getAuthorizeModel() {
		AuthorizeModel model = new AuthorizeModel();
		{
			model.SessionId = Utils.ReadString(mContext, Utils.KEY_SESSIONID);
			model.UserId = Utils.ReadString(mContext, Utils.KEY_USERNAME);
			model.Password = Utils.ReadString(mContext, Utils.KEY_PASSWORD);
			if (!Utils.isEmpty(model.Password)) {
				model.Password = Cryptography.HashPassword(model.Password, "");
			}
		}
		return model;
	}

	protected AuthorizeModel getLoginAuthorizeModel() {
		AuthorizeModel model = new AuthorizeModel();
		{
			model.SessionId = "";
			model.UserId = Utils.ReadString(mContext, Utils.KEY_USERNAME);
			model.Password = Utils.ReadString(mContext, Utils.KEY_PASSWORD);
			if (!Utils.isEmpty(model.Password)) {
				model.Password = Cryptography.HashPassword(model.Password,
						model.SessionId);
			}
		}
		return model;
	}

	protected GlobalModel getGlobalModel() {
		System.out.println(Utils.KEY_SERVERNAME);
		GlobalModel model = new GlobalModel();
		{
			model.ServerName = Utils.ReadString(mContext, Utils.KEY_SERVERNAME);
			model.ServerPort = Utils.ReadString(mContext, Utils.KEY_PORT);
		}
		System.out.println("....");
		return model;
	}
}