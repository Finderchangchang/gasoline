package com.app.bulkgasoline.task;

import com.app.bulkgasoline.utils.DBHelper;

public class DeleteCacheTask extends Thread{

	public DeleteCacheTask() {
	}

	@Override
	public void run() {	
		DBHelper.deleteCache();
	}
}
