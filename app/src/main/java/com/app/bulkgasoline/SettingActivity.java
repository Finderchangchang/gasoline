package com.app.bulkgasoline;

import com.app.bulkgasoline.utils.Utils;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SettingActivity extends BaseActivity {

    private EditText edit_server;
    private EditText edit_port;
    private SpinnerReadCardDevice read_card_spinner;

    private static int BLUETTOOTH_RESULT = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initTitleBarText();
        initSettingsViews();
        checkBluetooth();
    }

    private void initTitleBarText() {
        ImageView setting_back = (ImageView) findViewById(R.id.id_button_back);

        setting_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initSettingsViews() {
        edit_server = (EditText) findViewById(R.id.id_text_server);
        edit_port = (EditText) findViewById(R.id.id_text_port);
        read_card_spinner = (SpinnerReadCardDevice) findViewById(R.id.id_text_read_card_spinner);

        String server = Utils.ReadString(this, Utils.KEY_SERVERNAME);
        edit_server.setText(server);

        String port = Utils.ReadString(this, Utils.KEY_PORT);
        edit_port.setText(port);
    }

    private boolean checkBluetooth() {
        boolean result = true;
        BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
        if (null != ba && !ba.isEnabled()) {
            result = false;
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BLUETTOOTH_RESULT);
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BLUETTOOTH_RESULT == requestCode) {
            if (resultCode == RESULT_OK) {
                read_card_spinner = (SpinnerReadCardDevice) findViewById(R.id.id_text_read_card_spinner);
                read_card_spinner.initSpinner(this);
            } else {
                Toast.makeText(this, R.string.text_bluetooth_not_open,
                        Toast.LENGTH_SHORT).show();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onButtonSetting(View view) {
        Utils.hideIM(SettingActivity.this);

        if (Utils.fillTestData()) {
            edit_server.setText("hbdwkj.oicp.net");
            edit_port.setText("8090");
        }

        if (checkSettingInput()) {
            saveServerInfo();
        }
    }

    private boolean checkSettingInput() {
        String server = Utils.getString(edit_server);
        if (TextUtils.isEmpty(server)) {
            Toast.makeText(this, R.string.text_please_input_server,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * 保存系统配置信息
     */
    private void saveServerInfo() {
        String servername = edit_server.getText().toString();
        Utils.WriteString(this, Utils.KEY_SERVERNAME, servername);

        String port = edit_port.getText().toString();
        Utils.WriteString(this, Utils.KEY_PORT, port);

        String address = read_card_spinner.getSelectedKey();
        Utils.WriteBlueToothAddress(this, address);

        // Utils.WriteBoolean(SettingActivity.this, Utils.KEY_REMEMBER,
        // remenber_pwd.isChecked());

        Toast.makeText(this, R.string.text_setting_saved, Toast.LENGTH_SHORT)
                .show();
    }
}
