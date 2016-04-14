package com.app.bulkgasoline;

import java.io.File;

import com.app.bulkgasoline.model.VehicleModel;
import com.app.bulkgasoline.task.AddAlarmVehicleTask;
import com.app.bulkgasoline.task.AddAlarmVehicleTask.AddAlarmVehicleListener;
import com.app.bulkgasoline.task.CacheVehicleTask;
import com.app.bulkgasoline.utils.DBHelper;
import com.app.bulkgasoline.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 添加车辆信息
 */
public class FragmentAddVehicle extends BaseFragment implements OnClickListener {

    private PhotosLinearLayout vehicle_photos;
    private SpinnerVehicleType vehicle_type;
    private EditText vehicle_color;
    private EditText vehicle_number;
    private EditText vehicle_peoples;
    private EditText vehicle_direction;
    private SpinnerOperator vehicle_operator;
    private SpinnerGasolineType vehicle_gasolineType;
    private SpinnerBuyIntent vehicle_buyIntents;
    private Button btn_submit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected int getLayoutResourceId() {
        return R.layout.layout_vehicle;
    }

    @Override
    public void initViews() {
        vehicle_photos = (PhotosLinearLayout) mContentView
                .findViewById(R.id.id_add_vehicle_photo);
        vehicle_type = (SpinnerVehicleType) mContentView
                .findViewById(R.id.id_text_vehicle_type_spinner);
        vehicle_color = (EditText) mContentView
                .findViewById(R.id.id_text_vehicle_color);
        vehicle_gasolineType = (SpinnerGasolineType) mContentView
                .findViewById(R.id.id_text_gasoline_type);
        vehicle_buyIntents = (SpinnerBuyIntent) mContentView
                .findViewById(R.id.id_text_buy_intent_spinner);
        vehicle_number = (EditText) mContentView
                .findViewById(R.id.id_text_vehicle_number);
        vehicle_peoples = (EditText) mContentView
                .findViewById(R.id.id_text_vehicle_peoples);
        vehicle_direction = (EditText) mContentView
                .findViewById(R.id.id_text_vehicle_direction);
        vehicle_operator = (SpinnerOperator) mContentView
                .findViewById(R.id.id_text_vehicle_operator_spinner);
        btn_submit = (Button) mContentView
                .findViewById(R.id.id_button_add_vehicle);
    }

    @Override
    protected void lazyLoad() {
        String carNum = Utils.ReadString(MainActivity.mIntails, Utils.KEY_PAIZHAO_NUM);
        if ("" != carNum) {
            vehicle_number.setText(carNum);
            vehicle_number.setFocusable(false);
            Utils.WriteString(MainActivity.mIntails, Utils.KEY_ZHONGDIAN, "1");
        } else {
            vehicle_number.setText("");
            vehicle_number.setFocusable(true);
        }
        vehicle_photos.setFixedHeader(false);
        vehicle_photos.setClickPohoListener(onClickAddPhoto);
        btn_submit.setOnClickListener(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (Utils.CAMRAM_RESULT == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                String path = Utils.getTempImagePath();
                Bitmap bitmap = Utils.decodeBitmapFromFile(path, 800 * 800);
                if (bitmap != null)
                    vehicle_photos.addPhoto(bitmap);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view == btn_submit) {
            onButtonAddVehicle(view);
        }
    }

    // 拍照
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

    public void onButtonAddVehicle(View view) {
        Utils.hideIM(mContext);

        if (Utils.fillTestData()) {
            vehicle_color.setText("白色");
            vehicle_number.setText("冀F58T86");
            vehicle_peoples.setText("1");
            vehicle_direction.setText("1");
        }

        if (checkVehicleInput()) {
            startAddVehicle();
        }
    }

    private boolean checkVehicleInput() {
        String type = vehicle_type.getSelectedText();
        if (TextUtils.isEmpty(type)) {
            Toast.makeText(mContext, R.string.text_please_input_vehicle_type,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String color = Utils.getString(vehicle_color);
        if (TextUtils.isEmpty(color)) {
            Toast.makeText(mContext, R.string.text_please_input_vehicle_color,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String number = Utils.getString(vehicle_number);
        if (TextUtils.isEmpty(number)) {
            Toast.makeText(mContext, R.string.text_please_input_vehicle_number,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        String operator = vehicle_operator.getSelectedText();
        if (TextUtils.isEmpty(operator)) {
            Toast.makeText(mContext,
                    R.string.text_please_input_vehicle_operator,
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private VehicleModel getVehicleModel() {
        VehicleModel model = new VehicleModel();

        //model.VehicleId = Utils.createGUID();

        model.VehicleType = vehicle_type.getSelectedKey();
        // model.VehicleColor = vehicle_color_spinner.getSelectedKey();

        model.VehicleColor = Utils.getString(vehicle_color);

        model.VehicleNumber = Utils.getString(vehicle_number);
        model.VehiclePersonCount = Utils.getString(vehicle_peoples);
        if (Utils.isEmpty(model.VehiclePersonCount))
            model.VehiclePersonCount = "0";
        model.VehicleDirection = Utils.getString(vehicle_direction);
        model.EmployeeId = vehicle_operator.getSelectedKey();
        model.CompanyId = getAuthorizeModel().UserId;
        model.Comment = "AddVehicle";
        model.CreateTime = Utils.getCSTimeString();
        model.VehicleImageBitmaps = vehicle_photos.getBitmaps();
        return model;
    }

    private void startAddVehicle() {
        DBHelper.insertVehicle(mContext, getVehicleModel());
        AddAlarmVehicleTask task = new AddAlarmVehicleTask(getGlobalModel(),
                getAuthorizeModel(), getVehicleModel());

        task.start(mAddAlarmVehicleListener);
        showProgressDialog(R.string.text_adding_vehicle_info);
    }

    private AddAlarmVehicleListener mAddAlarmVehicleListener = new AddAlarmVehicleListener() {
        @Override
        public void OnResult(boolean result, VehicleModel vehicle,
                             String message) {
            hideProgressDialog();
            if (!result) {
                String text = getResources().getString(
                        R.string.text_add_vehicle_fail)
                        + "\n" + message;
                Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            } else {
                vehicle.VehicleType = vehicle_type.getSelectedText();
                vehicle.EmployeeId = vehicle_operator.getSelectedText();
                vehicle.CreateTime = vehicle.CreateTime.replace("T", " ");
                new CacheVehicleTask(mContext, vehicle).start();

                Toast.makeText(mContext, R.string.text_add_vehicle_sucess,
                        Toast.LENGTH_SHORT).show();
                if ("" != Utils.ReadString(MainActivity.mIntails, Utils.KEY_PAIZHAO_NUM)) {
                    MainActivity.pager.setCurrentItem(0);//跳回添加销售信息页面
                }
                clearVehicleForm();
            }
        }
    };

    private void clearVehicleForm() {
        vehicle_photos.clearPhotos();
        vehicle_type.setDefaultSelect();
        vehicle_color.setText("");
        vehicle_number.setText("");
        vehicle_peoples.setText("");
        vehicle_direction.setText("");
        vehicle_number.setFocusable(true);
    }
}