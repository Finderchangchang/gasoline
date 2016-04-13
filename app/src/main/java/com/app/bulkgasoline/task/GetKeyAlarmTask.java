package com.app.bulkgasoline.task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Message;
import android.util.Log;

import com.app.bulkgasoline.http.HTTPResponse;
import com.app.bulkgasoline.model.AuthorizeModel;
import com.app.bulkgasoline.model.PoliceModel;

/**
 * 获得辖区在线民警
 */
public class GetKeyAlarmTask extends HttpTask {

    private ArrayList<PoliceModel> mPolicesList;// 用来存储PoliceModel集合
    private String mPolicesXml;
    private String DEPARTMENTID = "departmentId";
    String departmentIds = "";

    public GetKeyAlarmTask(GlobalModel global, AuthorizeModel authorize,
                           String departmentId) {
        super(global, authorize);
        departmentIds = departmentId;
        mPolicesList = new ArrayList<PoliceModel>();
    }

    private GetKeyAlarmListener mListener;

    public interface GetKeyAlarmListener {
        void OnResult(boolean result, ArrayList<PoliceModel> mPolice,
                      String message);
    }

    public void start(GetKeyAlarmListener listener) {
        mListener = listener;
        start();
    }

    // 访问后台的方法
    @Override
    public String getActionName() {
        return "GetPoliceCollection";
    }

    @Override
    public String getDataType() {
        return REQUEST_TYPE_DATA;
    }

    // 给接口后台传值
    @Override
    public void prepareForms() {
        departmentIds = "130602500000";
        super.addArgument(DEPARTMENTID, departmentIds);
    }

    protected void onRunEnd(Message message) {
        HTTPResponse resp = (HTTPResponse) message.obj;
        if (resp != null && resp.code == 200) {

        }
    }

    @Override
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
                    while (xml.FindElem("PoliceModel")) {
                        PoliceModel police = new PoliceModel();
                        xml.IntoElem();
                        while (xml.FindElem()) {
                            key = xml.GetTagName();
                            value = xml.GetData();
                            if (key.compareToIgnoreCase("PoliceName") == 0) {
                                police.PoliceName = value;
                            } else if (key.compareToIgnoreCase("PoliceLinkway") == 0) {
                                police.PoliceLinkway = value;
                            } else if (key.compareToIgnoreCase("PoliceImageId") == 0) {
                                police.PoliceImageId = value;
                            }
                        }
                        mPolicesList.add(police);
                        xml.OutOfElem();
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

    @Override
    public void onInvokeReturn(boolean result, Map<String, String> data,
                               Message msg) {
        if (mListener != null) {
            if (result) {
                String strSuccess = data.get("Success");
                String strMessage = data.get("Message");
                mListener.OnResult(strSuccess.compareToIgnoreCase(strSuccess) == 0,
                        mPolicesList, strMessage);
            } else {
                mListener.OnResult(false, null, "网络错误！");
            }
        }
    }
}
