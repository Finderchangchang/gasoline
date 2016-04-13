package com.app.bulkgasoline.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.app.bulkgasoline.task.Jxml;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class Utils {

    public static final String KEY_SESSIONID = "key_sessionid";
    public static final String KEY_USERNAME = "key_username";
    public static final String KEY_PASSWORD = "key_password";
    public static final String KEY_REMEMBER = "key_remember";

    public static final String KEY_HAS_CODES = "key_hascodes";
    public static final String KEY_CODE_NAMES = "key_codenames";

    public static final String KEY_HAS_OPERATORS = "key_hasoperators";
    public static final String KEY_OPERATORS = "key_operators";

    public static final String KEY_SERVERNAME = "key_servername";
    public static final String KEY_PORT = "key_port";

    public static final String KEY_BLUETOOTH_ADDRESS = "key_bluetooth_address";

    public static final int CAMRAM_RESULT = 500;
    public static final int KEY_ZHENGJIAN = 501;//证件照
    public static final int KEY_ZHUAPAI = 502;//抓拍照

    public static boolean fillTestData() {
        return false;
    }

    static public String createGUID() {
        return UUID.randomUUID().toString();
    }

    static public double getDouble(TextView view) {
        if (view == null)
            return 0;

        try {
            return Double.parseDouble(view.getText().toString());
        } catch (Exception e) {
        }

        return 0;
    }

    static public int getInt(TextView view) {
        if (view == null)
            return 0;

        try {
            return Integer.parseInt(view.getText().toString());
        } catch (Exception e) {
        }

        return 0;
    }

    static public int getInt(Spinner view) {
        if (view == null)
            return 0;

        try {
            return view.getSelectedItemPosition();
        } catch (Exception e) {
        }

        return -1;
    }

    static public String getString(TextView view) {
        if (view == null)
            return "";

        try {
            return view.getText().toString();
        } catch (Exception e) {
        }

        return "";
    }

    static public String getString(Spinner view) {
        if (view == null)
            return "";

        try {
            return view.toString();
        } catch (Exception e) {
        }

        return "";
    }

    public static String Preferences_name = "BULKGASOLINE";

    public static String ReadString(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(key, "");
    }

    public static void WriteString(Context context, String key, String value) {
        SharedPreferences sp = getSharedPreferences(context);
        // 存入数据
        Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static int ReadInt(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(key, 0);
    }

    public static void WriteInt(Context context, String key, int value) {
        SharedPreferences sp = getSharedPreferences(context);
        // 存入数据
        Editor editor = sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void WriteBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = getSharedPreferences(context);
        // 存入数据
        Editor editor = sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static Set<String> ReadStringSet(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getStringSet(key, new LinkedHashSet<String>());
    }

    public static void WriteStringSet(Context context, String key,
                                      Set<String> value) {
        SharedPreferences sp = getSharedPreferences(context);
        // 存入数据
        Editor editor = sp.edit();
        editor.putStringSet(key, value);

        editor.commit();
    }

    public static boolean hasRememberPassword(Context context) {
        if (Utils.ReadBoolean(context, KEY_REMEMBER)) {
            if (!isEmpty(Utils.ReadString(context, KEY_USERNAME))
                    && !isEmpty(Utils.ReadString(context, KEY_PASSWORD))) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasSessionId(Context context) {
        return !Utils.isEmpty(Utils.ReadString(context, KEY_SESSIONID));
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    /**
     * 关闭软键盘
     *
     * @param context
     */
    public static void hideIM(Context context) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) context).getCurrentFocus();
        if (v == null)
            imm.hideSoftInputFromWindow(null, 0);
        else
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static Uri startCamra(Activity context, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 这里我们插入一条数据，ContentValues是我们希望这条记录被创建时包含的数据信息
        // 这些数据的名称已经作为常量在MediaStore.Images.Media中,有的存储在MediaStore.MediaColumn中了
        // ContentValues values = new ContentValues();

        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "testing");
        values.put(MediaStore.Images.Media.DESCRIPTION, "this is description");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Uri uri =
        // context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        // values);
        Uri uri = Uri.fromFile(new File(getDataPath() + "/temp/"
                + System.currentTimeMillis() + ".tmp"));
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri); // 这样就将文件的存储方式和uri指定到了Camera应用中

        // 由于我们需要调用完Camera后，可以返回Camera获取到的图片，
        // 所以，我们使用startActivityForResult来启动Camera
        context.startActivity(intent);
        return uri;
    }

    public static String getDataPath() {
        File defaultStorageFile = Environment.getExternalStorageDirectory();
        return String.format("%s/Gasoline",
                defaultStorageFile.getAbsolutePath());
    }

    public static String getDataPath(String name) {
        return String.format("%s/%s", getDataPath(), name);
    }

    public static String getRootPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static boolean hasCodeNames(Context context) {
        return Utils.ReadBoolean(context, KEY_HAS_CODES);
    }

    public static boolean loadCodeNames(Context context, Set<String> codeNames) {
        if (Utils.ReadBoolean(context, KEY_HAS_CODES)) {
            Set<String> names = Utils.ReadStringSet(context, KEY_CODE_NAMES);
            if (names != null) {
                codeNames.clear();
                codeNames.addAll(names);

                return true;
            }

        }

        return false;
    }

    public static void writeCodeNames(Context context, String xml) {
        Utils.WriteString(context, KEY_CODE_NAMES, xml);
        Utils.WriteBoolean(context, KEY_HAS_CODES, true);
    }

    /**
     * 加载字典信息
     */
    public static boolean loadCodes(Context context, String codeName,
                                    ArrayList<String> codeKeys, ArrayList<String> codeValues) {
        String xml = Utils.ReadString(context, codeName);
        if (Utils.isEmpty(xml))
            return false;
        return parseCodeXml(xml, codeKeys, codeValues);
    }

    public static boolean codesEqual(Context context, String codeName,
                                     ArrayList<String> codeKeys, ArrayList<String> codeValues) {
        String xml = Utils.ReadString(context, codeName);
        if (Utils.isEmpty(xml))
            return false;
        return parseCodeXml(xml, codeKeys, codeValues);

    }

    /**
     * 加载加油员信息
     */
    public static boolean loadOperators(Context context,
                                        ArrayList<String> operatorIds, ArrayList<String> operators) {
        if (!hasOperators(context)) {
            return false;
        }// 判断键值是否存在
        String xml = Utils.ReadString(context, KEY_OPERATORS);
        if (Utils.isEmpty(xml)) {
            return false;
        }// 判断xml是否为空
        return parseOperatorXml(xml, operatorIds, operators);
    }

    public static boolean hasOperators(Context context) {
        return Utils.ReadBoolean(context, KEY_HAS_OPERATORS);
    }

    public static boolean ReadBoolean(Context context, String key) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getBoolean(key, false);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(Preferences_name,
                Context.MODE_PRIVATE);// 默认操作模式
    }

    public static boolean parseCodeXml(String content,
                                       ArrayList<String> codeKeys, ArrayList<String> codeValues) {
        Jxml xml = new Jxml();
        if (xml.SetDoc(content) && xml.FindElem("InvokeReturn")) {
            xml.IntoElem();
            while (xml.FindElem()) {
                String key, value;
                key = xml.GetTagName();
                if ("Object".compareToIgnoreCase(key) == 0) {
                    codeKeys.clear();
                    codeValues.clear();

                    xml.IntoElem();
                    while (xml.FindElem("CodeModel")) {
                        xml.IntoElem();
                        while (xml.FindElem()) {
                            key = xml.GetTagName();
                            value = xml.GetData();
                            if (key.compareToIgnoreCase("Key") == 0) {
                                codeKeys.add(value);
                            } else if (key.compareToIgnoreCase("Value") == 0) {
                                codeValues.add(value);
                            }
                        }
                        xml.OutOfElem();
                    }
                    xml.OutOfElem();
                } else {
                    value = xml.GetData();
                }
            }
            xml.OutOfElem();

            return true;
        }

        return false;
    }

    public static void writeCodes(Context context, String codeName, String xml) {
        Utils.WriteString(context, codeName, xml);
    }

    public static boolean parseOperatorXml(String content,
                                           ArrayList<String> ids, ArrayList<String> values) {
        Map<String, String> data = new HashMap<String, String>();
        Jxml xml = new Jxml();
        if (xml.SetDoc(content) && xml.FindElem("InvokeReturn")) {
            xml.IntoElem();
            while (xml.FindElem()) {
                String key, value;
                key = xml.GetTagName();
                if ("Object".compareToIgnoreCase(key) == 0) {
                    ids.clear();
                    values.clear();

                    xml.IntoElem();
                    while (xml.FindElem("EmployeeModel")) {
                        xml.IntoElem();
                        while (xml.FindElem()) {
                            key = xml.GetTagName();
                            if (key.compareToIgnoreCase("EmployeeId") == 0) {
                                value = xml.GetData();
                                ids.add(value);
                            } else if (key.compareToIgnoreCase("EmployeeName") == 0) {
                                value = xml.GetData();
                                values.add(value);
                            }
                        }
                        xml.OutOfElem();
                    }
                    xml.OutOfElem();
                } else {
                    value = xml.GetData();
                    data.put(key, value);
                }
            }
            xml.OutOfElem();

            return true;
        }

        return false;
    }

    public static void writeOperators(Context context, String xml) {
        Utils.WriteString(context, KEY_OPERATORS, xml);
        Utils.WriteBoolean(context, KEY_HAS_OPERATORS, true);
    }

    public static String ReadBlueToothAddress(Context context) {
        return Utils.ReadString(context, KEY_BLUETOOTH_ADDRESS);
    }

    public static void WriteBlueToothAddress(Context context, String address) {
        Utils.WriteString(context, KEY_BLUETOOTH_ADDRESS, address);
    }

    public static void Sleep(long mi) {
        try {
            Thread.sleep(200);
        } catch (Exception e) {
        }
    }

    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date());
    }

    public static String getCSTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(new Date()).replace(" ", "T");
    }

    public static String encodeImageView(ImageView imageview) {
        String imageString = "";
        try {
            imageview.setDrawingCacheEnabled(true);
            Bitmap bitmap = imageview.getDrawingCache();
            imageString = encodeBitmap(bitmap);
            imageview.setDrawingCacheEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageString;
    }

    public static String encodeBitmap(Bitmap bitmap) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
            return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)
                    .trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String XMlEncode(String strData) {
        if (strData == null)
            return "";

        strData = strData.replace("&", "&amp;");
        strData = strData.replace("<", "&lt;");
        strData = strData.replace(">", "&gt;");
        strData = strData.replace("&apos;", "&apos;");
        strData = strData.replace("\"", "&quot;");

        return strData;
    }

    @SuppressWarnings("deprecation")
    public static String URLEncoder(String strData) {
        if (strData == null)
            return "";

        return URLEncoder.encode(strData);
    }

    public static String[] LinkedSetToArray(LinkedHashSet<String> datas) {
        String[] array = new String[datas.size()];
        int index = 0;
        for (Iterator<String> iter = datas.iterator(); iter.hasNext(); ) {
            array[index] = (String) iter.next();
            index++;
        }
        return array;
    }

    public static boolean saveAssetsFile(AssetManager assetManager,
                                         String strName, String strTarget) {
        boolean bSucc = false;
        final int BUF_SIZE = 10124;
        try {
            InputStream is = assetManager.open(strName);
            if (is == null)
                return false;
            File fOut = new File(strTarget);
            FileOutputStream ofs = new FileOutputStream(fOut);
            if (is != null && ofs != null) {
                byte[] bBuf = new byte[BUF_SIZE];
                int nRead, length = BUF_SIZE;
                while ((nRead = is.read(bBuf, 0, length)) > 0) {
                    ofs.write(bBuf, 0, nRead);
                }
                ofs.close();
                is.close();
                bSucc = true;
            }
        } catch (Exception e) {
        }
        return bSucc;
    }

    public static String getTempImagePath() {

        String path = Environment.getExternalStorageDirectory() + "/gasoline/";
        File file = new File(path);
        if (!file.exists())
            file.mkdirs();

        return path + "temp.image";
    }

    public static Bitmap decodeBitmapFromFile(String filePath,
                                              int maxNumOfPixels) {
        Bitmap bitmap = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, opts);

        opts.inSampleSize = computeSampleSize(opts, -1, maxNumOfPixels);
        opts.inJustDecodeBounds = false;

        try {
            bitmap = BitmapFactory.decodeFile(filePath, opts);
        } catch (Exception e) {
        }
        return bitmap;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    private static Bitmap previewBitmap = null;

    public static void setPreviewBitmap(Bitmap bitmap) {
        previewBitmap = bitmap;
    }

    public static Bitmap getPreviewBitmap() {
        return previewBitmap;
    }
}
