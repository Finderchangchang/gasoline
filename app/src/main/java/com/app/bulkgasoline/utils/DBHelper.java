package com.app.bulkgasoline.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.app.bulkgasoline.model.CustomerModel;
import com.app.bulkgasoline.model.VehicleModel;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class DBHelper {

	public static final int DB_VERSION = 2;
	public static final String DB_NAME = "gasoline.db";

	public static void checkUpdate(Context context) {
		SQLiteDatabase db = context.openOrCreateDatabase(getDBPath(),
				Context.MODE_PRIVATE, null);
		checkUpdate(db);
		db.close();
	}

	public synchronized static void insertCustomer(Context context,
			CustomerModel customer) {
		SQLiteDatabase db = context.openOrCreateDatabase(getDBPath(),
				Context.MODE_PRIVATE, null);
		checkUpdate(db);

		ContentValues cv = new ContentValues();

		cv.put("CustomerName", customer.CustomerName);
		cv.put("CustomerIdentityType", customer.CustomerIdentityType);
		cv.put("CustomerIdentityNumber", customer.CustomerIdentityNumber);
		cv.put("CustomerCompany", customer.CustomerCompany);
		cv.put("CustomerLinkway", customer.CustomerLinkway);
		cv.put("customerAddress", customer.customerAddress);
		cv.put("Suspicious", customer.Suspicious);
		cv.put("SuspiciousReson", customer.SuspiciousReson);
		cv.put("Purpose", customer.Purpose);
		cv.put("Count", customer.Count);
		cv.put("EmployeeId", customer.EmployeeId);
		cv.put("CreateTime", customer.CreateTime);
		cv.put("CustomerImage", saveBitmap(customer.CustomerHeder));
		cv.put("CustomerPhotos", saveBitmaps(customer.CustomerImageBitmaps));
		cv.put("GasolineType", customer.GasolineType);
		cv.put("TransportType", customer.TransportType);
		db.insert("customers", null, cv);
		db.close();
	}

	public synchronized static void insertVehicle(Context context,
			VehicleModel vehicle) {
		SQLiteDatabase db = context.openOrCreateDatabase(getDBPath(),
				Context.MODE_PRIVATE, null);
		checkUpdate(db);
		ContentValues cv = new ContentValues();

		cv.put("VehicleType", vehicle.VehicleType);
		cv.put("VehicleColor", vehicle.VehicleColor);
		cv.put("VehicleNumber", vehicle.VehicleNumber);
		cv.put("VehiclePersonCount", vehicle.VehiclePersonCount);
		cv.put("VehicleDirection", vehicle.VehicleDirection);
		cv.put("EmployeeId", vehicle.EmployeeId);
		cv.put("CreateTime", vehicle.CreateTime);
		cv.put("VehiclePhotos", saveBitmaps(vehicle.VehicleImageBitmaps));

		db.insert("vehicles", null, cv);
		db.close();
	}

	public static void queryVehicles(Context context, String key, String value,
			ArrayList<VehicleModel> vehicles) {
		SQLiteDatabase db = context.openOrCreateDatabase(getDBPath(),
				Context.MODE_PRIVATE, null);
		checkUpdate(db);

		try {
			Cursor c = null;

			if (Utils.isEmpty(key) || Utils.isEmpty(value))
				c = db.rawQuery("select * from vehicles order by _id desc",
						null);
			else {
				String sql = String
						.format("select * from vehicles where %s like '%%%s%%' order by _id desc",
								key, value);
				c = db.rawQuery(sql, null);
			}

			while (c.moveToNext()) {
				VehicleModel vehicle = new VehicleModel();

				vehicle.VehicleType = c.getString(c
						.getColumnIndex("VehicleType"));
				vehicle.VehicleColor = c.getString(c
						.getColumnIndex("VehicleColor"));
				vehicle.VehicleNumber = c.getString(c
						.getColumnIndex("VehicleNumber"));
				vehicle.VehiclePersonCount = c.getString(c
						.getColumnIndex("VehiclePersonCount"));
				vehicle.VehicleDirection = c.getString(c
						.getColumnIndex("VehicleDirection"));
				vehicle.EmployeeId = c
						.getString(c.getColumnIndex("EmployeeId"));
				vehicle.CreateTime = c
						.getString(c.getColumnIndex("CreateTime"));
				vehicle.VehicleImages = c.getString(c
						.getColumnIndex("VehiclePhotos"));

				vehicles.add(vehicle);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
	}

	public static void queryCustomers(Context context, String key,
			String value, ArrayList<CustomerModel> customers) {
		SQLiteDatabase db = context.openOrCreateDatabase(getDBPath(),
				Context.MODE_PRIVATE, null);
		checkUpdate(db);

		try {
			Cursor c = null;

			if (Utils.isEmpty(key) || Utils.isEmpty(value)) {
				c = db.rawQuery("select * from customers order by _id desc",
						null);
				System.out.println("select * from customers order by _id desc");
			} else {
				String sql = String
						.format("select * from customers where %s like '%%%s%%' order by _id desc",
								key, value);
				System.out.println("sql:" + sql);
				c = db.rawQuery(sql, null);
			}

			while (c.moveToNext()) {
				CustomerModel customer = new CustomerModel();

				customer.CustomerName = c.getString(c
						.getColumnIndex("CustomerName"));
				customer.CustomerIdentityType = c.getString(c
						.getColumnIndex("CustomerIdentityType"));
				customer.CustomerIdentityNumber = c.getString(c
						.getColumnIndex("CustomerIdentityNumber"));
				customer.CustomerCompany = c.getString(c
						.getColumnIndex("CustomerCompany"));
				customer.CustomerLinkway = c.getString(c
						.getColumnIndex("CustomerLinkway"));
				customer.customerAddress = c.getString(c
						.getColumnIndex("customerAddress"));
				customer.Suspicious = c.getString(c
						.getColumnIndex("Suspicious"));
				customer.SuspiciousReson = c.getString(c
						.getColumnIndex("SuspiciousReson"));
				customer.Purpose = c.getString(c.getColumnIndex("Purpose"));
				customer.Count = c.getString(c.getColumnIndex("Count"));
				customer.EmployeeId = c.getString(c
						.getColumnIndex("EmployeeId"));
				customer.CreateTime = c.getString(c
						.getColumnIndex("CreateTime"));
				customer.CustomerImage = c.getString(c
						.getColumnIndex("CustomerImage"));
				customer.CustomerImages = c.getString(c
						.getColumnIndex("CustomerPhotos"));

				customers.add(customer);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		db.close();
	}

	public static Bitmap loadBitmap(String name) {
		try {
			String path = Environment.getExternalStorageDirectory()
					+ "/gasoline/" + name;
			return BitmapFactory.decodeFile(path);
		} catch (Exception e) {
		}

		return null;
	}

	public static ArrayList<Bitmap> loadBitmaps(String names) {
		ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
		if (Utils.isEmpty(names))
			return bitmaps;

		String[] array = names.split(";");
		if (array == null || array.length == 0)
			return bitmaps;

		for (String name : array) {
			Bitmap bmp = loadBitmap(name);
			if (bmp != null)
				bitmaps.add(bmp);
		}

		return bitmaps;
	}

	private static String saveBitmap(Bitmap bitmap) {
		if (bitmap == null)
			return "";

		String path = Environment.getExternalStorageDirectory() + "/gasoline/";
		String filename = Utils.createGUID() + ".png";

		File f = new File(path, filename);
		if (f.exists()) {
			f.delete();
		}
		try {
			FileOutputStream out = new FileOutputStream(f);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return filename;
	}

	private static String saveBitmaps(ArrayList<Bitmap> bitmaps) {
		if (bitmaps == null || bitmaps.size() == 0)
			return "";

		String filenames = "";
		for (Bitmap bitmap : bitmaps) {
			filenames += saveBitmap(bitmap) + ";";
		}

		return filenames;
	}

	private static void checkUpdate(SQLiteDatabase db) {
		int version = getDBVersion(db);
		if (version != DB_VERSION)
			resetDatabase(db);
	}

	private static String getDBPath() {
		String path = Environment.getExternalStorageDirectory() + "/gasoline/";
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();

		return path + DB_NAME;
	}

	private static int getDBVersion(SQLiteDatabase db) {
		int version = 0;

		try {
			Cursor c = db.rawQuery("select * from version", null);
			if (c.moveToNext()) {
				version = c.getInt(c.getColumnIndex("version"));
			}
			c.close();
		} catch (Exception e) {
			version = 0;
		}

		return version;
	}

	private static void resetVersionTable(SQLiteDatabase db) {
		try {
			db.execSQL("drop table if exists version");
			db.execSQL("create table version (_id INTEGER PRIMARY KEY AUTOINCREMENT, version INTEGER)");
			db.execSQL("insert into version values (NULL, ?)",
					new Object[] { DB_VERSION });
		} catch (Exception e) {

		}
	}

	private static void resetCustomerTable(SQLiteDatabase db) {
		try {
			db.execSQL("drop table if exists customers");
			String sql = "create table customers (";
			sql += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
			sql += "CustomerName VARCHAR,";
			sql += "CustomerIdentityType VARCHAR,";
			sql += "CustomerIdentityNumber VARCHAR,";
			sql += "CustomerCompany VARCHAR,";
			sql += "CustomerLinkway VARCHAR,";
			sql += "customerAddress VARCHAR,";
			sql += "Suspicious VARCHAR,";
			sql += "SuspiciousReson VARCHAR,";
			sql += "Purpose VARCHAR,";
			sql += "Count VARCHAR,";
			sql += "EmployeeId VARCHAR,";
			sql += "CreateTime VARCHAR,";
			sql += "CustomerImage VARCHAR,";
			sql += "CustomerPhotos VARCHAR,";
			sql += "GasolineType VARCHAR,";
			sql += "TransportType VARCHAR";
			sql += ")";
			db.execSQL(sql);
		} catch (Exception e) {

		}
	}

	private static void resetVehicleTable(SQLiteDatabase db) {
		try {
			db.execSQL("drop table if exists vehicles");
			db.execSQL("drop table if exists vehicles");
			String sql = "create table vehicles (";
			sql += "_id INTEGER PRIMARY KEY AUTOINCREMENT,";
			sql += "VehicleType VARCHAR,";
			sql += "VehicleColor VARCHAR,";
			sql += "VehicleNumber VARCHAR,";
			sql += "VehiclePersonCount VARCHAR,";
			sql += "VehicleDirection VARCHAR,";
			sql += "EmployeeId VARCHAR,";
			sql += "CreateTime VARCHAR,";
			sql += "VehiclePhotos VARCHAR";
			sql += ")";
			db.execSQL(sql);
		} catch (Exception e) {

		}
	}

	private static void resetDatabase(SQLiteDatabase db) {
		try {
			resetVersionTable(db);
			resetCustomerTable(db);
			resetVehicleTable(db);
		} catch (Exception e) {

		}
	}

	public static void deleteCache() {
		String path = Environment.getExternalStorageDirectory() + "/gasoline/";
		RecursionDeleteFile(new File(path));
	}

	public static void RecursionDeleteFile(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				RecursionDeleteFile(f);
			}
			file.delete();
		}
	}
}
