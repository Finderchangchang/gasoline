package com.app.bulkgasoline;

import android.util.AttributeSet;
import android.widget.Spinner;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class BaseSpinner extends Spinner {

	private Bitmap arrow;

	public BaseSpinner(Context context) {
		super(context);

		init(context);
	}

	public BaseSpinner(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	private void init(Context context) {
		arrow = BitmapFactory.decodeResource(getResources(),
				R.drawable.spinner_arrow);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int left = getWidth() - arrow.getWidth() - 30;
		int top = (getHeight() - arrow.getHeight()) / 2;

		canvas.drawBitmap(arrow, left, top, new Paint());
	}
}
