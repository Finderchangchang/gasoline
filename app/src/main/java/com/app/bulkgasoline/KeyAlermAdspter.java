package com.app.bulkgasoline;

import java.util.ArrayList;
import com.app.bulkgasoline.model.PoliceModel;
import com.app.bulkgasoline.utils.CustomDialog;
import com.app.bulkgasoline.utils.Utils;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 自定义Adapter（一键报警功能）
 * */
public class KeyAlermAdspter extends BaseAdapter {
	private Context context;
	private ArrayList<PoliceModel> mPolices;
	private LayoutInflater inflater;

	public KeyAlermAdspter(Context context, ArrayList<PoliceModel> police) {
		super();
		this.context = context;
		this.mPolices = police;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mPolices.size();
	}

	@Override
	public Object getItem(int position) {
		return mPolices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 重写adapter页面
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			// 获得行显示信息
			final PoliceModel police = mPolices.get(position);
			// 行布局模板
			view = inflater.inflate(R.layout.key_alerm_list, null);
			// 打到行布局模板中要显示的控件
			TextView name = (TextView) view.findViewById(R.id.tName);
			TextView linker = (TextView) view.findViewById(R.id.tLinker);
			Button btn = (Button) view.findViewById(R.id.id_button_alerm);
			// 给控件赋值
			name.setText(police.PoliceName);
			linker.setText(police.PoliceLinkway);
			btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					showAlertDialog(view, police.PoliceLinkway);
				}
			});
		}
		return view;
	}
	public void showAlertDialog(final View view, final String tel) {
		CustomDialog.Builder builder = new CustomDialog.Builder(
				view.getContext());
		final String message = "短信内容：\n  我是"
				+ Utils.ReadString(context, "Key_CompanyName")
				+ "工作人员，我在加油过程中遇到紧急情况，请民警同志速来支援";
		builder.setMessage(message, "", false);
		builder.setView(null, false);
		builder.setTitle("民警电话：" + tel);
		builder.setPositiveButton("打电话", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();// 关闭自定义弹出框
				// 用intent启动拨打电话
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
						+ tel));
				view.getContext().startActivity(intent);
			}
		});

		builder.setNegativeButton("发短信",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();// 关闭提示窗
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage(tel, null, message, null, null);
					}
				});

		builder.create().show();
	}

}
