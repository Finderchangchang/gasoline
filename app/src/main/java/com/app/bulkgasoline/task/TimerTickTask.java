package com.app.bulkgasoline.task;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class TimerTickTask extends Thread {

	private long timelong = 1500;
	private boolean stopthread;
	private TimerListener mTimerListener;

	public interface TimerListener {
		public void onTimer();
	}

	public void setTimerListener(TimerListener listener) {
		mTimerListener = listener;
	}

	public void startTimer() {
		start();
	}

	public void stopTimer() {
		stopthread = true;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (mTimerListener != null)
				mTimerListener.onTimer();
		}
	};

	private boolean doInThread = true;

	@Override
	public void run() {
		Looper.prepare();

		while (!stopthread) {
			if (doInThread) {
				if (mTimerListener != null)
					mTimerListener.onTimer();
			} else {
				mHandler.sendEmptyMessage(1);
			}

			try {
				Thread.sleep(timelong);
			} catch (InterruptedException e) {
			}
		}

		super.run();
	}
}
