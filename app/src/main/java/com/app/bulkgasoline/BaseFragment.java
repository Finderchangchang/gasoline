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

public abstract class BaseFragment extends Fragment {
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

    public abstract void initViews();
    /** Fragment当前状态是否可见 */
    protected boolean isVisible;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }


    /**
     * 不可见
     */
    protected void onInvisible() {


    }


    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mContext = getActivity();
        String carNum = Utils.ReadString(MainActivity.mIntails, Utils.KEY_PAIZHAO_NUM);
        initViews();
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        GlobalModel model = new GlobalModel();
        {
            model.ServerName = Utils.ReadString(mContext, Utils.KEY_SERVERNAME);
            model.ServerPort = Utils.ReadString(mContext, Utils.KEY_PORT);
        }
        return model;
    }
}