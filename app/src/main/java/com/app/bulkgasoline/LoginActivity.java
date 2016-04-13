package com.app.bulkgasoline;

import java.util.ArrayList;

import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.task.GetCodeNamesTask;
import com.app.bulkgasoline.task.GetCodesTask;
import com.app.bulkgasoline.task.GetEmployeesTask;
import com.app.bulkgasoline.task.GlobalModel;
import com.app.bulkgasoline.task.LoginTask;
import com.app.bulkgasoline.task.GetCodeNamesTask.GetCodeNamesListener;
import com.app.bulkgasoline.task.GetCodesTask.GetCodesListener;
import com.app.bulkgasoline.task.GetEmployeesTask.GetEmployeesListener;
import com.app.bulkgasoline.task.LoginTask.LoginListener;
import com.app.bulkgasoline.utils.Utils;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 登陆Activity
 * */
public class LoginActivity extends BaseActivity {

	private EditText edit_username;
	private EditText edit_password;
	private Button button_login;
	private CheckBox checkbox_remember;

	private ArrayList<String> mCodeNames;
	private String mCodeNamesXml;

	private AuthorizeModel mAuthorizeModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);


		initTitleBarText();
		initLoginButtonListener();
		initCheckBoxRemember();

		mAuthorizeModel = getLoginAuthorizeModel();
	}

	private void initTitleBarText() {
		ImageView setting_icon = (ImageView) findViewById(R.id.id_button_setting);
		setting_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(LoginActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});
	}

	private void initCheckBoxRemember() {
		checkbox_remember = (CheckBox) findViewById(R.id.id_checkbox_remember);
		checkbox_remember.setChecked(Utils.ReadBoolean(LoginActivity.this,
				Utils.KEY_REMEMBER));
		if (checkbox_remember.isChecked()) {
			edit_username.setText(Utils.ReadString(LoginActivity.this,
					Utils.KEY_USERNAME));
			// edit_password.setText(Utils.ReadString(LoginActivity.this,
			// Utils.KEY_PASSWORD));
			// button_login.performClick();
		}

		checkbox_remember
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
												 boolean isChecked) {
						Utils.WriteBoolean(LoginActivity.this,
								Utils.KEY_REMEMBER, isChecked);
					}
				});
	}

	private void initLoginButtonListener() {
		edit_username = (EditText) findViewById(R.id.id_text_username);
		edit_password = (EditText) findViewById(R.id.id_text_password);

		if (Utils.fillTestData()) {
			edit_username.setText("1306010001");
			edit_password.setText("12345");
		}

		button_login = (Button) findViewById(R.id.id_button_login);
		// 点击登录按钮触发事件
		button_login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Utils.hideIM(LoginActivity.this);
				// if (checkServerInfo() && checkInput()) {
				startLoginThread();
				// }
			}
		});
	}

	/**
	 * 判断是否存在服务器地址
	 * */
	private boolean checkServerInfo() {
		GlobalModel model = getGlobalModel();
		if (model == null || Utils.isEmpty(model.ServerName)) {
			Toast.makeText(this, R.string.text_please_input_server,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	/**
	 * 判断账户密码是否为空
	 * */
	private boolean checkInput() {
		String username = Utils.getString(edit_username);
		if (TextUtils.isEmpty(username)) {
			Toast.makeText(this, R.string.text_please_input_username,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		String password = Utils.getString(edit_password);
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(this, R.string.text_please_input_password,
					Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private void startLoginThread() {
		saveLoginInfo();// 保存登录信息

		String userid = edit_username.getText().toString();
		boolean same = userid.equalsIgnoreCase(mAuthorizeModel.UserId);
		LoginTask task = new LoginTask(getGlobalModel(),
				getLoginAuthorizeModel(), same);
		task.start(mListener);

		showProgressDialog(R.string.text_logining_system);
	}

	private LoginListener mListener = new LoginListener() {
		@Override
		public void OnResult(boolean result, AuthorizeModel model,
							 String message) {

			hideProgressDialog();

			if (!result) {
				String text = getResources()
						.getString(R.string.text_login_fail) + "\n" + message;
				Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT)
						.show();
			} else {
				Utils.WriteString(LoginActivity.this, Utils.KEY_SESSIONID,
						model.SessionId);

				AuthorizeModel oldModel = getLoginAuthorizeModel();
				if (oldModel != null && model != null) {
					if (!oldModel.UserId.equalsIgnoreCase(model.UserId))
						clearCacheData();
				}

				startCheckGetBaseData();
			}
		}
	};

	private void clearCacheData() {

	}

	private void onLoginOK() {
		Intent intent = new Intent();
		intent.setClass(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		finish();
	}

	private void saveLoginInfo() {
		String username = edit_username.getText().toString();
		Utils.WriteString(this, Utils.KEY_USERNAME, username);

		String password = edit_password.getText().toString();
		Utils.WriteString(this, Utils.KEY_PASSWORD, password);
	}

	private void startCheckGetBaseData() {
		// 每次登录更新字典／加油员
		GetCodeNamesTask task = new GetCodeNamesTask(getGlobalModel(),
				getAuthorizeModel());
		task.start(mGetCodeNamesListener);
		showProgressDialog(R.string.text_updating_basedata);
	}

	private GetCodeNamesListener mGetCodeNamesListener = new GetCodeNamesListener() {
		@Override
		public void OnResult(boolean result, String xml, String strMessage) {

			if (!result) {
				String message = String
						.format("%s\n%s", LoginActivity.this.getResources()
										.getString(R.string.text_update_basedata_fail),
								strMessage);
				Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT)
						.show();
				hideProgressDialog();
			} else {
				mCodeNamesXml = xml;

				if (mCodeNames == null)
					mCodeNames = new ArrayList<String>();
				else
					mCodeNames.clear();

				// 不需要所有的字典

				mCodeNames.add("Code_IdentityType");
				mCodeNames.add("Code_Purpose");
				mCodeNames.add("Code_VehicleType");
				mCodeNames.add("Code_VehicleColor");
				mCodeNames.add("Code_GasolineType");
				mCodeNames.add("Code_TransportType");
				mCodeNames.add("Code_DangerZone");
				startGetAllCodes();
			}
		}
	};

	private void startGetAllCodes() {
		GetCodesTask task = new GetCodesTask(getGlobalModel(),
				getAuthorizeModel(), mCodeNames);
		task.start(mGetCodesListener);
	}

	private GetCodesListener mGetCodesListener = new GetCodesListener() {

		@Override
		public void OnResult(boolean result) {
			hideProgressDialog();

			if (!result) {
				Toast.makeText(LoginActivity.this,
						R.string.text_update_basedata_fail, Toast.LENGTH_SHORT)
						.show();
			} else {
				Utils.writeCodeNames(LoginActivity.this, mCodeNamesXml);

				startGetEmployees();

				/*
				 * if(!Utils.hasOperators(LoginActivity.this)) {
				 * startGetEmployees(); } else {
				 * Toast.makeText(LoginActivity.this,
				 * R.string.text_update_basedata_success,
				 * Toast.LENGTH_SHORT).show(); onLoginOK(); }
				 */
			}
		}

		@Override
		public void OnProgress(boolean result, String name, int index,
							   int count, String xml, String strMessage) {

			if (result) {
				Utils.writeCodes(LoginActivity.this, name, xml);
			}
		}
	};

	private void startGetEmployees() {
		GetEmployeesTask task = new GetEmployeesTask(getGlobalModel(),
				getAuthorizeModel());
		task.start(mGetEmployeesListener);
	}

	private GetEmployeesListener mGetEmployeesListener = new GetEmployeesListener() {

		@Override
		public void OnResult(boolean result, String xml, String strMessage) {

			if (result) {
				Utils.writeOperators(LoginActivity.this, xml);
				onLoginOK();
			} else {
				String message = String
						.format("%s\n%s", LoginActivity.this.getResources()
										.getString(R.string.text_update_basedata_fail),
								strMessage);
				Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT)
						.show();
				hideProgressDialog();
			}
		}
	};

}
