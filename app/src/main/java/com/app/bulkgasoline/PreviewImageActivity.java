package com.app.bulkgasoline;

import com.app.bulkgasoline.image.GestureImageView;
import com.app.bulkgasoline.utils.Utils;

import android.graphics.Bitmap;
import android.os.Bundle;

public class PreviewImageActivity extends BaseActivity {

	private GestureImageView image_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		initView();
	}

	private void initView() {
		image_view = (GestureImageView) findViewById(R.id.id_preview_imageview);

		Bitmap bitmap = Utils.getPreviewBitmap();

		if (bitmap != null)
			image_view.setImageBitmap(bitmap);
	}

	@Override
	protected void onDestroy() {
		image_view.setImageBitmap(null);
		Utils.setPreviewBitmap(null);

		super.onDestroy();
	}
}
