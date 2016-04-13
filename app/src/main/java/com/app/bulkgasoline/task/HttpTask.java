package com.app.bulkgasoline.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.app.bulkgasoline.http.Cryptography;
import com.app.bulkgasoline.http.HTTPResponse;
import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.utils.Utils;

public abstract class HttpTask extends Thread {

    protected GlobalModel mGlobalModel;
    protected AuthorizeModel mAuthorizeModel;

    protected String mObjectiveType;
    protected Map<String, String> mHeaders;
    protected Map<String, String> mForms;

    protected String mHttpUrl;

    protected static String REQUEST_TYPE_DATA = "Data";
    protected static String REQUEST_TYPE_VIEW = "View";

    protected static String SESSIONID = "SessionId";
    protected static String USERID = "UserId";
    protected static String PASSWORD = "Password";
    protected static String DATATYPE = "Data";
    protected static String INFOMATION = "Information";

    public abstract String getActionName();

    public abstract String getDataType();

    public abstract void onInvokeReturn(boolean result,
                                        Map<String, String> data, Message message);

    public HttpTask(GlobalModel global, AuthorizeModel authorize) {
        mGlobalModel = global;
        mAuthorizeModel = authorize;

        mForms = new HashMap<String, String>();
        mHeaders = new HashMap<String, String>();
    }

    protected boolean isPost() {
        return false;
    }

    protected void addArgument(String key, String value) {
        if (!Utils.isEmpty(value))
            mForms.put(key, value);
    }

    protected void addHeader(String key, String value) {
        if (!Utils.isEmpty(value))
            mHeaders.put(key, value);
    }

    protected void prepareData() {
        // Url
        prepareHttpUrl();

        // Infomation 参数
        prepareForms();

        // Headers
        prepareHeaders();
    }

    protected void prepareHttpUrl() {
        // if (Utils.isEmpty(mGlobalModel.ServerPort)) {
        // mHttpUrl = String.format("http://%s/Services/Index.html?Action=%s",
        // mGlobalModel.ServerName, getActionName());
        // } else {
        // mHttpUrl = String.format(
        // "http://%s:%s/Services/Index.html?Action=%s",
        // mGlobalModel.ServerName, mGlobalModel.ServerPort,
        // getActionName());
        // }
        // System.out.println("url" + mHttpUrl);
        mHttpUrl = String.format("http://%s:%s/Services/Index.html?Action=%s",
                mGlobalModel.ServerName, mGlobalModel.ServerPort, getActionName());
    }

    protected void prepareForms() {

    }

    protected void prepareHeaders() {
        addHeader(SESSIONID, mAuthorizeModel.SessionId);
        addHeader(USERID, mAuthorizeModel.UserId);
        addHeader(PASSWORD, mAuthorizeModel.Password);
        addHeader(DATATYPE, getDataType());
        if (!isPost()) {
            addHeader(INFOMATION, getInfomation());
        }
    }

    private String getInfomationKeyValue() {
        return String.format("%s=%s", INFOMATION,
                Utils.URLEncoder(getInfomation()));
    }

    protected Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message message) {
            HTTPResponse resp = (HTTPResponse) message.obj;
            if (resp == null || resp.code != 200) {
                onInvokeReturn(false, null, null);
            } else {
                resp.content = Cryptography.DecryptString(resp.content, "");
                onInvokeReturn(true, parseXml(resp.content), message);
            }
        }
    };

    @Override
    public void run() {
        prepareData();
        Message msg = new Message();
        msg.what = 1;

        HTTPResponse resp = null;
        if (isPost()) {
            Log.i("post之前", "post");
            resp = httpPost(mHttpUrl, getInfomationKeyValue());
            Log.i("post之前after", "post" + resp.content);
        } else {
            resp = httpGet(mHttpUrl);
        }
        msg.obj = resp;
        onRunEnd(msg);
        mHandler.sendMessage(msg);
    }

    protected void onRunEnd(Message message) {

    }

    protected boolean isModelInfomation() {
        return false;
    }

    protected String getInfomation() {
        String infomation = "";
        for (String key : mForms.keySet()) {
            String value = mForms.get(key);
            if (Utils.isEmpty(value))
                continue;

            if (!Utils.isEmpty(infomation))
                infomation += "&";

            infomation += String.format("%s=%s", key, Utils.URLEncoder(value));
        }

        if (!Utils.isEmpty(infomation)) {
            infomation = Cryptography.EncryptString(infomation,
                    mAuthorizeModel.SessionId);
        }

        return infomation;
    }

    public HTTPResponse httpGet(String url) {
        HTTPResponse resp = new HTTPResponse();
        try {
            HttpGet get = new HttpGet(url);

            for (String key : mHeaders.keySet()) {
                String value = mHeaders.get(key);
                if (!Utils.isEmpty(value))
                    get.addHeader(key, value);
            }
            HttpResponse response = httpClient().execute(get);
            StatusLine status = response.getStatusLine();
            resp.code = status.getStatusCode();
            resp.content = getResponseContent(response);
            return resp;
        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        } catch (Exception e) {

        }
        return resp;
    }

    public HTTPResponse httpPost(String url, String entity) {
        HTTPResponse resp = new HTTPResponse();
        try {
            HttpPost post = new HttpPost(url);

            post.setHeader("Content-Type", "application/x-www-form-urlencoded");
            for (String key : mHeaders.keySet()) {
                String value = mHeaders.get(key);
                Log.i("--------" + key, value);
                if (!Utils.isEmpty(value))
                    post.addHeader(key, value);
            }
            post.setEntity(new StringEntity(entity));

            HttpResponse response = httpClient().execute(post);
            StatusLine status = response.getStatusLine();
            Log.e("error", status.toString());
            resp.code = status.getStatusCode();
            resp.content = getResponseContent(response);

            return resp;
        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }
        return resp;
    }

    public Map<String, String> parseXml(String content) {
        Map<String, String> data = new HashMap<String, String>();
        Jxml xml = new Jxml();
        if (xml.SetDoc(content) && xml.FindElem("InvokeReturn")) {
            xml.IntoElem();
            while (xml.FindElem()) {
                String key, value;
                key = xml.GetTagName();
                if ("Object".compareToIgnoreCase(key) == 0) {
                    xml.IntoElem();
                    while (xml.FindElem()) {
                        key = xml.GetTagName();
                        value = xml.GetData();
                        data.put(key, value);
                    }
                    xml.OutOfElem();
                } else {
                    value = xml.GetData();
                    data.put(key, value);
                }
            }
            xml.OutOfElem();
        }

        return data;
    }

    public static DefaultHttpClient httpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpParams params = httpClient.getParams();
        params.setParameter(ClientPNames.HANDLE_REDIRECTS, Boolean.TRUE);
        params.setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8"); // 默认为ISO-8859-1
        params.setParameter(CoreProtocolPNames.HTTP_ELEMENT_CHARSET, "UTF-8"); // 默认为US-ASCII
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 5000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);

        return httpClient;
    }

    public static DefaultHttpClient httpClient(int timeout) {
        DefaultHttpClient httpClient = httpClient();
        if (timeout > 0) {
            httpClient.getParams().setParameter(
                    CoreConnectionPNames.CONNECTION_TIMEOUT, timeout);
            httpClient.getParams().setParameter(
                    CoreConnectionPNames.SO_TIMEOUT, timeout);
        }
        return httpClient;
    }

    private static String getResponseContent(HttpResponse response) {
        InputStream is = null;
        String content = null;
        try {
            is = response.getEntity().getContent();
            content = stream2String(is);

        } catch (IOException e) {
            content = "";
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        return content;

    }

    public static String stream2String(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024 * 4];
        int len = 0;

        try {
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {

        }

        String result = null;
        try {
            result = new String(baos.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // bug??
            result = new String(baos.toByteArray());
        } finally {
            try {
                baos.close();
            } catch (IOException e) {
            }
        }
        return result;
    }

}
