package com.app.bulkgasoline;

import com.app.bulkgasoline.model.VehicleModel;
import com.app.bulkgasoline.utils.DBHelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 车辆详细信息
 */
public class DetailVehicleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_vehicle_detail);

        initTitleBarText();
        initVehicleDetail();
    }

    private void initTitleBarText() {
        TextView title_text = (TextView) findViewById(R.id.id_title_bar_text);
        title_text.setText(R.string.text_vehicle_detail);

        ImageView setting_back = (ImageView) findViewById(R.id.id_button_back);

        setting_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initVehicleDetail() {
        Intent intent = getIntent();
        VehicleModel vehicle = (VehicleModel) intent
                .getSerializableExtra("VehicleModel");
        if (vehicle == null)
            return;

        PhotosLinearLayout photos = (PhotosLinearLayout) findViewById(R.id.id_vehicle_photos);
        photos.setReadOnly(true);
        photos.setFixedHeader(false);
        photos.setPhotos(DBHelper.loadBitmaps(vehicle.VehicleImages));

        TextView vehicle_type = (TextView) findViewById(R.id.id_text_vehicle_type);
        vehicle_type.setText(vehicle.VehicleType);

        TextView vehicle_color = (TextView) findViewById(R.id.id_text_vehicle_color);
        vehicle_color.setText(vehicle.VehicleColor);

        TextView vehicle_number = (TextView) findViewById(R.id.id_text_vehicle_number);
        vehicle_number.setText(vehicle.VehicleNumber);

        TextView count = (TextView) findViewById(R.id.id_text_vehicle_peoples);
        count.setText(vehicle.VehiclePersonCount);

        TextView direction = (TextView) findViewById(R.id.id_text_vehicle_direction);
        direction.setText(vehicle.VehicleDirection);

        TextView operator = (TextView) findViewById(R.id.id_text_operator);
        operator.setText(vehicle.EmployeeId);

        TextView datetime = (TextView) findViewById(R.id.id_text_datetime);
        datetime.setText(vehicle.CreateTime);
    }
}
