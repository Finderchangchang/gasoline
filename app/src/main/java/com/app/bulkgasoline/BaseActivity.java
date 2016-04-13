package com.app.bulkgasoline;

import com.app.bulkgasoline.http.Cryptography;
import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.task.GlobalModel;
import com.app.bulkgasoline.utils.Utils;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.ProgressDialog;

public class BaseActivity extends FragmentActivity {

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showProgressDialog(String text) {
        if (mProgressDialog != null)
            mProgressDialog.cancel();

        mProgressDialog = ProgressDialog.show(this, null, text, true, false);
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
            model.SessionId = Utils.ReadString(this, Utils.KEY_SESSIONID);
            model.UserId = Utils.ReadString(this, Utils.KEY_USERNAME);
            model.Password = Utils.ReadString(this, Utils.KEY_PASSWORD);
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
            model.UserId = Utils.ReadString(this, Utils.KEY_USERNAME);
            model.Password = Utils.ReadString(this, Utils.KEY_PASSWORD);
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
            model.ServerName = Utils.ReadString(this, Utils.KEY_SERVERNAME);// 用户名
            model.ServerPort = Utils.ReadString(this, Utils.KEY_PORT);// 密码
        }
        return model;
    }
}
