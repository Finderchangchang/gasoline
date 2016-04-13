package com.app.bulkgasoline;

import com.app.bulkgasoline.task.GetCompanyTask;
import com.app.bulkgasoline.task.GetCompanyTask.GetCompanyListener;
import com.app.bulkgasoline.utils.Utils;

import android.os.Bundle;
import android.os.Handler;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

	private PagerTab tabs;
	private ViewPager pager;
	private CalcPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initTitleBarText();
		initViewPages();
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				pager.setVisibility(View.VISIBLE);
			}
		});
	}

	private void initTitleBarText() {
		TextView title_text = (TextView) findViewById(R.id.id_title_bar_text);
		title_text.setText(R.string.title_name);

		ImageView setting_icon = (ImageView) findViewById(R.id.id_button_setting);

		setting_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SettingActivity.class);
				startActivity(intent);
			}
		});

		TextView name_text = (TextView) findViewById(R.id.id_name_title);
		String name = Utils.ReadString(this, "Key_CompanyName");
		if (!Utils.isEmpty(name))
			name_text.setText(name);

		updateCompanyInfo();
	}

	private void updateCompanyInfo() {
		GetCompanyTask task = new GetCompanyTask(getGlobalModel(),
				getAuthorizeModel());

		task.start(mGetCompanyListener);
	}

	private GetCompanyListener mGetCompanyListener = new GetCompanyListener() {

		@Override
		public void OnResult(boolean result, String strMessage, String strName,
				String strDepartmentId) {
			if (result) {
				TextView name = (TextView) findViewById(R.id.id_name_title);
				name.setText(strName);
				Utils.WriteString(MainActivity.this, "key_DepartmentId",
						strDepartmentId);
				Utils.WriteString(MainActivity.this, "Key_CompanyName", strName);
			}
		}
	};

	private void initViewPages() {
		adapter = new CalcPagerAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(adapter);
		tabs = (PagerTab) findViewById(R.id.tabs);
		tabs.setShouldExpand(true);
		tabs.setViewPager(pager);
	}

	public class CalcPagerAdapter extends FragmentPagerAdapter {
		private String[] mTasbText;

		public CalcPagerAdapter(FragmentManager fm) {
			super(fm);

			mTasbText = getResources().getStringArray(R.array.main_tabs_text);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTasbText[position];
		}

		@Override
		public int getCount() {
			return mTasbText.length;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = null;
			switch (position) {
			case 0:
				fragment = new FragmentAddCustomer();
				break;
			case 1:
				fragment = new FragmentAddVehicle();
				break;
			case 2:
				fragment = new FragmentQueryInfo();
				break;
			case 3:
				fragment = new FragmentKeyAlarm();
				break;
			}
			return fragment;
		}
	}

	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.text_to_exit)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setPositiveButton(R.string.text_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								MainActivity.super.onBackPressed();
							}
						}).setNegativeButton(R.string.text_cancel, null).show();
	}
}
