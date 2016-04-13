package com.app.bulkgasoline.task;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.app.bulkgasoline.model.CustomerModel;
import com.app.bulkgasoline.utils.Utils;
import com.ivsign.android.IDCReader.IDCReaderSDK;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class Cvr100bTask extends Thread {

    private Context mContext;
    BluetoothAdapter myBluetoothAdapter = null;
    BluetoothServerSocket mBThServer = null;
    BluetoothSocket mBTHSocket = null;
    InputStream mmInStream = null;
    OutputStream mmOutStream = null;
    int Readflage = -99;

    byte[] cmd_SAM = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x12, (byte) 0xFF, (byte) 0xEE};
    byte[] cmd_find = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x01, 0x22};
    byte[] cmd_selt = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x20, 0x02, 0x21};
    byte[] cmd_read = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x03, 0x30, 0x01, 0x32};
    byte[] cmd_sleep = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x00, 0x02};
    byte[] cmd_weak = {(byte) 0xAA, (byte) 0xAA, (byte) 0xAA, (byte) 0x96, 0x69, 0x00, 0x02, 0x01, 0x03};
    byte[] recData = new byte[1500];

    String DEVICE_NAME1 = "CVR-100B";
    String DEVICE_NAME2 = "IDCReader";
    String DEVICE_NAME3 = "COM2";
    String DEVICE_NAME4 = "BOLUTEK";

    UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    String[] decodeInfo = new String[10];

    private String mAddress = "";

    private boolean read_ok;
    private Bitmap mBitmap;

    public Cvr100bTask(Context context, String address) {
        mContext = context;
        mAddress = address;
    }

    private CustomerModel mCustomer;
    private Cvr100bListener cvr100bListener;

    public interface Cvr100bListener {
        public void onResult(boolean result, CustomerModel customer);
    }

    public void setCvr100bListener(Cvr100bListener listener) {
        cvr100bListener = listener;
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (mCustomer != null)
                cvr100bListener.onResult(true, mCustomer);
            else
                cvr100bListener.onResult(false, null);
        }
    };

    public void stopRun() {
        closeConnect();
    }

    private void init() {
        mBitmap = null;
        read_ok = false;
        mBitmap = null;
        decodeInfo = new String[10];

        copyCvr100Files();
    }

    private void copyCvr100Files() {
        String wltlib = Utils.getRootPath() + "/wltlib/";
        File file = new File(wltlib);
        if (!file.exists())
            file.mkdirs();
        else {
            if (!file.isDirectory()) {
                file.delete();
                file.mkdirs();
            }
        }

        String datPath = wltlib + "base.dat";
        if (!new File(datPath).exists())
            Utils.saveAssetsFile(mContext.getAssets(), "base.dat", datPath);

        String licPath = wltlib + "license.lic";
        if (!new File(licPath).exists())
            Utils.saveAssetsFile(mContext.getAssets(), "license.lic", licPath);
    }

    @Override
    public void run() {
        Looper.prepare();
        try {
            init();
            startConnect();
            startReadCard();
            closeConnect();
            returnData();
            return;
        } catch (Exception e) {
            closeConnect();
        }
        mCustomer = null;
        mHandler.sendEmptyMessage(0);
    }

    private void returnData() throws IOException {
        if (read_ok) {
            mCustomer = new CustomerModel();
            mCustomer.CustomerName = decodeInfo[0].trim();
            mCustomer.CustomerSex = decodeInfo[1].trim();
            mCustomer.CustomerNation = decodeInfo[2].trim();
            mCustomer.customerAddress = decodeInfo[4].trim();

            mCustomer.CustomerIdentityNumber = decodeInfo[5].trim();

            mCustomer.CustomerBirthday = decodeInfo[3].trim();
            if (!Utils.isEmpty(mCustomer.CustomerBirthday) &&
                    mCustomer.CustomerBirthday.length() == 8) {
                mCustomer.CustomerBirthday = String.format("%s-%s-%s",
                        mCustomer.CustomerBirthday.substring(0, 4),
                        mCustomer.CustomerBirthday.substring(4, 6),
                        mCustomer.CustomerBirthday.substring(6, 8));
            }
            //if(mBitmap != null)
            //	mCustomer.CustomerImage = Utils.encodeBitmap(mBitmap);

            mCustomer.CustomerHeder = mBitmap;

            mHandler.sendEmptyMessage(0);
        } else {
            mCustomer = null;
            mHandler.sendEmptyMessage(0);
        }
    }

    private void ReadCard() throws IOException {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                Readflage = -2;//连接异常
                return;
            }
            mmOutStream.write(cmd_find);
            Thread.sleep(200);

            int datalen = mmInStream.read(recData);
            if (recData[9] == -97) {
                mmOutStream.write(cmd_selt);
                Thread.sleep(200);
                datalen = mmInStream.read(recData);
                if (recData[9] == -112) {
                    mmOutStream.write(cmd_read);
                    Thread.sleep(1000);
                    byte[] tempData = new byte[1500];
                    if (mmInStream.available() > 0) {
                        datalen = mmInStream.read(tempData);
                    } else {
                        Thread.sleep(500);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        }
                    }
                    int flag = 0;
                    if (datalen < 1294) {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                        Thread.sleep(1000);
                        if (mmInStream.available() > 0) {
                            datalen = mmInStream.read(tempData);
                        } else {
                            Thread.sleep(500);
                            if (mmInStream.available() > 0) {
                                datalen = mmInStream.read(tempData);
                            }
                        }
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                    } else {
                        for (int i = 0; i < datalen; i++, flag++) {
                            recData[flag] = tempData[i];
                        }
                    }
                    tempData = null;
                    if (flag == 1295) {
                        if (recData[9] == -112) {

                            byte[] dataBuf = new byte[256];
                            for (int i = 0; i < 256; i++) {
                                dataBuf[i] = recData[14 + i];
                            }
                            String TmpStr = new String(dataBuf, "UTF16-LE");
                            TmpStr = new String(TmpStr.getBytes("UTF-8"));
                            decodeInfo[0] = TmpStr.substring(0, 15);
                            decodeInfo[1] = TmpStr.substring(15, 16);
                            decodeInfo[2] = TmpStr.substring(16, 18);
                            decodeInfo[3] = TmpStr.substring(18, 26);
                            decodeInfo[4] = TmpStr.substring(26, 61);
                            decodeInfo[5] = TmpStr.substring(61, 79);
                            decodeInfo[6] = TmpStr.substring(79, 94);
                            decodeInfo[7] = TmpStr.substring(94, 102);
                            decodeInfo[8] = TmpStr.substring(102, 110);
                            decodeInfo[9] = TmpStr.substring(110, 128);

                            //照片解码
                            try {
                                int ret = IDCReaderSDK.Init();
                                if (ret == 0) {
                                    byte[] datawlt = new byte[1384];
                                    byte[] byLicData = {(byte) 0x05, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x5B, (byte) 0x03, (byte) 0x33, (byte) 0x01, (byte) 0x5A, (byte) 0xB3, (byte) 0x1E, (byte) 0x00};
                                    for (int i = 0; i < 1295; i++) {
                                        datawlt[i] = recData[i];
                                    }
                                    int t = IDCReaderSDK.unpack(datawlt, byLicData);
                                    if (t == 1) {
                                        Readflage = 1;//读卡成功
                                    } else {
                                        Readflage = 6;//照片解码异常
                                    }
                                } else {
                                    Readflage = 6;//照片解码异常
                                }
                            } catch (Exception e) {
                                Readflage = 6;//照片解码异常
                            }

                        } else {
                            Readflage = -5;//读卡失败！
                        }
                    } else {
                        Readflage = -5;//读卡失败
                    }
                } else {
                    Readflage = -4;//选卡失败
                }
            } else {
                Readflage = -3;//寻卡失败
            }

        } catch (IOException e) {
            Readflage = -99;//读取数据异常
        } catch (InterruptedException e) {
            Readflage = -99;//读取数据异常
        }
    }

    private void startReadCard() {
        int readcount = 15;
        try {
            while (readcount > 1) {
                ReadCard();
                readcount = readcount - 1;
                if (Readflage > 0) {
                    readcount = 0;
                    /*
					String text = "姓名：" + decodeInfo[0] + "\n" + "性别：" + decodeInfo[1] + "\n" + "民族：" + decodeInfo[2] + "\n"
			        		+ "出生日期：" + decodeInfo[3] + "\n" + "地址：" + decodeInfo[4] + "\n" + "身份号码：" + decodeInfo[5] + "\n"
			        		+ "签发机关：" + decodeInfo[6] + "\n" + "有效期限：" + decodeInfo[7] + "-" + decodeInfo[8] + "\n"
			        		+ decodeInfo[9] + "\n"; 					
					*/

                    read_ok = true;
                    if (Readflage == 1) {
                        FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory() + "/wltlib/zp.bmp");
                        mBitmap = BitmapFactory.decodeStream(fis);
                        //saveBitmap(mBitmap);

                        fis.close();
                        //image.setImageBitmap(bmp);
                    } else {
                        //fw.write("照片解码失败，请检查路径" + Environment.getExternalStorageDirectory() + "/wltlib/\n");
                    }
                } else {
                    if (Readflage == -2) {
                        //fw.write("蓝牙连接异常\n");
                    }
                    if (Readflage == -3) {
                        //fw.write("无卡或卡片已读过\n");
                    }
                    if (Readflage == -4) {
                        //fw.write("无卡或卡片已读过\n");
                    }
                    if (Readflage == -5) {
                        //fw.write("读卡失败\n");
                    }
                    if (Readflage == -99) {
                        //fw.write("操作异常\n");
                    }
                }
                Thread.sleep(100);
            }

        } catch (IOException e) {

            //image.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.face));
        } catch (InterruptedException e) {

        }
    }

    private void startConnect() throws Exception {
        myBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothDevice device = myBluetoothAdapter.getRemoteDevice(mAddress);

        try {
            myBluetoothAdapter.enable();
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);// 使得蓝牙处于可发现模式，持续时间150s
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 150);
            mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);

            @SuppressWarnings("deprecation")
            int sdk = Integer.parseInt(Build.VERSION.SDK);
            if (sdk >= 10) {
                mBTHSocket = device.createInsecureRfcommSocketToServiceRecord(MY_UUID);
            } else {
                mBTHSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
            }

            mBThServer = myBluetoothAdapter.listenUsingRfcommWithServiceRecord("myServerSocket", MY_UUID);

            mBTHSocket.connect();
            mmInStream = mBTHSocket.getInputStream();
            mmOutStream = mBTHSocket.getOutputStream();
        } catch (IOException e) {
            throw new Exception("设备连接异常！");
        }

        if (mmInStream == null || mmInStream == null) {
            throw new Exception("设备连接失败,请检查设备设置！");
        }
    }

    private void closeConnect() {
        try {
            if ((mmInStream == null) || (mmInStream == null)) {
                return;
            }
            mmOutStream.close();
            mmOutStream = null;
            mmInStream.close();
            mmInStream = null;
            mBTHSocket.close();
            mBTHSocket = null;
            mBThServer.close();
            mBThServer = null;

        } catch (IOException e) {
        } catch (Exception e) {
        }
    }
}
