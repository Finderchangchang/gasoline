package com.app.bulkgasoline;

import com.app.bulkgasoline.utils.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ViewFlipper;

/**
 * 查询功能页面
 */
public class FragmentQueryInfo extends BaseFragment implements OnClickListener {

    private CheckBox query_customer;
    private CheckBox query_vehicle;
    private QueryTypeSpinner query_type;
    private ViewFlipper view_flipper;

    private Button btn_query;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getLayoutResourceId() {
        return R.layout.layout_query;
    }

    @Override
    public void initViews() {
        query_customer = (CheckBox) mContentView
                .findViewById(R.id.id_query_customer);
        query_vehicle = (CheckBox) mContentView
                .findViewById(R.id.id_query_vehicle);
        query_type = (QueryTypeSpinner) mContentView
                .findViewById(R.id.id_query_type_spinner);
        view_flipper = (ViewFlipper) mContentView
                .findViewById(R.id.id_view_flipper);
        btn_query = (Button) mContentView.findViewById(R.id.id_button_query);
        query_type.initSpinner(mContext, true);
        query_type.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                onClickQueryType();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }

        });


        query_customer.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                query_customer.setChecked(true);
                query_vehicle.setChecked(false);
                query_type.initSpinner(mContext, true);
            }
        });

        query_vehicle.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                query_vehicle.setChecked(true);
                query_customer.setChecked(false);
                query_type.initSpinner(mContext, false);
            }
        });


        btn_query.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                System.out.println("输出点击............................");
                Utils.hideIM(mContext);

                Intent intent = new Intent();
                if (query_customer.isChecked())
                    intent.setClass(mContext, QueryResultCustomerActivity.class);
                else
                    intent.setClass(mContext, QueryResultVehicleActivity.class);

                String key = query_type.getSelectedKey();
                String value = getQueryValue();

                intent.putExtra("key", key);
                intent.putExtra("value", value);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void lazyLoad() {
        if (("1").equals(Utils.ReadString(MainActivity.mIntails, Utils.KEY_ZHONGDIAN))) {
            Utils.WriteString(MainActivity.mIntails, Utils.KEY_PAIZHAO_NUM, "");
        }

    }

    private void onClickQueryType() {
        int position = query_type.getSelectedItemPosition();
        if (query_customer.isChecked()) {
            if (position == 1)
                view_flipper.setDisplayedChild(1);
            else if (position == 5)
                view_flipper.setDisplayedChild(2);
            else if (position == 7)
                view_flipper.setDisplayedChild(3);
            else if (position == 8)
                view_flipper.setDisplayedChild(5);
            else
                view_flipper.setDisplayedChild(0);
        } else {
            if (position == 0)
                view_flipper.setDisplayedChild(4);
            else if (position == 5)
                view_flipper.setDisplayedChild(3);
            else
                view_flipper.setDisplayedChild(0);
        }
    }

    private String getQueryValue() {

        String value = "";

        int position = query_type.getSelectedItemPosition();
        if (query_customer.isChecked()) {
            if (position == 1) {
                SpinnerCertiType spinner = (SpinnerCertiType) mContentView
                        .findViewById(R.id.id_certi_type_spinner);
                value = spinner.getSelectedText();
            } else if (position == 5) {
                SpinnerBuyIntent spinner = (SpinnerBuyIntent) mContentView
                        .findViewById(R.id.id_text_buy_intent_spinner);
                value = spinner.getSelectedText();
            } else if (position == 7) {
                SpinnerOperator spinner = (SpinnerOperator) mContentView
                        .findViewById(R.id.id_text_operator_spinner);
                value = spinner.getSelectedText();
            } else if (position == 8) {
                CheckBox checkbox = (CheckBox) mContentView
                        .findViewById(R.id.id_text_doubt);
                value = checkbox.isChecked() ? "是" : "否";
            } else if (position == 9) {

            } else if (position == 10) {

            } else {
                EditText edittext = (EditText) mContext
                        .findViewById(R.id.id_text_key);
                value = edittext.getText().toString();
            }
        } else {
            if (position == 0) {
                SpinnerVehicleType spinner = (SpinnerVehicleType) mContentView
                        .findViewById(R.id.id_text_vehicle_type_spinner);
                value = spinner.getSelectedText();
            } else if (position == 5) {
                SpinnerOperator spinner = (SpinnerOperator) mContentView
                        .findViewById(R.id.id_text_operator_spinner);
                value = spinner.getSelectedText();
            } else {
                EditText edittext = (EditText) mContentView
                        .findViewById(R.id.id_text_key);
                value = edittext.getText().toString();
            }
        }

        return value;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {

    }
}