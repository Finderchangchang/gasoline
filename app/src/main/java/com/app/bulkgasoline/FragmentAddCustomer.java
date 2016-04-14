package com.app.bulkgasoline;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import com.app.bulkgasoline.MainActivity.CalcPagerAdapter;
import com.app.bulkgasoline.model.CustomerModel;
import com.app.bulkgasoline.task.AddCustomerTask;
import com.app.bulkgasoline.task.CacheCustomerTask;
import com.app.bulkgasoline.task.Cvr100bTask;
import com.app.bulkgasoline.task.AddCustomerTask.AddCustomerListener;
import com.app.bulkgasoline.task.Cvr100bTask.Cvr100bListener;
import com.app.bulkgasoline.utils.CustomDialog;
import com.app.bulkgasoline.utils.DBHelper;
import com.app.bulkgasoline.utils.Utils;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * 添加销售信息
 */
public class FragmentAddCustomer extends BaseFragment implements
        OnClickListener {
    private Boolean result = null;//判断是否已经弹出窗口(true为一致，false为不一致)
    // #start --------相关组件定义--------------
    private PhotosLinearLayout customer_photos;
    private EditText customer_name;
    private SpinnerCertiType certi_type;
    private Button read_card;
    private EditText certi_number;
    private EditText birth_day;
    private EditText company_name;
    private EditText tel_number;
    private EditText people_address;
    private EditText buy_liter;
    private SpinnerGasolineType gasoline_type;// 油品种类
    private SpinnerTransportType transport_type;// 运输方式
    private EditText vehicle_number;// 车牌号
    private LinearLayout id_vehicle_number;// 禁用启用车牌号
    private SpinnerBuyIntent buy_intent;// 购买用途
    private SpinnerOperator customer_operator;// 加油员
    private CheckBox is_doubt;
    private EditText doubt_desc;
    private View doubt_desc_layout;
    private EditText customer_nation;
    private Button btn_submit;
    private ImageView zhengjian_img_iv;
    private ImageView zhuapai_img_iv;
    private static int BLUETTOOTH_RESULT = 400;
    private ArrayList<Bitmap> mImgs;
    // #end

    // #start---------onCreate方法-------
    @Override
    public void onCreate(Bundle savedInstanceState) {
        mImgs = new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    // #end

    // #start --------绑定页面和Fragment-------
    protected int getLayoutResourceId() {
        return R.layout.layout_customer;
    }

    @Override
    public void initViews() {
        // #start ---相关控件声明------------
        vehicle_number = (EditText) mContentView
                .findViewById(R.id.id_text_vehicle_number);
        id_vehicle_number = (LinearLayout) mContentView
                .findViewById(R.id.id_vehicle_number);
        customer_photos = (PhotosLinearLayout) mContentView
                .findViewById(R.id.id_add_customer_photo);
        customer_name = (EditText) mContentView.findViewById(R.id.id_text_name);
        certi_type = (SpinnerCertiType) mContentView
                .findViewById(R.id.id_certi_type_spinner);
        read_card = (Button) mContentView
                .findViewById(R.id.id_button_read_card);
        certi_number = (EditText) mContentView
                .findViewById(R.id.id_text_certi_number);
        birth_day = (EditText) mContentView.findViewById(R.id.id_text_birthday);
        company_name = (EditText) mContentView
                .findViewById(R.id.id_text_company_name);
        tel_number = (EditText) mContentView
                .findViewById(R.id.id_text_tel_number);
        people_address = (EditText) mContentView
                .findViewById(R.id.id_text_address);
        buy_liter = (EditText) mContentView.findViewById(R.id.id_text_liter);
        gasoline_type = (SpinnerGasolineType) mContentView
                .findViewById(R.id.id_text_gasoline_type);// 油种类
        transport_type = (SpinnerTransportType) mContentView
                .findViewById(R.id.id_text_transport_type);// 加载运输方式下拉菜单

        buy_intent = (SpinnerBuyIntent) mContentView
                .findViewById(R.id.id_text_buy_intent_spinner);// 购买用途
        customer_operator = (SpinnerOperator) mContentView
                .findViewById(R.id.id_text_operator_spinner);// 加油员
        is_doubt = (CheckBox) mContentView.findViewById(R.id.id_text_doubt);
        doubt_desc = (EditText) mContentView
                .findViewById(R.id.id_text_doubt_desc);
        doubt_desc_layout = mContentView
                .findViewById(R.id.id_doubt_desc_layout);
        customer_nation = (EditText) mContentView
                .findViewById(R.id.id_text_nation);
        btn_submit = (Button) mContentView
                .findViewById(R.id.id_button_add_cusomer);
        zhengjian_img_iv = (ImageView) mContentView.findViewById(R.id.zhengjian_img_iv);
        zhuapai_img_iv = (ImageView) mContentView.findViewById(R.id.zhuapai_img_iv);
        read_card.setOnClickListener(this);
        is_doubt.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        zhengjian_img_iv.setOnClickListener(this);
        zhuapai_img_iv.setOnClickListener(this);
        // #end

        // #start ----------点击选择运输类型：机动车的话添加车牌号，不是机动车隐藏------------
        transport_type.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View view, int num,
                                       long position) {
                switch (num) {
                    case 0:
                        id_vehicle_number.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        id_vehicle_number.setVisibility(View.GONE);
                        vehicle_number.setText("");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                System.out.println("onNothingSelected");
            }
        });
        // #end

        // #start ----------（未实现）车牌号失去焦点事件vehicle_number-----------
        vehicle_number.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean result) {
                if (!result) {// 失去焦点验证改车牌号是否为重点地区车辆(Code_DangerZone)Utils.codesEqual
                    // 先判断是否为重点地区车辆，再判断是否是本地全国布控车辆
                    String num = vehicle_number.getText().toString().trim();//获得当前输入的车牌号
                    String now_num = Utils.ReadString(MainActivity.mIntails, Utils.KEY_PAIZHAO_NUM);
                    if (!now_num.equals(num)) {//本地存储的号码与输入的号码不用，再判断是否为重点地区车辆
                        if (num.contains("藏") || num.contains("新")) {
                            Utils.WriteString(MainActivity.mIntails, Utils.KEY_PAIZHAO_NUM, num);
                            MainActivity.pager.setCurrentItem(1);
                        }
                    }
                }
            }
        });
        // #start ----------第一次启动页面默认显示添加车牌号的文本框----------------------
        if (transport_type.getSelectedKey() == "0") {// 如果选择的运输方式为机动车
            id_vehicle_number.setVisibility(View.VISIBLE);
        } else if (transport_type.getSelectedKey() == "1") {// 非机动车
            id_vehicle_number.setVisibility(View.GONE);
            vehicle_number.setText("");
        }
    }

    private void loadRead() {
        zhengjian_img_iv.setImageResource(R.drawable.head_img);
        InputStream is = getResources().openRawResource(R.drawable.head_img);
        if (mImgs.size() > 0 && mImgs.get(0) != null) {
            mImgs.remove(0);
        }
        mImgs.add(0, BitmapFactory.decodeStream(is));
        customer_name.setText("成伟");
        certi_number.setText("130623198304152437");
    }

    @Override
    protected void lazyLoad() {
        if (("1").equals(Utils.ReadString(MainActivity.mIntails, Utils.KEY_ZHONGDIAN))) {
            Utils.WriteString(MainActivity.mIntails, Utils.KEY_PAIZHAO_NUM, "");
        }
    }

    // #end

    /**
     * 开启蓝牙，以及拍照，接收返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (BLUETTOOTH_RESULT == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                startReadCard();
            } else {
                Toast.makeText(mContext, R.string.text_bluetooth_not_open,
                        Toast.LENGTH_SHORT).show();
            }
        } else if (Utils.KEY_ZHENGJIAN == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                String path = Utils.getTempImagePath();
                Bitmap bitmap = Utils.decodeBitmapFromFile(path, 800 * 800);
                if (bitmap != null) {
                    if (mImgs.size() > 0 && mImgs.get(0) != null) {
                        mImgs.remove(0);
                        mImgs.add(0, bitmap);
                    } else {
                        mImgs.add(0, bitmap);
                    }
                    zhengjian_img_iv.setImageBitmap(bitmap);
                    if (mImgs.size() == 2) {
                        checkDialog();
                    }
                }
            }
        } else if (Utils.KEY_ZHUAPAI == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                String path = Utils.getTempImagePath();
                Bitmap bitmap = Utils.decodeBitmapFromFile(path, 800 * 800);
                if (bitmap != null) {
                    if (mImgs.size() > 1) {
                        mImgs.remove(1);
                        mImgs.add(1, bitmap);
                    } else {
                        mImgs.add(1, bitmap);
                    }
                    zhuapai_img_iv.setImageBitmap(bitmap);
                    if (mImgs.size() == 2) {
                        checkDialog();
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 页面点击效果
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.zhengjian_img_iv://证件图片点击
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Utils.getTempImagePath())));

                startActivityForResult(intent, Utils.KEY_ZHENGJIAN);
                break;
            case R.id.zhuapai_img_iv://抓拍图片点击
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Utils.getTempImagePath())));

                startActivityForResult(intent, Utils.KEY_ZHUAPAI);
                break;
            case R.id.id_text_doubt://是否为可疑人员
                if (is_doubt.isChecked())
                    doubt_desc_layout.setVisibility(View.VISIBLE);
                else
                    doubt_desc_layout.setVisibility(View.GONE);
                break;
            case R.id.id_button_add_cusomer://点击提交事件
                Utils.hideIM(mContext);
                if (checkPeopleInput()) {
                    AddCustomerTask task = new AddCustomerTask(getGlobalModel(),
                            getAuthorizeModel(), getCustomerModel());
                    task.start(mAddCustomerListener);
                }
                break;
            case R.id.id_button_read_card://读卡按钮
                Utils.hideIM(mContext);//关闭软键盘
//                BluetoothAdapter ba = BluetoothAdapter.getDefaultAdapter();
//                if (null != ba) {
//                    if (!ba.isEnabled()) {
//                        intent = new Intent(
//                                BluetoothAdapter.ACTION_REQUEST_ENABLE);
//                        startActivityForResult(intent, BLUETTOOTH_RESULT);
//                    } else {
//                        startReadCard();
//                    }
//                }
                loadRead();
                break;
        }
    }

    /**
     * 调用系统相机
     */
    private OnClickListener onClickAddPhoto = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Utils.hideIM(mContext);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(Utils.getTempImagePath())));

            startActivityForResult(intent, Utils.CAMRAM_RESULT);
        }
    };

    /**
     * 开始读取身份证信息
     */

    private void startReadCard() {
        String address = Utils.ReadBlueToothAddress(mContext);
        if (Utils.isEmpty(address)) {
            Toast.makeText(mContext, R.string.text_config_bluetooth,
                    Toast.LENGTH_SHORT).show();
        } else {
            showProgressDialog(R.string.text_reading_card);
            Cvr100bTask cvr100bTask = new Cvr100bTask(mContext, address);
            cvr100bTask.setCvr100bListener(mCvr100bListener);
            cvr100bTask.start();
        }
    }

    private Cvr100bListener mCvr100bListener = new Cvr100bListener() {

        @Override
        public void onResult(boolean result, CustomerModel customer) {
            hideProgressDialog();
            if (result) {
                customer_name.setText(customer.CustomerName);
                certi_type.setDefaultSelect();
                certi_number.setText(customer.CustomerIdentityNumber);
                people_address.setText(customer.customerAddress);
                mImgs.set(0, customer.CustomerHeder);//读取证件头像
                zhengjian_img_iv.setImageBitmap(customer.CustomerHeder);
                customer_nation.setText(customer.CustomerNation);
                birth_day.setText(customer.CustomerBirthday);
                checkDialog();
            } else {// 读卡失败！！
                Toast.makeText(mContext, R.string.text_read_card_fail,
                        Toast.LENGTH_SHORT).show();
            }

        }
    };

    /**
     * 验证输入的内容是否合法
     *
     * @return 合法为true
     */
    private boolean checkPeopleInput() {
//        MainActivity.adapter
        String name = customer_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, R.string.text_please_input_people_name,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int type = certi_type.getSelectedItemPosition();
        if (type == -1) {
            Toast.makeText(mContext, R.string.text_please_input_certi_type,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String number = certi_number.getText().toString();
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(mContext, R.string.text_please_input_certi_number,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String tel = tel_number.getText().toString();
        if (TextUtils.isEmpty(tel)) {
            Toast.makeText(mContext, R.string.text_please_input_tel_number,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String liter = buy_liter.getText().toString();
        if (TextUtils.isEmpty(liter)) {
            Toast.makeText(mContext, R.string.text_please_input_buy_liter,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String gasoline = gasoline_type.getSelectedText();
        if (TextUtils.isEmpty(gasoline)) {
            Toast.makeText(mContext, R.string.text_please_input_gasoline_type,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        String transport = transport_type.getSelectedText();
        if (TextUtils.isEmpty(transport)) {
            Toast.makeText(mContext, R.string.text_please_input_transport_type,
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        String intent = buy_intent.getSelectedText();
        if (TextUtils.isEmpty(intent)) {
            Toast.makeText(mContext, R.string.text_please_input_buy_intent,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String operator = customer_operator.getSelectedText();
        if (TextUtils.isEmpty(operator)) {
            Toast.makeText(mContext,
                    R.string.text_please_input_people_operator,
                    Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获得页面上输入的内容
     *
     * @return 输入内容存储的model
     */
    private CustomerModel getCustomerModel() {
        CustomerModel model = new CustomerModel();
        model.CustomerId = Utils.createGUID();
        model.CustomerName = Utils.getString(customer_name);
        model.CustomerIdentityType = certi_type.getSelectedKey();
        model.CustomerIdentityNumber = Utils.getString(certi_number);
        model.CustomerCompany = Utils.getString(company_name);
        model.CustomerLinkway = Utils.getString(tel_number);
        model.customerAddress = Utils.getString(people_address);
        model.Count = Utils.getString(buy_liter);
        if (Utils.isEmpty(model.Count))
            model.Count = "0";
        model.GasolineType = gasoline_type.getSelectedKey();
        model.TransportType = transport_type.getSelectedKey();
        model.Purpose = buy_intent.getSelectedKey();
        model.CustomerHeder = customer_photos.getHeader();
        model.EmployeeId = customer_operator.getSelectedKey();
        model.Suspicious = is_doubt.isChecked() ? "true" : "false";
        model.SuspiciousReson = Utils.getString(doubt_desc);
        model.CustomerNation = Utils.getString(customer_nation);
        model.CompanyId = getAuthorizeModel().UserId;
        model.DepartmentId = model.CompanyId;
        model.CreateTime = Utils.getCSTimeString();
        model.CreateIP = "127.0.0.1";
        model.Comment = "AddCustomer";
        model.CustomerBirthday = Utils.getString(birth_day);
        if (Utils.isEmpty(model.CustomerBirthday)) {
            model.CustomerBirthday = "1990-01-01";
        }
        model.VehicleNumber = Utils.getString(vehicle_number);
        model.CustomerImageBitmaps = customer_photos.getBitmaps();

        return model;
    }

    /**
     * 将销售信息添加到服务器
     */
    private AddCustomerListener mAddCustomerListener = new AddCustomerListener() {
        @Override
        public void OnResult(boolean result, CustomerModel customer,
                             String strMessage) {
            hideProgressDialog();
            if (!result) {
                String text = getResources().getString(
                        R.string.text_add_customer_fail)
                        + "\n" + strMessage;

                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            } else {
                DBHelper.insertCustomer(mContext, getCustomerModel());
                Toast.makeText(mContext, R.string.text_add_customer_sucess,
                        Toast.LENGTH_SHORT).show();

                customer.CustomerIdentityType = certi_type.getSelectedText();
                customer.Purpose = buy_intent.getSelectedText();
                customer.EmployeeId = customer_operator.getSelectedText();
                customer.Suspicious = is_doubt.isChecked() ? "是" : "否";
                customer.CreateTime = customer.CreateTime.replace("T", " ");

                new CacheCustomerTask(mContext, customer).start();

                clearCustomerForm();
            }
        }

    };

    /**
     * 清空所有输入的内容
     */
    private void clearCustomerForm() {
        customer_photos.clearPhotos();
        customer_name.setText("");
        certi_type.setDefaultSelect();
        certi_number.setText("");
        birth_day.setText("");
        tel_number.setText("");
        company_name.setText("");
        people_address.setText("");
        buy_liter.setText("");
        buy_intent.setDefaultSelect();
        is_doubt.setChecked(false);
        doubt_desc.setText("");
        customer_photos.setHeader(null);
        customer_photos.clearPhotos();
        transport_type.setDefaultSelect();
        id_vehicle_number.setVisibility(View.VISIBLE);
        vehicle_number.setText("");
        zhengjian_img_iv.setImageResource(R.drawable.default_header2);
        zhuapai_img_iv.setImageResource(R.drawable.default_header1);
        mImgs = new ArrayList<>();
    }


    /**
     * 自定义弹出框checkDialog
     */
    private void checkDialog() {
        if (mImgs != null) {
            if (mImgs.size() == 2) {
                CustomDialog.Builder builder = new CustomDialog.Builder(
                        mContext);
                builder.setMessage("请核对证件与持证人是否一致？", "验证落实不到位将承担责任！！", true);
                builder.setView(mImgs, true);
                builder.setTitle("民警提示");
                builder.setPositiveButton("一致",
                        new DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton(
                        "不一致",// 不一致关闭
                        new android.content.DialogInterface.OnClickListener() {
                            public void onClick(
                                    final DialogInterface dialog, int which) {
                                // 报警处理未实现
                                clearCustomerForm();
                                Toast.makeText(mContext, R.string.text_replace_shuru,
                                        Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });

                builder.create().show();
            }
        }
    }
}